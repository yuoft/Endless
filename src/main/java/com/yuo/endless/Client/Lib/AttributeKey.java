package com.yuo.endless.Client.Lib;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class AttributeKey<T> {
    String name;

    IntFunction<T> factory;

    int attributeKeyIndex;

    int operationIndex;

    public AttributeKey(String name, IntFunction<T> factory) {
        this.name = name;
        this.factory = factory;
        this.attributeKeyIndex = AttributeKeyRegistry.registerAttributeKey(this);
        this.operationIndex = IVertexOperation.registerOperation();
    }

    public T newArray(int length) {
        return this.factory.apply(length);
    }

    public static class AttributeKeyRegistry {
        static Map<String, AttributeKey<?>> nameMap = new HashMap<>();

        static List<AttributeKey<?>> attributeKeys = new ArrayList<>();

        static int registerAttributeKey(AttributeKey<?> attr) {
            if (nameMap.containsKey(attr.name))
                throw new IllegalArgumentException();
            nameMap.put(attr.name, attr);
            attributeKeys.add(attr);
            return attributeKeys.size() - 1;
        }

        public static <T> AttributeKey<T> getAttributeKey(int index) {
            return MathHelper.unsafeCast(attributeKeys.get(index));
        }
    }
}
