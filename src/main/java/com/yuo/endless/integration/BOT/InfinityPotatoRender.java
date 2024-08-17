package com.yuo.endless.integration.BOT;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.core.handler.ContributorList;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.regex.Pattern;

public class InfinityPotatoRender extends TileEntityRenderer<InfinityPotatoTile> {
    public static final String DEFAULT = "default";
    public static final String HALLOWEEN = "halloween";
    private static final Pattern ESCAPED = Pattern.compile("[^a-z0-9/._-]");
    private static final ResourceLocation TE = new ResourceLocation(Endless.MOD_ID, "infinity_potato/");
    public InfinityPotatoRender(TileEntityRendererDispatcher manager) {
        super(manager);
    }
    private static boolean matches(String name, String match) {
        return name.equals(match) || name.startsWith(match + " ");
    }

    private static String removeFromFront(String name, String match) {
        return name.substring(match.length()).trim();
    }

    public static IBakedModel getModelFromDisplayName(ITextComponent displayName) {
        return getModel(stripShaderName(displayName.getString().trim().toLowerCase(Locale.ROOT)).getSecond());
    }

    private static Pair<ShaderHelper.BotaniaShader, String> stripShaderName(String name) {
        if (matches(name, "gaia")) {
            return Pair.of(ShaderHelper.BotaniaShader.DOPPLEGANGER, removeFromFront(name, "gaia"));
        } else if (matches(name, "hot")) {
            return Pair.of(ShaderHelper.BotaniaShader.HALO, removeFromFront(name, "hot"));
        } else if (matches(name, "magic")) {
            return Pair.of(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, removeFromFront(name, "magic"));
        } else if (matches(name, "gold")) {
            return Pair.of(ShaderHelper.BotaniaShader.GOLD, removeFromFront(name, "gold"));
        } else if (matches(name, "snoop")) {
            return Pair.of(ShaderHelper.BotaniaShader.TERRA_PLATE, removeFromFront(name, "snoop"));
        } else {
            return Pair.of(null, name);
        }
    }

    private static IBakedModel getModel(String name) {
        ModelManager mm = Minecraft.getInstance().getModelManager();
        ResourceLocation location = taterLocation(name);
        IBakedModel model = mm.getModel(location);
        if (model == mm.getMissingModel()) {
            if (ClientProxy.dootDoot) {
                return mm.getModel(taterLocation(HALLOWEEN));
            } else {
                return mm.getModel(taterLocation(DEFAULT));
            }
        }
        return model;
    }

    private static ResourceLocation taterLocation(String name) {
        return new ResourceLocation(Endless.MOD_ID, "textures/block/infinity_potato");
    }

    private static String normalizeName(String name) {
        return ESCAPED.matcher(name).replaceAll("_");
    }

    private static RenderType getRenderLayer(@Nullable ShaderHelper.BotaniaShader shader) {
        RenderType base = Atlases.getTranslucentCullBlockType();
        return shader == null || !ShaderHelper.useShaders() ? base : new ShaderWrappedRenderLayer(shader, null, base);
    }

    @Override
    public void render(@Nonnull InfinityPotatoTile potato, float partialTicks, MatrixStack ms, @Nonnull IRenderTypeBuffer buffers, int light, int overlay) {
        ms.push();

        String name = potato.name.getString().toLowerCase(Locale.ROOT).trim();
        Pair<ShaderHelper.BotaniaShader, String> shaderStrippedName = stripShaderName(name);
        ShaderHelper.BotaniaShader shader = shaderStrippedName.getFirst();
        name = shaderStrippedName.getSecond();
        RenderType layer = getRenderLayer(shader);
//        IBakedModel model = getModel(name);
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(EndlessBlocks.infinityPotato.get().getDefaultState());

        ms.translate(0.5F, 0F, 0.5F);
        Direction potatoFacing = potato.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
        float rotY = 0;
        switch (potatoFacing) {
            default:
            case SOUTH:
                rotY = 180F;
                break;
            case NORTH:
                break;
            case EAST:
                rotY = 90F;
                break;
            case WEST:
                rotY = 270F;
                break;
        }
        ms.rotate(Vector3f.YN.rotationDegrees(rotY));

        float jump = potato.jumpTicks;
        if (jump > 0) {
            jump -= partialTicks;
        }

        float up = (float) Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
        float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;
        float wiggle = (float) Math.sin(jump / 10 * Math.PI) * 0.05F;

        ms.translate(wiggle, up, 0F);
        ms.rotate(Vector3f.ZP.rotationDegrees(rotZ));

        boolean render = !(name.equals("mami") || name.equals("soaryn") || name.equals("eloraam") && jump != 0);
        if (render) {
            ms.push();
            ms.translate(-0.5F, 0, -0.5F);
            IVertexBuilder buffer = buffers.getBuffer(layer);

            renderModel(ms, buffer, light, overlay, model);
            ms.pop();
        }

        ms.translate(0F, 1.5F, 0F);
        ms.push();
        ms.rotate(Vector3f.ZP.rotationDegrees(180F));
        renderItems(potato, potatoFacing, name, partialTicks, ms, buffers, light, overlay);

        ms.push();
        MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, potato.name, partialTicks, ms, buffers, light, overlay));
        ms.pop();
        ms.pop();

        ms.rotate(Vector3f.ZP.rotationDegrees(-rotZ));
        ms.rotate(Vector3f.YN.rotationDegrees(-rotY));

        renderName(potato, name, ms, buffers, light);
        ms.pop();
    }

    private void renderName(InfinityPotatoTile potato, String name, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
        Minecraft mc = Minecraft.getInstance();
        RayTraceResult pos = mc.objectMouseOver;
        if (!name.isEmpty() && pos != null && pos.getType() == RayTraceResult.Type.BLOCK
                && potato.getPos().equals(((BlockRayTraceResult) pos).getPos())) {
            ms.push();
            ms.translate(0F, -0.6F, 0F);
            ms.rotate(mc.getRenderManager().getCameraOrientation());
            float f1 = 0.016666668F * 1.6F;
            ms.scale(-f1, -f1, f1);
            int halfWidth = mc.fontRenderer.getStringWidth(potato.name.getString()) / 2;

            float opacity = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
            int opacityRGB = (int) (opacity * 255.0F) << 24;
            mc.fontRenderer.func_243247_a(potato.name, -halfWidth, 0, 0x20FFFFFF, false, ms.getLast().getMatrix(), buffers, true, opacityRGB, light);
            mc.fontRenderer.func_243247_a(potato.name, -halfWidth, 0, 0xFFFFFFFF, false, ms.getLast().getMatrix(), buffers, false, 0, light);
            if (name.equals("pahimar") || name.equals("soaryn")) {
                ms.translate(0F, 14F, 0F);
                String str = name.equals("pahimar") ? "[WIP]" : "(soon)";
                halfWidth = mc.fontRenderer.getStringWidth(str) / 2;

                mc.fontRenderer.renderString(str, -halfWidth, 0, 0x20FFFFFF, false, ms.getLast().getMatrix(), buffers, true, opacityRGB, light);
                mc.fontRenderer.renderString(str, -halfWidth, 0, 0xFFFFFFFF, false, ms.getLast().getMatrix(), buffers, true, 0, light);
            }

            ms.pop();
        }
    }

    private void renderItems(InfinityPotatoTile potato, Direction facing, String name, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        ms.push();
        ms.rotate(Vector3f.ZP.rotationDegrees(180F));
        ms.translate(0F, -1F, 0F);
        float s = 1F / 3.5F;
        ms.scale(s, s, s);

        for (int i = 0; i < potato.inventorySize(); i++) {
            ItemStack stack = potato.getItemHandler().getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            ms.push();
            Direction side = Direction.values()[i];
            if (side.getAxis() != Axis.Y) {
                float sideAngle = side.getHorizontalAngle() - facing.getHorizontalAngle();
                side = Direction.fromAngle(sideAngle);
            }

            boolean block = stack.getItem() instanceof BlockItem;
            boolean mySon = stack.getItem() instanceof ItemBlockPotato;

            switch (side) {
                case UP:
                    if (mySon) {
                        ms.translate(0F, 2.0F, 1.0F);
                    } else if (block) {
                        ms.translate(0F, 0.6F, 0.5F);
                    }
                    ms.translate(0F, 0.6F, -0.4F);
                    break;
                case DOWN:
                    ms.translate(0, -2.3F, -1.35F);
                    if (mySon) {
                        ms.translate(0, 1.5F, 0.8F);
                    } else if (block) {
                        ms.translate(0, 1, 0.6F);
                    }
                    break;
                case NORTH:
                    ms.translate(0, -1.2F, 0.48F);
                    if (mySon) {
                        ms.translate(0, 2.6, 0.68F);
                    } else if (block) {
                        ms.translate(0, 1, 0.6F);
                    }
                    break;
                case SOUTH:
                    ms.translate(0, -1.2F, -1.35F);
                    if (mySon) {
                        ms.translate(0, 3.4F, 1.2F);
                    } else if (block) {
                        ms.translate(0, 1.0F, 0.5F);
                    }
                    break;
                case EAST:
                    if (mySon) {
                        ms.translate(-0.8F, 1.6F, -0.2F);
                    } else if (block) {
                        ms.translate(-0.2F, 0.8F, -0.4F);
                    } else {
                        ms.rotate(Vector3f.YP.rotationDegrees(-90F));
                    }
                    ms.translate(-0.8F, -1.0F, 0.48F);
                    break;
                case WEST:
                    if (mySon) {
                        ms.translate(2.5F, 1.6F, 1.5F);
                    } else if (block) {
                        ms.translate(1.92F, 0.8F, 1.3F);
                    } else {
                        ms.rotate(Vector3f.YP.rotationDegrees(-90F));
                    }
                    ms.translate(-0.8F, -1.0F, -1.34F);
                    break;
            }

            if (mySon) {
                ms.scale(1.1F, 1.1F, 1.1F);
            } else if (block) {
                ms.scale(0.5F, 0.5F, 0.5F);
            }
            if (block && side == Direction.NORTH) {
                ms.rotate(Vector3f.YP.rotationDegrees(180F));
            }
            renderItem(ms, buffers, light, overlay, stack);
            ms.pop();
        }
        ms.pop();

        ms.push();
        if (!name.isEmpty()) {
            ContributorList.firstStart();

            float scale = 1F / 4F;
            ms.translate(0F, 1F, 0F);
            ms.scale(scale, scale, scale);
            switch (name) {
                case "phi":
                case "vazkii":
                    ms.push();
                    ms.translate(-0.15, 0.1, 0.4);
                    ms.rotate(Vector3f.YP.rotationDegrees(90F));
                    ms.rotate(new Vector3f(1, 0, 1).rotationDegrees(20));
                    renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.phiFlowerModel);
                    ms.pop();

                    if (name.equals("vazkii")) {
                        ms.scale(1.25F, 1.25F, 1.25F);
                        ms.rotate(Vector3f.XP.rotationDegrees(180F));
                        ms.rotate(Vector3f.YP.rotationDegrees(-90F));
                        ms.translate(0.2, -1.25, 0);
                        renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.nerfBatModel);
                    }
                    break;
                case "haighyorkie":
                    ms.scale(1.25F, 1.25F, 1.25F);
                    ms.rotate(Vector3f.ZP.rotationDegrees(180F));
                    ms.rotate(Vector3f.YP.rotationDegrees(-90F));
                    ms.translate(-0.5F, -1.2F, -0.075F);
                    renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.goldfishModel);
                    break;
                case "martysgames":
                case "marty":
                    ms.scale(0.7F, 0.7F, 0.7F);
                    ms.rotate(Vector3f.ZP.rotationDegrees(180F));
                    ms.translate(-0.3F, -2.7F, -1.2F);
                    ms.rotate(Vector3f.ZP.rotationDegrees(15F));
                    renderItem(ms, buffers, light, overlay, new ItemStack(ModItems.infiniteFruit, 1).setDisplayName(new StringTextComponent("das boot")));
                    break;
                case "jibril":
                    ms.scale(1.5F, 1.5F, 1.5F);
                    ms.translate(0F, 0.8F, 0F);
                    ItemFlightTiara.renderHalo(null, null, ms, buffers, partialTicks);
                    break;
                case "kingdaddydmac":
                    ms.scale(0.5F, 0.5F, 0.5F);
                    ms.rotate(Vector3f.ZP.rotationDegrees(180));
                    ms.rotate(Vector3f.YP.rotationDegrees(90));
                    ms.push();
                    ms.translate(0F, -2.5F, 0.65F);
                    ItemStack ring = new ItemStack(ModItems.manaRing);
                    renderItem(ms, buffers, light, overlay, ring);
                    ms.translate(0F, 0F, -4F);
                    renderItem(ms, buffers, light, overlay, ring);
                    ms.pop();

                    ms.translate(1.5, -4, -2.5);
                    renderBlock(ms, buffers, light, overlay, Blocks.CAKE);
                    break;
                default:
                    ItemStack icon = ContributorList.getFlower(name);
                    if (!icon.isEmpty()) {
                        ms.rotate(Vector3f.XP.rotationDegrees(180));
                        ms.rotate(Vector3f.YP.rotationDegrees(180));
                        ms.translate(0, -0.75, -0.5);
                        Minecraft.getInstance().getItemRenderer().renderItem(icon, ItemCameraTransforms.TransformType.HEAD, light, overlay, ms, buffers);
                    }
                    break;
            }
        }
        ms.pop();
    }

    private void renderModel(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, IBakedModel model) {
        renderModel(ms, buffers.getBuffer(Atlases.getTranslucentCullBlockType()), light, overlay, model);
    }

    private void renderModel(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, IBakedModel model) {
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, overlay);
    }

    private void renderItem(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, ItemStack stack) {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.HEAD, light, overlay, ms, buffers);
    }

    private void renderBlock(MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, Block block) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(block.getDefaultState(), ms, buffers, light, overlay);
    }

}
