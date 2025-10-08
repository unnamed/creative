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
package team.unnamed.creative.model;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Axis3D;
import team.unnamed.creative.base.Vector3Float;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Class for representing {@link Element}
 * rotations in a single axis
 *
 * @since 1.0.0
 */
public class ElementRotation implements Examinable {

    public static final boolean DEFAULT_RESCALE = false;

    private final Vector3Float origin;
    private final Axis3D axis;
    private final float angle;
    private final boolean rescale;

    private ElementRotation(
            Vector3Float origin,
            Axis3D axis,
            float angle,
            boolean rescale
    ) {
        this.origin = requireNonNull(origin, "origin");
        this.axis = requireNonNull(axis, "axis");
        this.angle = angle;
        this.rescale = rescale;
        validate();
    }

    private void validate() {
        float absAngle = Math.abs(angle);
        if (absAngle > 45.0f)
            throw new IllegalArgumentException("Angle must be between [-45.0, 45.0] (inclusive), but was " + absAngle);
    }

    /**
     * Returns the rotation origin, a.k.a.
     * rotation pivot
     *
     * @return The rotation origin
     */
    public Vector3Float origin() {
        return origin;
    }

    /**
     * Returns the axis of the element
     * rotation, because elements can only
     * use rotation in a single axis
     *
     * @return The rotation axis
     */
    public Axis3D axis() {
        return axis;
    }

    /**
     * Returns the actual rotation angle in the
     * range of [-45.0, 45.0]
     *
     * @return The rotation angle
     */
    public float angle() {
        return angle;
    }

    /**
     * Returns true if faces will be
     * scaled across the whole block
     */
    public boolean rescale() {
        return rescale;
    }

    public ElementRotation origin(Vector3Float origin) {
        return new ElementRotation(origin, this.axis, this.angle, this.rescale);
    }

    public ElementRotation axis(Axis3D axis) {
        return new ElementRotation(this.origin, axis, this.angle, this.rescale);
    }

    public ElementRotation angle(float angle) {
        return new ElementRotation(this.origin, this.axis, angle, this.rescale);
    }

    public ElementRotation rescale(boolean rescale) {
        return new ElementRotation(this.origin, this.axis, this.angle, rescale);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("origin", origin),
                ExaminableProperty.of("axis", axis),
                ExaminableProperty.of("angle", angle),
                ExaminableProperty.of("rescale", rescale)
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
        ElementRotation that = (ElementRotation) o;
        return Float.compare(that.angle, angle) == 0
                && rescale == that.rescale
                && origin.equals(that.origin)
                && axis == that.axis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, axis, angle, rescale);
    }

    /**
     * Creates a new {@link ElementRotation} instance
     * from the provided values
     *
     * @param origin  The rotation origin or pivot
     * @param axis    The rotation axis
     * @param angle   The rotation angle (value)
     * @param rescale Whether to rescale the faces
     *                to the whole block
     * @return A new {@link ElementRotation} instance
     * @since 1.0.0
     */
    public static ElementRotation of(
            Vector3Float origin,
            Axis3D axis,
            float angle,
            boolean rescale
    ) {
        return new ElementRotation(
                origin, axis, angle, rescale
        );
    }

    /**
     * Static factory method for instantiating our
     * builder implementation
     *
     * @return A new builder for {@link ElementRotation}
     * instances
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for creating
     * {@link ElementRotation} instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Vector3Float origin;
        private Axis3D axis;
        private float angle;
        private boolean rescale = DEFAULT_RESCALE;

        private Builder() {
        }

        public Builder origin(Vector3Float origin) {
            this.origin = requireNonNull(origin, "origin");
            return this;
        }

        public Builder axis(Axis3D axis) {
            this.axis = requireNonNull(axis, "axis");
            return this;
        }

        public Builder angle(float angle) {
            this.angle = requireNonNull(angle, "angle");
            return this;
        }

        public Builder rescale(boolean rescale) {
            this.rescale = rescale;
            return this;
        }

        /**
         * Finishes building the {@link ElementRotation}
         * instance, this method can be invoked multiple
         * times, the builder is re-usable
         *
         * @return The element rotation
         */
        public ElementRotation build() {
            return new ElementRotation(origin, axis, angle, rescale);
        }

    }

}
