/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
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
 * Represents a fixed-size vector of three 32-bit
 * floating-point numbers, immutable.
 *
 * @since 1.0.0
 */
public final class Vector3Float implements Examinable, Iterable<Float> {

    /**
     * Constant for {@link Vector3Float} value with
     * abscissa, ordinate and applicate of {@code 0} zero.
     *
     * @since 1.0.0
     */
    public static final Vector3Float ZERO = new Vector3Float(0F, 0F, 0F);

    /**
     * Constant for {@link Vector3Float} value with
     * abscissa, ordinate and applicate of {@code 1} one.
     *
     * @since 1.0.0
     */
    public static final Vector3Float ONE = new Vector3Float(1F, 1F, 1F);

    private final float x;
    private final float y;
    private final float z;

    /**
     * Constructs a new {@link Vector3Float} with the
     * given {@code x}, {@code y} and {@code z} values.
     *
     * @param x The vector abscissa
     * @param y The vector ordinate
     * @param z The vector applicate
     * @since 1.0.0
     */
    public Vector3Float(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * Returns a new {@link Vector3Float vector} with
     * the given {@code x} value and the same {@code y}
     * and {@code z} values as this vector.
     *
     * @param x The new X value
     * @return The updated vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float x(final float x) {
        return new Vector3Float(x, this.y, this.z);
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
     * Returns a new {@link Vector3Float vector} with
     * the given {@code y} value and the same {@code x}
     * and {@code z} values as this vector.
     *
     * @param y The new Y value
     * @return The updated vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float y(final float y) {
        return new Vector3Float(this.x, y, this.z);
    }

    /**
     * Returns the "Z" component or applicate
     * of this vector.
     *
     * @return The vector applicate
     * @since 1.0.0
     */
    public float z() {
        return z;
    }

    /**
     * Returns a new {@link Vector3Float vector} with
     * the given {@code z} value and the same {@code x}
     * and {@code y} values as this vector.
     *
     * @param z The new Z value
     * @return The updated vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float z(final float z) {
        return new Vector3Float(this.x, this.y, z);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * adding the given {@code x}, {@code y} and {@code z}
     * values to this vector.
     *
     * @param x The value to add to the abscissa
     * @param y The value to add to the ordinate
     * @param z The value to add to the applicate
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public @NotNull Vector3Float add(final float x, final float y, final float z) {
        return new Vector3Float(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * adding the given {@code value} to this vector.
     *
     * @param value The value to add
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float add(final @NotNull Vector3Float value) {
        return new Vector3Float(this.x + value.x(), this.y + value.y(), this.z + value.z());
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * subtracting the given {@code x}, {@code y} and {@code z}
     * values from this vector.
     *
     * @param x The value to subtract from the abscissa
     * @param y The value to subtract from the ordinate
     * @param z The value to subtract from the applicate
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public @NotNull Vector3Float subtract(final float x, final float y, final float z) {
        return new Vector3Float(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * subtracting the given {@code value} from this vector.
     *
     * @param value The value to subtract
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float subtract(final @NotNull Vector3Float value) {
        return new Vector3Float(this.x - value.x(), this.y - value.y(), this.z - value.z());
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * multiplying this vector by the given scalar {@code value}.
     *
     * @param value The scalar value
     * @return The result vector
     * @since 1.0.0
     */
    public @NotNull Vector3Float multiply(final float value) {
        return new Vector3Float(this.x * value, this.y * value, this.z * value);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * multiplying this vector by the given {@code x}, {@code y}
     * and {@code z} values, one by one.
     *
     * @param x The factor for the abscissa
     * @param y The factor for the ordinate
     * @param z The factor for the applicate
     * @return The result vector
     * @since 1.0.0
     */
    public @NotNull Vector3Float multiply(final float x, final float y, final float z) {
        return new Vector3Float(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * multiplying this vector by the given {@code value},
     * one by one (Xa*Xb, Ya*Yb, Za*Zb).
     *
     * @param value The factor vector
     * @return The result vector
     * @since 1.0.0
     */
    public @NotNull Vector3Float multiply(final @NotNull Vector3Float value) {
        return new Vector3Float(this.x * value.x(), this.y * value.y(), this.z * value.z());
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * dividing this vector by the given scalar {@code value}.
     *
     * @param value The divisor
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float divide(final float value) {
        return new Vector3Float(this.x / value, this.y / value, this.z / value);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * dividing this vector by the given {@code x}, {@code y}
     * and {@code z} values, one by one.
     *
     * @param x The divisor for the abscissa
     * @param y The divisor for the ordinate
     * @param z The divisor for the applicate
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public @NotNull Vector3Float divide(final float x, final float y, final float z) {
        return new Vector3Float(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Returns a new {@link Vector3Float vector} result of
     * dividing this vector by the given {@code value},
     * one by one (Xa/Xb, Ya/Yb, Za/Zb).
     *
     * @param value The divisor vector
     * @return The result vector
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float divide(final @NotNull Vector3Float value) {
        return new Vector3Float(this.x / value.x(), this.y / value.y(), this.z / value.z());
    }

    /**
     * Returns the dot product of this and the given
     * vector.
     *
     * @param vector The vector to calculate the dot product with
     * @return The dot product
     * @since 1.0.0
     */
    @Contract(pure = true)
    public float dot(final @NotNull Vector3Float vector) {
        return this.x * vector.x() + this.y * vector.y() + this.z * vector.z();
    }

    /**
     * Returns the cross product of this and the given
     * vector.
     *
     * @param vector The vector to calculate the cross product with
     * @return The cross product
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vector3Float crossProduct(final @NotNull Vector3Float vector) {
        return new Vector3Float(
                y * vector.z - vector.y * z,
                z * vector.x - vector.z * x,
                x * vector.y - vector.x * y
        );
    }

    /**
     * Returns a new {@link Vector3Float vector} which is
     * the same as this vector, but with one component
     * updated.
     *
     * @param axis  The axis to update
     * @param value The new value for the axis
     * @return The updated vector
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    public @NotNull Vector3Float with(final @NotNull Axis3D axis, final float value) {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        switch (axis) {
            case X:
                x = value;
                break;
            case Y:
                y = value;
                break;
            case Z:
                z = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
        return new Vector3Float(x, y, z);
    }

    /**
     * Returns the component at a specific {@link Axis3D},
     * fails if axis is invalid
     *
     * @param axis The requested axis
     * @return The component at the given axis
     * @throws IllegalArgumentException If axis is not X, Y or Z
     */
    public float get(final @NotNull Axis3D axis) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }

    /**
     * Converts this vector to an array of
     * {@code float} values with X, Y, Z
     * order.
     *
     * @return This vector as an array
     * @since 1.0.0
     */
    public float @NotNull [] toArray() {
        return new float[]{x, y, z};
    }

    @NotNull
    @Override
    public Iterator<Float> iterator() {
        return Arrays.asList(x, y, z).iterator();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("x", x),
                ExaminableProperty.of("y", y),
                ExaminableProperty.of("z", z)
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
        Vector3Float that = (Vector3Float) o;
        return x == that.x
                && y == that.y
                && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}
