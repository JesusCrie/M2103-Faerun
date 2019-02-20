package com.jesus_crie.faerun.utils;

import javax.annotation.Nonnull;

public class Pair<T, V> {

    private final T left;
    private final V right;

    public Pair(@Nonnull final T left,
                @Nonnull final V right) {
        this.left = left;
        this.right = right;
    }

    @Nonnull
    public T getLeft() {
        return left;
    }

    @Nonnull
    public V getRight() {
        return right;
    }

    @Nonnull
    public static <T, V> Pair<T, V> of(final T left, final V right) {
        return new Pair<>(left, right);
    }
}
