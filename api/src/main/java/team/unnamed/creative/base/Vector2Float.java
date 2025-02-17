/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.creative.base;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a fixed-size vector of two 32-bit
 * floating-point numbers, immutable.
 *
 * @since 1.0.0
 */
public final class Vector2Float implements Examinable, Iterable<Float> {

    /**
     * Constant for {@link Vector2Float} value with
     * abscissa and ordinate of {@code 0} zero.
     *
     * @since 1.0.0
     */
    public static final Vector2Float ZERO = new Vector2Float(0F, 0F);

    /**
     * Constant for {@link Vector2Float} value with
     * abscissa and ordinate of {@code 1} one.
     *
     * @since 1.1.0
     */
    public static final Vector2Float ONE = new Vector2Float(1F, 1F);

    private final float x;
    private final float y;

    /**
     * Constructs a new {@link Vector2Float} with the
     * given {@code x} and {@code y} values.
     *
     * @param x The vector abscissa
     * @param y The vector ordinate
     * @since 1.0.0
     */
    public Vector2Float(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the "X" component or abscissa
     * of this vector.
     *
     * @return The vector abscissa
     * @since 1.0.0
     */
    public float x() {
        return x;
    }

    /**
     * Returns the "Y" component or ordinate
     * of this vector.
     *
     * @return The vector ordinate
     * @since 1.0.0
     */
    public float y() {
        return y;
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * adding the given {@code value} to this vector.
     *
     * @param value The value to add
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float add(final @NotNull Vector2Float value) {
        return new Vector2Float(x + value.x, y + value.y);
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * subtracting the given {@code value} to this vector.
     *
     * @param value The value to subtract
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float subtract(final @NotNull Vector2Float value) {
        return new Vector2Float(x - value.x, y - value.y);
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * multiplying the given {@code value} vector by this vector.
     *
     * <p>The resulting X component is calculated by multiplying the
     * X components of both vectors.</p>
     *
     * <p>The resulting Y component is calculated by multiplying the
     * Y components of both vectors.</p>
     *
     * @param value The value to multiply
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float multiply(final @NotNull Vector2Float value) {
        return new Vector2Float(x * value.x, y * value.y);
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * multiplying this vector by the given scalar {@code value}.
     *
     * @param value The factor
     * @return The result vector
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float multiply(final float value) {
        return new Vector2Float(x * value, y * value);
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * dividing this vector by the given {@code value} vector.
     *
     * <p>The resulting X component is calculated by dividing the
     * X components of both vectors.</p>
     *
     * <p>The resulting Y component is calculated by dividing the
     * Y components of both vectors.</p>
     *
     * @param value The value to divide
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float divide(final @NotNull Vector2Float value) {
        return new Vector2Float(x / value.x, y / value.y);
    }

    /**
     * Returns a new {@link Vector2Float vector} result of
     * dividing this vector by the given scalar {@code value}.
     *
     * @param value The divisor
     * @return The result vector
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector2Float divide(final float value) {
        return new Vector2Float(x / value, y / value);
    }

    /**
     * Converts this vector object to a {@code float} array
     * with the X component at index {@code 0} and the Y component
     * at index {@code 1}.
     *
     * @return This vector as an array
     * @since 1.0.0
     */
    @Contract(value = "-> new", pure = true)
    public float @NotNull [] toArray() {
        return new float[]{x, y};
    }

    @NotNull
    @Override
    public Iterator<Float> iterator() {
        return Arrays.asList(x, y).iterator();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("x", x),
                ExaminableProperty.of("y", y)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2Float that = (Vector2Float) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
