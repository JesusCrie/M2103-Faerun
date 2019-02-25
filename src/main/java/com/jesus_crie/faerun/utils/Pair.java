package com.jesus_crie.faerun.utils;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Associate two objects together.
 *
 * @param <T> - The left type.
 * @param <V> - The right type.
 */
public class Pair<T, V> implements Serializable {

    private static final long serialVersionUID = 732229522109241921L;

    private final T left;
    private final V right;

    public Pair(@Nonnull final T left,
                @Nonnull final V right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return The left object of the pair.
     */
    @Nonnull
    public T getLeft() {
        return left;
    }

    /**
     * @return The right object of the pair.
     */
    @Nonnull
    public V getRight() {
        return right;
    }

    /**
     * Factory method to create a pair.
     *
     * @param left  - The left object.
     * @param right - The right object.
     * @param <T>   - The left type.
     * @param <V>   - The right type.
     * @return A new pair containing the two objects.
     */
    @Nonnull
    public static <T, V> Pair<T, V> of(@Nonnull final T left, @Nonnull final V right) {
        return new Pair<>(left, right);
    }
}
