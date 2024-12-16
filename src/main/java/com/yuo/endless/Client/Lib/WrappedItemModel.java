package com.yuo.endless.Client.Lib;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.yuo.endless.Endless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class WrappedItemModel implements BakedModel {
    protected List<BakedQuad> maskQuad;

    protected BakedQuad haloQuad;

    protected Random RANDOM = new Random();

    protected boolean pulse;

    static ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    static FaceBakery FACE_BAKERY = new FaceBakery();

    BakedModel wrapped;

    LivingEntity entity;

    ClientLevel world;

    ItemTransforms parentState;

    ItemOverrides overrideList;

    public WrappedItemModel(BakedModel wrapped) {
        this.overrideList = new ItemOverrides() {
            public BakedModel getOverrideModel(BakedModel originalModel, ItemStack stack, ClientLevel world, LivingEntity entity) {
                WrappedItemModel.this.entity = entity;
                WrappedItemModel.this.world = (world == null) ? ((entity == null) ? null : (ClientLevel)entity.level) : null;
                if (WrappedItemModel.this.isCosmic())
                    return WrappedItemModel.this.wrapped.getOverrides().resolve(originalModel, stack, world, entity, 0);
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

    public ItemTransforms getTransforms() {
        return this.parentState;
    }

    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    public boolean usesBlockLight() {
        return this.wrapped.isLayered();
    }

    public ItemOverrides getOverrides() {
        return this.overrideList;
    }

    public List<BakedQuad> getQuads(BlockState b, Direction s, Random r) {
        return Collections.emptyList();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.wrapped.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        return this.wrapped.getParticleIcon(data);
    }

    public static <E> void checkArgument(E argument, Predicate<E> predicate) {
        if (predicate.test(argument))
            throw new RuntimeException();
    }

    public static List<BakedQuad> bakeItem(TextureAtlasSprite... sprites) {
        return bakeItem(TransformationMatrix.identity(), sprites);
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous) {
        renderWrapped(s, p, c, light, packed, fabulous, Function.identity());
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous, Function<IVertexBuilder, IVertexBuilder> cons) {
        Minecraft.getInstance().getItemRenderer().renderModelLists(Objects.requireNonNull(this.wrapped.getOverrides().resolve(this, s, world, entity, 0)), s, light, packed, p, cons.apply(ItemRenderer.getFoilBuffer(c, RenderTypeLookup.func_239219_a_(s, fabulous), true, s.hasFoil())));
    }

    static TransformationMatrix create(Vector3f transform, Vector3f rotation, Vector3f scale) {
        return new TransformationMatrix(transform, new Quaternion(rotation.x(), rotation.y(), rotation.z(), true), scale, null);
    }

    static TransformationMatrix create(ItemTransformVec3f i) {
        if (ItemTransformVec3f.DEFAULT.equals(i))
            return TransformationMatrix.identity();
        return create(i.translation, i.rotation, i.scale);
    }

    static ItemTransforms stateFromItemTransforms(TransformType i) {
        if (i == TransformType.NONE)
            return new SimpleModelTransform(ImmutableMap.of());
        ImmutableMap.Builder<TransformType, TransformationMatrix> map = ImmutableMap.builder();
        for (TransformType value : TransformType.values())
            map.put(value, create(i.getTransform(value)));
        return new SimpleModelTransform(map.build());
    }
}
