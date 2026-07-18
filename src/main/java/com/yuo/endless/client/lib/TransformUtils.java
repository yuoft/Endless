package com.yuo.endless.client.lib;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class TransformUtils {
    private static final Transformation flipX = new Transformation((Vector3f)null, (Quaternionf)null, new Vector3f(-1.0F, 1.0F, 1.0F), (Quaternionf)null);
    public static final PerspectiveModelState IDENTITY;
    public static final PerspectiveModelState DEFAULT_BLOCK;
    public static final PerspectiveModelState DEFAULT_ITEM;
    public static final PerspectiveModelState DEFAULT_TOOL;
    public static final PerspectiveModelState DEFAULT_BOW;
    public static final PerspectiveModelState DEFAULT_HANDHELD_ROD;

    public TransformUtils() {
    }

    public static Transformation create(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
        return create(new Vector3f(tx / 16.0F, ty / 16.0F, tz / 16.0F), new Vector3f(rx, ry, rz), new Vector3f(s, s, s));
    }

    public static Transformation create(Vector3 transform, Vector3 rotation, Vector3 scale) {
        return create(transform.vector3f(), rotation.vector3f(), scale.vector3f());
    }

    public static Transformation create(Vector3f transform, Vector3f rotation, Vector3f scale) {
        return new Transformation(transform, (new Quaternionf()).rotationXYZ((float)((double)rotation.x() * 0.017453292519943), (float)((double)rotation.y() * 0.017453292519943), (float)((double)rotation.z() * 0.017453292519943)), scale, (Quaternionf)null);
    }

    public static Transformation create(ItemTransform transform) {
        return ItemTransform.NO_TRANSFORM.equals(transform) ? Transformation.identity() : create(transform.translation, transform.rotation, transform.scale);
    }

    public static Transformation flipLeft(Transformation transform) {
        return flipX.compose(transform).compose(flipX);
    }

    public static ModelState stateFromItemTransforms(ItemTransforms itemTransforms) {
        if (itemTransforms == ItemTransforms.NO_TRANSFORMS) {
            return IDENTITY;
        } else {
            ImmutableMap.Builder<ItemDisplayContext, Transformation> map = ImmutableMap.builder();
            ItemDisplayContext[] var2 = ItemDisplayContext.values();

            for (ItemDisplayContext value : var2) {
                map.put(value, create(itemTransforms.getTransform(value)));
            }

            return new PerspectiveModelState(map.build());
        }
    }

    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    public static void applyLeftyFlip(PoseStack pStack) {
        if (!pStack.clear()) {
            Matrix4f tMat = pStack.last().pose();
            Matrix3f nMat = pStack.last().normal();
            tMat.mulLocal(flipX.getMatrix());
            tMat.mul(flipX.getMatrix());
            nMat.mulLocal(flipX.getNormalMatrix());
            nMat.mul(flipX.getNormalMatrix());
            pStack.last().pose().mul(tMat);
            pStack.last().normal().mul(nMat);
        }

    }

    static {
        IDENTITY = PerspectiveModelState.IDENTITY;
        Map<ItemDisplayContext, Transformation> map = new HashMap<>();
        Transformation thirdPerson = create(0.0F, 2.5F, 0.0F, 75.0F, 45.0F, 0.0F, 0.375F);
        map.put(ItemDisplayContext.GUI, create(0.0F, 0.0F, 0.0F, 30.0F, 225.0F, 0.0F, 0.625F));
        map.put(ItemDisplayContext.GROUND, create(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.25F));
        map.put(ItemDisplayContext.FIXED, create(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flipLeft(thirdPerson));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(0.0F, 0.0F, 0.0F, 0.0F, 45.0F, 0.0F, 0.4F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(0.0F, 0.0F, 0.0F, 0.0F, 225.0F, 0.0F, 0.4F));
        DEFAULT_BLOCK = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        thirdPerson = create(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
        Transformation firstPerson = create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.HEAD, create(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flipLeft(thirdPerson));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, firstPerson);
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, flipLeft(firstPerson));
        DEFAULT_ITEM = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.FIXED, create(0.0F, 0.0F, 0.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(0.0F, 4.0F, 0.5F, 0.0F, -90.0F, 55.0F, 0.85F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(0.0F, 4.0F, 0.5F, 0.0F, 90.0F, -55.0F, 0.85F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F));
        DEFAULT_TOOL = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.FIXED, create(0.0F, 0.0F, 0.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(-1.0F, -2.0F, 2.5F, -80.0F, 260.0F, -40.0F, 0.9F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(-1.0F, -2.0F, 2.5F, -80.0F, -280.0F, 40.0F, 0.9F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F));
        DEFAULT_BOW = new PerspectiveModelState(ImmutableMap.copyOf(map));
        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, create(0.0F, 4.0F, 2.5F, 0.0F, 90.0F, 55.0F, 0.85F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, create(0.0F, 4.0F, 2.5F, 0.0F, -90.0F, -55.0F, 0.85F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, create(0.0F, 1.6F, 0.8F, 0.0F, 90.0F, 25.0F, 0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, create(0.0F, 1.6F, 0.8F, 0.0F, -90.0F, -25.0F, 0.68F));
        DEFAULT_HANDHELD_ROD = new PerspectiveModelState(ImmutableMap.copyOf(map));
    }
}
