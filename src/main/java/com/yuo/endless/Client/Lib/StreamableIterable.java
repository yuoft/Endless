package com.yuo.endless.Client.Lib;


import com.google.common.collect.*;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

import static com.yuo.endless.Client.Lib.SneakyUtils.unsafeCast;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * StreamableIterable
 *
 * @author cnlimiter
 * @version 1.0
 */
public interface StreamableIterable<T> extends Iterable<T> {

    /**
     * Static empty instance.
     * You want {@link #empty()}.
     */
    StreamableIterable<?> EMPTY = of(Collections.emptyList());

    /**
     * Returns an empty {@link StreamableIterable}.
     *
     * @return The empty {@link StreamableIterable}
     */
    static <T> StreamableIterable<T> empty() {
        return unsafeCast(EMPTY);
    }

    /**
     * Constructs a {@link StreamableIterable} wrapper from the
     * given {@link Iterable}.
     *
     * @param itr The {@link Iterable} to wrap.
     * @return The {@link StreamableIterable}
     */
    @SuppressWarnings ("unchecked")
    static <T> StreamableIterable<T> of(Iterable<? extends T> itr) {
        if (itr instanceof StreamableIterable) return (StreamableIterable<T>) itr;

        return ((Iterable<T>) itr)::iterator;
    }

    /**
     * Returns an empty {@link StreamableIterable}.
     *
     * @return The empty {@link StreamableIterable}
     */
    static <T> StreamableIterable<T> of() {
        return unsafeCast(EMPTY);
    }

    /**
     * Creates a {@link StreamableIterable} for single input.
     *
     * @param thing The thing.
     * @return the {@link StreamableIterable}.
     */
    static <T> StreamableIterable<T> of(T thing) {
        return of(singletonList(thing));
    }

    /**
     * Creates an {@link StreamableIterable} for an {@link Optional} value.
     * <p>
     * If the value provided is not present, this method will
     * return {@link #empty()}, otherwise it will return a new {@link StreamableIterable}
     * containing only the value provided.
     *
     * @param optional The thing.
     * @return The {@link StreamableIterable}.
     */
    static <T> StreamableIterable<T> of(Optional<? extends T> optional) {
        return optional.map(StreamableIterable::<T>of).orElse(empty());
    }

    /**
     * Creates an {@link StreamableIterable} from a variable list of inputs.
     *
     * @param things The things.
     * @return the {@link StreamableIterable}.
     */
    @SafeVarargs
    static <T> StreamableIterable<T> of(T... things) {
        return of(asList(things));
    }

    /**
     * Transform the elements in this {@link StreamableIterable}.
     *
     * @param func The {@link Function} to transform with.
     * @return A wrapped {@link StreamableIterable} with the mapped elements.
     */
    default <R> StreamableIterable<R> map(Function<? super T, ? extends R> func) {
        return () -> new Iterator<R>() {
            private final Iterator<T> itr = iterator();

            @Override
            public boolean hasNext() {
                return itr.hasNext();
            }

            @Override
            public R next() {
                return func.apply(itr.next());
            }

            @Override
            public void remove() {
                itr.remove();
            }
        };
    }

    /**
     * Folds each element of this {@link StreamableIterable} into the previous.
     *
     * @param identity    The starting element.
     * @param accumulator The function to fold elements with.
     * @return The result.
     * @deprecated Remains for ABI compat.
     */
    @Nullable
    @Contract ("!null,_ -> !null")
    @Deprecated
    @ScheduledForRemoval (inVersion = "0.5.0")
    default T fold(@Nullable T identity, BinaryOperator<@Nullable T> accumulator) {
        return fold(identity, (BiFunction<@Nullable T, T, T>) accumulator);
    }

    /**
     * Folds each element of this {@link StreamableIterable} into the previous.
     *
     * @param identity    The starting element.
     * @param accumulator The function to fold elements with.
     * @return The result.
     */
    @Nullable
    @Contract ("!null,_ -> !null")
    default <U> U fold(@Nullable U identity, BiFunction<? super @Nullable U, ? super T, ? extends U> accumulator) {
        U ret = identity;
        for (T t : this) {
            ret = accumulator.apply(ret, t);
        }
        return ret;
    }

    /**
     * Count the number of elements in this {@link StreamableIterable}.
     *
     * @return The number of elements.
     */
    default int count() {
        int i = 0;
        for (T ignored : this) {
            i++;
        }
        return i;
    }

    /**
     * @return If this {@link StreamableIterable} is empty.
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Collect this {@link StreamableIterable} to an {@link ArrayList}.
     *
     * @return The {@link ArrayList}.
     */
    default ArrayList<T> toList() {
        return Lists.newArrayList(this);
    }

}