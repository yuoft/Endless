package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.yuo.endless.Endless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.SimpleModelState;
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

    ModelState parentState;

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
        this.parentState = stateFromItemTransforms(wrapped.getTransforms());
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

    public static List<BakedQuad> bakeItem(Transformation s, TextureAtlasSprite... sprites) {
        checkArgument(sprites, WrappedItemModel::isNullOrContainsNull);
        List<BakedQuad> quads = new LinkedList<>();
        for (int i = 0; i < sprites.length; i++) {
            TextureAtlasSprite sprite = sprites[i];
            List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite);
            for (BlockElement element : unbaked) {
                for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(element.from, element.to, entry.getValue(), sprite, entry.getKey(), SimpleModelState.IDENTITY, element.rotation, element.shade, new ResourceLocation(Endless.MOD_ID, "dynamic")));
                }
            }
        }
        return quads;
    }

    boolean isCosmic() {
        return false;
    }

    @NotNull
    public ModelState getModelTransform() {
        return this.parentState;
    }

    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }

    public @NotNull ItemOverrides getOverrides() {
        return this.overrideList;
    }

    public @NotNull List<BakedQuad> getQuads(BlockState b, Direction s, @NotNull Random r) {
        return Collections.emptyList();
    }

    @Override
    @Deprecated
    public @NotNull TextureAtlasSprite getParticleIcon() {
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
        return bakeItem(Transformation.identity(), sprites);
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous) {
        renderWrapped(s, p, c, light, packed, fabulous, Function.identity());
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous, Function<VertexConsumer, VertexConsumer> cons) {
        BakedModel model = this.wrapped.getOverrides().resolve(this.wrapped, s, this.world, this.entity, 0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderType rType = ItemBlockRenderTypes.getRenderType(s, fabulous);
        VertexConsumer builder = ItemRenderer.getFoilBuffer(c, rType, true, s.hasFoil());
        if (model != null) {
            itemRenderer.renderModelLists(model, s, light, packed, p, cons.apply(builder));
        }
    }

    static Transformation create(Vector3f transform, Vector3f rotation, Vector3f scale) {
        return new Transformation(transform, new Quaternion(rotation.x(), rotation.y(), rotation.z(), true), scale, null);
    }

    @Deprecated
    static Transformation create(ItemTransform i) {
        if (ItemTransform.NO_TRANSFORM.equals(i))
            return Transformation.identity();
        return create(i.translation, i.rotation, i.scale);
    }

    static ModelState stateFromItemTransforms(ItemTransforms i) {
        if (i == ItemTransforms.NO_TRANSFORMS)
            return new SimpleModelState(ImmutableMap.of());
        ImmutableMap.Builder<TransformType, Transformation> map = ImmutableMap.builder();
        for (TransformType value : TransformType.values())
            map.put(value, create(i.getTransform(value)));
        return new SimpleModelState(map.build());
    }
}
