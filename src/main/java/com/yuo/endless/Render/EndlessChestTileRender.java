package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Blocks.AbsEndlessChest;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Event.ClientEventHandler;
import com.yuo.endless.Tiles.AbsEndlessChestTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndlessChestTileRender<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {
    private final ModelRenderer singleLid;
    private final ModelRenderer singleBottom;
    private final ModelRenderer singleLatch;

    public EndlessChestTileRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.singleBottom = new ModelRenderer(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelRenderer(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.rotationPointY = 9.0F;
        this.singleLid.rotationPointZ = 1.0F;
        this.singleLatch = new ModelRenderer(64, 64, 0, 0);
        this.singleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.singleLatch.rotationPointY = 8.0F;
    }

    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        AbsEndlessChestTile chestTile = (AbsEndlessChestTile) tileEntityIn;
        World world = tileEntityIn.getWorld();
        boolean flag = world != null;
        BlockState blockstate = flag ? tileEntityIn.getBlockState() : chestTile.getBlock().getDefaultState().with(AbsEndlessChest.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();

        EndlessChestType chestType = EndlessChestType.NORMAL;
        EndlessChestType actualType = AbsEndlessChest.getTypeFromBlock(block);

        if (actualType != null) {
            chestType = actualType;
        }

        if (block instanceof AbsEndlessChest) {
            AbsEndlessChest endlessChest = (AbsEndlessChest) block;
            matrixStackIn.push();
            float f = blockstate.get(AbsEndlessChest.FACING).getHorizontalAngle();
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
            matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

            TileEntityMerger.ICallbackWrapper<? extends AbsEndlessChestTile> icallbackwrapper;
            if (flag) {
                icallbackwrapper = endlessChest.combine(blockstate, world, tileEntityIn.getPos(), true);
            } else {
                icallbackwrapper = TileEntityMerger.ICallback::func_225537_b_;
            }

            float f1 = icallbackwrapper.apply(AbsEndlessChest.getLidRotationCallback(chestTile)).get(partialTicks);
            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            int i = icallbackwrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);

            RenderMaterial renderMaterial = new RenderMaterial(Atlases.CHEST_ATLAS, ClientEventHandler.chooseChestTexture(chestType));
            IVertexBuilder buffer = renderMaterial.getBuffer(bufferIn, RenderType::getEntityCutout);
            this.renderModels(matrixStackIn, buffer, this.singleLid, this.singleLatch, this.singleBottom, f1, i, combinedOverlayIn);

            matrixStackIn.pop();
        }
    }

    private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer chestLid, ModelRenderer chestLatch, ModelRenderer chestBottom, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        chestLid.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));
        chestLatch.rotateAngleX = chestLid.rotateAngleX;
        chestLid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestLatch.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestBottom.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

}