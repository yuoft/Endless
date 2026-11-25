package com.yuo.endless.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCShaderInstance;
import com.yuo.endless.client.lib.CCUniform;
import com.yuo.endless.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

/**
 * 星空（cosmic）着色器的状态中心。
 * <p>
 * 这里集中管理：
 * <ul>
 *   <li>两个常驻 shader（新版 cosmic / 旧版 old_cosmic）的注册与切换；</li>
 *   <li>uniform 引用与上传；</li>
 *   <li>图集里 10 张星空贴图的 UV 矩形。</li>
 * </ul>
 * 所有渲染调用点都必须通过 {@link #applyCosmicUniforms(float, float, float, float)} 或
 * {@link #updateCosmicUVs()} 取数据，不要在别处再写一份图集查找，否则很容易出现
 * “写入了 missingno 的 UV / 重载后 uniform 不更新” 的花屏、马赛克问题。
 */
@Mod.EventBusSubscriber(modid = Endless.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AvaritiaShaders {

    /** 10 张星空贴图在方块图集里的 UV 矩形，顺序 u0, v0, u1, v1 */
    public static final float[] COSMIC_UVS = new float[40];
    /** 只在渲染线程使用，避免每帧新建数组 */
    private static final float[] UVS_SCRATCH = new float[40];
    private static final int COSMIC_SPRITE_COUNT = 10;

    public static boolean inventoryRender = false;
    public static int renderTime;
    public static float renderFrame;

    /** 当前生效的 shader */
    public static CCShaderInstance cosmicShader;
    public static CCUniform cosmicTime;
    public static CCUniform cosmicYaw;
    public static CCUniform cosmicPitch;
    public static CCUniform cosmicExternalScale;
    public static CCUniform cosmicOpacity;
    public static CCUniform cosmicUVs;

    // 两个 shader 常驻
    private static CCShaderInstance shaderNew;
    private static CCShaderInstance shaderOld;
    /** shader 是否可用（注册完成且 uniform 已就绪）；为 false 时调用方必须跳过星空渲染 */
    public static boolean shaderReady = false;
    /** 图集 UV 是否已经成功解析过一次 */
    private static boolean cosmicUVsValid = false;

    public static void init(RegisterShadersEvent event) {
        // 重载开始：只清掉“新实例”的槽位，旧实例继续工作到新实例注册完成再原子替换。
        // 这样重载期间不会出现 cosmicShader == null 的空窗，也不会让 RenderType 拿到 null shader。
        shaderNew = null;
        shaderOld = null;

        event.registerShader(
                CCShaderInstance.create(event.getResourceProvider(),
                        EndlessUtils.fa("cosmic"), DefaultVertexFormat.BLOCK),
                loaded -> {
                    shaderNew = (CCShaderInstance) loaded;
                    setupShader(shaderNew);
                    syncActiveShader();
                });

        event.registerShader(
                CCShaderInstance.create(event.getResourceProvider(),
                        EndlessUtils.fa("old_cosmic"), DefaultVertexFormat.BLOCK),
                loaded -> {
                    shaderOld = (CCShaderInstance) loaded;
                    setupShader(shaderOld);
                    syncActiveShader();
                });
    }

    /**
     * 每 tick 调用：把“当前配置期望的 shader”与“正在使用的 shader”对齐。
     * 资源重载、配置切换都靠这里收敛，不需要重启游戏。
     */
    public static void refreshShader() {
        syncActiveShader();
    }

    private static void syncActiveShader() {
        boolean old = ModConfig.SERVER.isOldCosmicRender.get();
        CCShaderInstance desired = old ? shaderOld : shaderNew;
        if (desired == null) {
            // 目标实例还没注册好（重载进行中）：继续用当前的旧实例，不做任何切换
            return;
        }

        if (desired != cosmicShader || !shaderReady) {
            activateShader(desired);
        }
    }

    /** 当前实际生效的是否为旧版着色器（供排查/显示用） */
    public static boolean isUsingOldShader() {
        return cosmicShader != null && cosmicShader == shaderOld;
    }

    // 保留给外部查询：未就绪时返回 null
    public static CCShaderInstance getCosmicShader() {
        return shaderReady && cosmicShader != null ? cosmicShader : null;
    }

    /** 每个 shader 只注册一次 onApply，lambda 捕获自己的 time uniform，避免串到另一个 shader */
    private static void setupShader(CCShaderInstance shader) {
        CCUniform time = shader.getUniform("time");
        shader.onApply(() -> {
            if (time != null) time.set((float) renderTime + renderFrame);
        });
    }

    /** 切换当前使用的 shader，并刷新全部 uniform 引用 */
    private static void activateShader(CCShaderInstance shader) {
        cosmicShader = shader;
        cosmicTime = shader.getUniform("time");
        cosmicYaw = shader.getUniform("yaw");
        cosmicPitch = shader.getUniform("pitch");
        cosmicExternalScale = shader.getUniform("externalScale");
        cosmicOpacity = shader.getUniform("opacity");
        cosmicUVs = shader.getUniform("cosmicuvs");
        shaderReady = true;
        cosmicUVsValid = false;

        // 新 shader 的 uniform 缓存还是 json 里的默认值（cosmicuvs 全 0、opacity 0），
        // 必须立刻用真实数据覆盖并强制上传，否则重载后第一帧就是花屏/马赛克。
        if (cosmicTime != null) {
            cosmicTime.set((float) renderTime + renderFrame);
            cosmicTime.markDirty();
        }
        updateCosmicUVs();
        forceUploadUniforms();
    }

    /**
     * 从方块图集里重新解析 10 张星空贴图的 UV。
     * 只有 10 张全部解析成功才提交，避免重载中途拿到缺失贴图而写入错误坐标。
     *
     * @return 是否成功（成功时同时把 UV 写进了当前 uniform）
     */
    public static boolean updateCosmicUVs() {
        Minecraft mc = Minecraft.getInstance();
        Function<ResourceLocation, TextureAtlasSprite> atlas = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        if (atlas == null) return false;

        for (int i = 0; i < COSMIC_SPRITE_COUNT; i++) {
            ResourceLocation id = EndlessUtils.fa("shader/cosmic_" + i);
            TextureAtlasSprite sprite = atlas.apply(id);
            // apply() 找不到时会返回 missingno，必须按名字校验
            if (sprite == null || !id.equals(sprite.contents().name())) {
                return false;
            }
            UVS_SCRATCH[i * 4] = sprite.getU0();
            UVS_SCRATCH[i * 4 + 1] = sprite.getV0();
            UVS_SCRATCH[i * 4 + 2] = sprite.getU1();
            UVS_SCRATCH[i * 4 + 3] = sprite.getV1();
        }

        System.arraycopy(UVS_SCRATCH, 0, COSMIC_UVS, 0, COSMIC_UVS.length);
        cosmicUVsValid = true;
        if (cosmicUVs != null) {
            cosmicUVs.set(COSMIC_UVS);
        }
        return true;
    }

    /**
     * 星空渲染的统一入口：刷新 UV 并写入全部 uniform。所有调用点都用它，避免各写一份。
     * 未就绪或图集不完整时返回 false，调用方应放弃本次星空渲染（回退到普通模型），
     * 而不是把 null/0 值交给 OpenGL。
     */
    public static boolean applyCosmicUniforms(float yaw, float pitch, float externalScale, float opacity) {
        if (getCosmicShader() == null) return false;
        // 每帧开始时 renderTick 已经刷新过；这里只在“还没有有效 UV”时补一次
        if (!cosmicUVsValid && !updateCosmicUVs()) return false;

        if (cosmicTime != null) cosmicTime.set((float) renderTime + renderFrame);
        if (cosmicYaw != null) cosmicYaw.set(yaw);
        if (cosmicPitch != null) cosmicPitch.set(pitch);
        if (cosmicExternalScale != null) cosmicExternalScale.set(externalScale);
        if (cosmicOpacity != null) cosmicOpacity.set(opacity);
        if (cosmicUVs != null) cosmicUVs.set(COSMIC_UVS);
        return true;
    }

    /** 强制把当前 uniform 缓存完整重传一次（切换 shader / 重载后使用） */
    private static void forceUploadUniforms() {
        if (cosmicTime != null) {
            cosmicTime.set((float) renderTime + renderFrame);
            cosmicTime.markDirty();
        }
        if (cosmicYaw != null) cosmicYaw.markDirty();
        if (cosmicPitch != null) cosmicPitch.markDirty();
        if (cosmicExternalScale != null) cosmicExternalScale.markDirty();
        if (cosmicOpacity != null) cosmicOpacity.markDirty();
        if (cosmicUVs != null) {
            cosmicUVs.set(COSMIC_UVS);
            cosmicUVs.markDirty();
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            if (!Minecraft.getInstance().isPaused()) ++renderTime;
            refreshShader(); // 配置切换 / 重载后最多 1 tick 收敛
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != Phase.START) return;
        if (!Minecraft.getInstance().isPaused()) {
            renderFrame = event.renderTickTime;
        }
        // 每帧渲染前刷新图集 UV（仅在 Shader 就绪时刷新）
        if (shaderReady) {
            updateCosmicUVs();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPre(ScreenEvent.Render.Pre e) {
        inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPost(ScreenEvent.Render.Post e) {
        inventoryRender = false;
    }
}
