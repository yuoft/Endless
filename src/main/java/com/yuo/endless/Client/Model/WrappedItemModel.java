package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import committee.nova.mods.avaritia.api.client.model.PerspectiveModel;
import committee.nova.mods.avaritia.api.client.model.PerspectiveModelState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class WrappedItemModel implements PerspectiveModel {
    ModelState parentState;

    BakedModel wrapped;

    LivingEntity entity;

    ClientLevel world;

    List<BakedQuad> maskQuad;

    static ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    static FaceBakery FACE_BAKERY = new FaceBakery();

    ItemOverrides overrideList;

    public WrappedItemModel(BakedModel wrapped) {
        this.overrideList = new ItemOverrides() {
            public BakedModel m_173464_(BakedModel originalModel, ItemStack stack, ClientLevel world, LivingEntity entity, int seed) {
                WrappedItemModel.this.entity = entity;
                WrappedItemModel.this.world = (world == null) ? ((entity == null) ? null : (ClientLevel)entity.level()) : null;
                if (WrappedItemModel.this.isCosmic())
                    return WrappedItemModel.this.wrapped.getOverrides().resolve(originalModel, stack, world, entity, seed);
                return originalModel;
            }
        };
        this.wrapped = wrapped;
        this.parentState = stateFromItemTransform(wrapped.getTransforms());
    }


    public static ModelState stateFromItemTransform(ItemTransforms i) {
        if (i == ItemTransforms.NO_TRANSFORMS)
            return (ModelState)new PerspectiveModelState((Map)ImmutableMap.of());
        ImmutableMap.Builder<ItemDisplayContext, Transformation> map = ImmutableMap.builder();
        for (ItemDisplayContext value : ItemDisplayContext.values())
            map.put(value, create(i.getTransform(value)));
        return (ModelState)new PerspectiveModelState((Map)map.build());
    }


    static Transformation create(ItemTransform t) {
        if (ItemTransform.NO_TRANSFORM.equals(t))
            return Transformation.identity();
        return create(t.translation, t.rotation, t.scale);
    }


    static Transformation create(Vector3f t, Vector3f r, Vector3f s) {
        return new Transformation(t, (new Quaternionf())

                .rotationXYZ((float)(r.x() * 0.017453292519943D), (float)(r.y() * 0.017453292519943D), (float)(r.z() * 0.017453292519943D)), s, null);
    }
    public static <T> boolean isNullOrContainsNull(T[] input) {
        if (input != null) {
            for (T t : input) {
                if (t == null)
                    return true;
            }
            return false;
        }
        return true;
    }

    static List<BakedQuad> bakeItem(Transformation s, TextureAtlasSprite... sprites) {
        checkArgument(sprites, WrappedItemModel::isNullOrContainsNull);
        LinkedList<BakedQuad> quads = new LinkedList();
        for (int i = 0; i < sprites.length; i++) {
            TextureAtlasSprite sprite = sprites[i];
            List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite.contents);
            Iterator<BlockElement> var6 = unbaked.iterator();
            while (var6.hasNext()) {
                BlockElement element = var6.next();
                Iterator<Map.Entry<Direction, BlockElementFace>> var8 = element.faces.entrySet().iterator();
                while (var8.hasNext()) {
                    Map.Entry<Direction, BlockElementFace> entry = var8.next();
                    quads.add(FACE_BAKERY.bakeQuad(element.from, element.to, entry.getValue(), sprite, entry.getKey(), new PerspectiveModelState(ImmutableMap.of()), element.rotation, element.shade, ResourceLocation.fromNamespaceAndPath("endless", "dynamic")));
                }
            }
        }
        return quads;
    }

    public PerspectiveModelState getModelTransform() {
        return (PerspectiveModelState)this.parentState;
    }

    boolean isCosmic() {
        return false;
    }

    static List<BakedQuad> bakeItem(TextureAtlasSprite... s) {
        return bakeItem(Transformation.identity(), s);
    }

    public static <E> void checkArgument(E argument, Predicate<E> predicate) {
        if (predicate.test(argument))
            throw new RuntimeException("");
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

    public ItemOverrides getOverrides() {
        return this.overrideList;
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous) {
        renderWrapped(s, p, c, light, packed, fabulous, Function.identity());
    }

    public void renderWrapped(ItemStack s, PoseStack p, MultiBufferSource c, int light, int packed, boolean fabulous, Function<VertexConsumer, VertexConsumer> v) {
        BakedModel model = this.wrapped.getOverrides().resolve(this.wrapped, s, this.world, this.entity, 0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        RenderType rType = ItemBlockRenderTypes.getRenderType(s, fabulous);
        VertexConsumer builder = ItemRenderer.getFoilBuffer(c, rType, true, s.hasFoil());
        itemRenderer.renderModelLists(model, s, light, packed, p, v.apply(builder));
    }
}