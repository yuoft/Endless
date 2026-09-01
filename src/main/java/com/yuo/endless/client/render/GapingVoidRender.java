package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yuo.endless.client.lib.*;
import com.yuo.endless.entity.GapingVoidEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

public class GapingVoidRender extends EntityRenderer<GapingVoidEntity> {
    private static final ResourceLocation VOID = EndlessUtils.fa("textures/entity/void.png");
    private static final ResourceLocation VOID1 = EndlessUtils.fa("textures/entity/void_halo.png");

    private final CCModel hemisphere;

    private static final RenderType VOID_HALO = RenderType.create("endless:void_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID1, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false));


    private static final RenderType VOID_HEMISPHERE = RenderType.create("endless:void_hemisphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES,
            256, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_SHADOW_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID, false, false))
                    .setCullState(RenderType.NO_CULL).createCompositeState(false));

    private static final RenderType VOID_DISTORT = RenderType.create("endless:void_distort", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256,
            RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> DistortShaders.distortShader))
                    .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );

    public GapingVoidRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.hemisphere = new OBJParser(EndlessUtils.fa("models/hemisphere.obj")).parse().get("model");
    }

    @Override
    public void render(GapingVoidEntity entityIn, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        float age = entityIn.getAge() + partialTicks;
        Colour colour = getColour(age); // 光环颜色
        double scale = GapingVoidEntity.getVoidScale(age);
        double haloCord = 0.58D * scale;
        double haloScaleDist = 2.2D * scale;
        Vec3 cam = this.entityRenderDispatcher.camera.getPosition();
        double dx = entityIn.getX() - cam.x();
        double dy = entityIn.getY() - cam.y();
        double dz = entityIn.getZ() - cam.z();
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len <= haloScaleDist) {
            double close = (haloScaleDist - len) / haloScaleDist;
            haloCord *= 1.0D + close * close * close * close * 1.5D;
        }
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees((float) (Math.atan2(dx, dz) * 57.29577951308232f)));
        stack.mulPose(Axis.XP.rotationDegrees((float)(Math.atan2(Math.sqrt(dx * dx + dz * dz), dy) * 57.29577951308232D + 90.0D)));

        //外部光环
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));
//        TransformingVertexConsumer consHalo = new TransformingVertexConsumer(bufferIn.getBuffer(VOID_HALO), stack);
//        consHalo.vertex(-haloCord, 0.0D, -haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 0.0F).endVertex();
//        consHalo.vertex(-haloCord, 0.0D, haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 1.0F).endVertex();
//        consHalo.vertex(haloCord, 0.0D, haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 1.0F).endVertex();
//        consHalo.vertex(haloCord, 0.0D, -haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 0.0F).endVertex();
        renderDistort(bufferIn, stack, age, haloCord);
        stack.popPose();


        //内部球体
//        stack.scale((float)scale, (float)scale, (float)scale);
//        CCRenderState cc = CCRenderState.instance();
//        cc.reset();
//        cc.bind(VOID_HEMISPHERE, bufferIn, stack);
//        cc.baseColour = colour.rgba();
//        this.hemisphere.render(cc);
        stack.popPose();
    }

    /**
     * 扭曲渲染
     */
    private void renderDistort(MultiBufferSource bufferIn, PoseStack stack, float age, double haloCord){
        // 在 render 方法中，渲染扭曲环之前
        ShaderInstance shader = DistortShaders.distortShader;
        if (shader != null) {
            shader.apply(); // 激活当前着色器程序
            DistortShaders.distortTime.set(age / 20.0f); // 时间参数，可调节速度
            DistortShaders.distortStrength.set(2f);    // 扭曲强度
        }
        // 然后绑定 RenderType 并渲染
        TransformingVertexConsumer cons = new TransformingVertexConsumer(bufferIn.getBuffer(VOID_DISTORT), stack);
        double size = haloCord * 2;
        float alpha = 0.8f; // 可根据 age 调整
        cons.vertex(-size, 0, -size).color(1,1,1,alpha).uv(0,0).endVertex();
        cons.vertex(-size, 0,  size).color(1,1,1,alpha).uv(0,1).endVertex();
        cons.vertex( size, 0,  size).color(1,1,1,alpha).uv(1,1).endVertex();
        cons.vertex( size, 0, -size).color(1,1,1,alpha).uv(1,0).endVertex();
    }

    private static Colour getColour(double age) {
        double life = age / 186.0D;
        double f = Math.max(0.0, (life - 0.95) / 0.05);
        f = Math.max(f, 1.0 - life * 30.0);
        return new ColourRGBA(f, f, f, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(GapingVoidEntity entity) {
        return VOID;
    }

    private static ShaderInstance DISTORT_SHADER;

    public static ShaderInstance getDistortShader() {
        if (DISTORT_SHADER == null) {
            try {
                DISTORT_SHADER = new ShaderInstance(
                        Minecraft.getInstance().getResourceManager(), EndlessUtils.fa("distort"),
                        DefaultVertexFormat.POSITION_COLOR_TEX
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DISTORT_SHADER;
    }
}
