package com.yuo.endless.client.lib;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * Created by covers1624 on 3/27/2016.
 */
public class ArrayUtils {

    /**
     * Checks if the array is all null.
     *
     * @param array The array to check.
     * @param <T>   What we are dealing with.
     * @return True if the array only contains nulls.
     */
    public static <T> boolean isEmpty(T[] array) {
        for (T value : array) {
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts elements in the array that conform to the Function check.
     *
     * @param array The array to check.
     * @param check The Function to apply to each element.
     * @param <T>   What we are dealing with.
     * @return The count.
     */
    public static <T> int count(T[] array, Function<T, Boolean> check) {
        int counter = 0;
        for (T value : array) {
            if (check.apply(value)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Basically a wrapper for System.arraycopy with support for CCL Copyable's
     *
     * @param src     The source array.
     * @param srcPos  Starting position in the source array.
     * @param dst     The destination array.
     * @param destPos Starting position in the destination array.
     * @param length  The number of elements to copy.
     */
    public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
        System.arraycopy(src, srcPos, dst, destPos, length);
        if (dst instanceof Copyable[]) {
            Object[] oa = (Object[]) dst;
            Copyable<Object>[] c = (Copyable[]) dst;
            for (int i = destPos; i < destPos + length; i++) {
                if (c[i] != null) {
                    oa[i] = c[i].copy();
                }
            }
        }
    }

    /**
     * Create a new array using the provided array as a template for the type and with the provided length.
     *
     * @param array  The type template.
     * @param length The new array's length.
     * @param <T>    The type.
     * @return The new array.
     */
    @SuppressWarnings ("unchecked")
    public static <T> T[] createNewArray(T[] array, int length) {
        Class<? extends T[]> newType = (Class<? extends T[]>) array.getClass();
        T[] copy;
        //noinspection RedundantCast
        if (newType.equals(Object[].class)) {
            copy = (T[]) new Object[length];
        } else {
            copy = (T[]) newArray(newType.getComponentType(), length);
        }
        return copy;
    }

    /**
     * Creates a new array form its component class.
     *
     * @param arrayClass The component class.
     * @param length     The length.
     * @param <T>        The thing.
     * @return The new array.
     */
    @SuppressWarnings ("unchecked")
    public static <T> T[] newArray(Class<T> arrayClass, int length) {
        return (T[]) Array.newInstance(arrayClass, length);
    }

    /**
     * Checks if an array contains any of the specified element.
     *
     * @param input   The input
     * @param element The thing to test against.
     * @param <T>     The thing.
     * @return If the element exists at all.
     */
    public static <T> boolean contains(T[] input, T element) {
        for (T test : input) {
            if (Objects.equals(test, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the inverse of an array.
     * If the input array does not contain an element from the allElements array,
     * then it is added to the output.
     *
     * @param input       The input.
     * @param allElements All possible values.
     * @param <T>         The thing.
     * @return The inverse array.
     */
    public static <T> T[] inverse(T[] input, T[] allElements) {
        List<T> list = new LinkedList<>();
        for (T e : allElements) {
            if (!contains(input, e)) {
                list.add(e);
            }
        }

        return list.toArray(createNewArray(input, list.size()));
    }

}
