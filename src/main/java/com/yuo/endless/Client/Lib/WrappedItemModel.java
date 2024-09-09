package com.yuo.endless.Client.Lib;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Endless;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class WrappedItemModel implements IBakedModel {
    protected List<BakedQuad> maskQuad;

    protected BakedQuad haloQuad;

    protected Random RANDOM = new Random();

    protected boolean pulse;

    static ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    static FaceBakery FACE_BAKERY = new FaceBakery();

    IBakedModel wrapped;

    LivingEntity entity;

    ClientWorld world;

    IModelTransform parentState;

    ItemOverrideList overrideList;

    public WrappedItemModel(IBakedModel wrapped) {
        this.overrideList = new ItemOverrideList() {
            public IBakedModel getOverrideModel(IBakedModel originalModel, ItemStack stack, ClientWorld world, LivingEntity entity) {
                WrappedItemModel.this.entity = entity;
                WrappedItemModel.this.world = (world == null) ? ((entity == null) ? null : (ClientWorld)entity.world) : null;
                if (WrappedItemModel.this.isCosmic())
                    return WrappedItemModel.this.wrapped.getOverrides().getOverrideModel(originalModel, stack, world, entity);
                return originalModel;
            }
        };
        this.wrapped = wrapped;
        this.parentState = stateFromItemTransforms(wrapped.getItemCameraTransforms());
    }

    static <T> boolean isNullOrContainsNull(T[] i) {
        if (i != null) {
            for (T t : i) {
                if (t == null)
                    return true;
            }
            return false;
        }
        return true;
    }

    public static List<BakedQuad> bakeItem(TransformationMatrix s, TextureAtlasSprite... sprites) {
        checkArgument(sprites, WrappedItemModel::isNullOrContainsNull);
        List<BakedQuad> quads = new LinkedList<>();
        for (int i = 0; i < sprites.length; i++) {
            TextureAtlasSprite sprite = sprites[i];
            List<BlockPart> unbaked = ITEM_MODEL_GENERATOR.getBlockParts(i, "layer" + i, sprite);
            for (BlockPart element : unbaked) {
                for (Map.Entry<Direction, BlockPartFace> entry : element.mapFaces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(element.positionFrom, element.positionTo, entry.getValue(), sprite, entry.getKey(), SimpleModelTransform.IDENTITY, element.partRotation, element.shade, new ResourceLocation(Endless.MOD_ID, "dynamic")));
                }
            }
        }
        return quads;
    }

    boolean isCosmic() {
        return false;
    }

    public IModelTransform getModelTransform() {
        return this.parentState;
    }

    public boolean isAmbientOcclusion() {
        return this.wrapped.isAmbientOcclusion();
    }

    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    public boolean isSideLit() {
        return this.wrapped.isSideLit();
    }

    public ItemOverrideList getOverrides() {
        return this.overrideList;
    }

    public List<BakedQuad> getQuads(BlockState b, Direction s, Random r) {
        return Collections.emptyList();
    }

    public TextureAtlasSprite getParticleTexture() {
        return this.wrapped.getParticleTexture();
    }

    public TextureAtlasSprite getParticleTexture(IModelData d) {
        return this.wrapped.getParticleTexture();
    }

    public static <E> void checkArgument(E argument, Predicate<E> predicate) {
        if (predicate.test(argument))
            throw new RuntimeException();
    }

    public static List<BakedQuad> bakeItem(TextureAtlasSprite... sprites) {
        return bakeItem(TransformationMatrix.identity(), sprites);
    }

    public void renderWrapped(ItemStack s, MatrixStack p, IRenderTypeBuffer c, int light, int packed, boolean fabulous) {
        renderWrapped(s, p, c, light, packed, fabulous, Function.identity());
    }

    public void renderWrapped(ItemStack s, MatrixStack p, IRenderTypeBuffer c, int light, int packed, boolean fabulous, Function<IVertexBuilder, IVertexBuilder> cons) {
        Minecraft.getInstance().getItemRenderer().renderModel(Objects.requireNonNull(this.wrapped.getOverrides().getOverrideModel(this.wrapped, s, this.world, this.entity)), s, light, packed, p, cons.apply(ItemRenderer.getBuffer(c, RenderTypeLookup.func_239219_a_(s, fabulous), true, s.hasEffect())));
    }

    static TransformationMatrix create(Vector3f transform, Vector3f rotation, Vector3f scale) {
        return new TransformationMatrix(transform, new Quaternion(rotation.getX(), rotation.getY(), rotation.getZ(), true), scale, null);
    }

    static TransformationMatrix create(ItemTransformVec3f i) {
        if (ItemTransformVec3f.DEFAULT.equals(i))
            return TransformationMatrix.identity();
        return create(i.translation, i.rotation, i.scale);
    }

    static IModelTransform stateFromItemTransforms(ItemCameraTransforms i) {
        if (i == ItemCameraTransforms.DEFAULT)
            return new SimpleModelTransform(ImmutableMap.of());
        ImmutableMap.Builder<ItemCameraTransforms.TransformType, TransformationMatrix> map = ImmutableMap.builder();
        for (ItemCameraTransforms.TransformType value : ItemCameraTransforms.TransformType.values())
            map.put(value, create(i.getTransform(value)));
        return new SimpleModelTransform(map.build());
    }
}
