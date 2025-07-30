package com.yuo.endless.Client.Lib;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * SneakyUtils
 *
 * @author cnlimiter
 * @version 1.0
 */
public class SneakyUtils {
    private static final BinaryOperator<Object> FIRST = (a, b) -> a;
    private static final BinaryOperator<Object> LAST = (a, b) -> b;

    /**
     * A BinaryOperator which always resolves to the left-hand element.
     *
     * @return The BinaryOperator.
     */
    public static <T> BinaryOperator<T> first() {
        return unsafeCast(FIRST);
    }

    /**
     * A BinaryOperator which always resolves to the right-hand element.
     *
     * @return The BinaryOperator.
     */
    public static <T> BinaryOperator<T> last() {
        return unsafeCast(LAST);
    }

    @Nullable
    @Contract ("null->null;!null->!null")
    @SuppressWarnings ("unchecked")
    public static <T> T unsafeCast(@Nullable Object object) {
        return (T) object;
    }
}
