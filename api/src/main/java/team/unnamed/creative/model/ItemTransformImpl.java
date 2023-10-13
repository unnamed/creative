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
package team.unnamed.creative.model;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Vector3Float;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ItemTransformImpl implements ItemTransform {

    private final Vector3Float rotation;
    private final Vector3Float translation;
    private final Vector3Float scale;

    ItemTransformImpl(
            final @NotNull Vector3Float rotation,
            final @NotNull Vector3Float translation,
            final @NotNull Vector3Float scale
    ) {
        this.rotation = requireNonNull(rotation, "rotation");
        this.translation = requireNonNull(translation, "translation");
        this.scale = requireNonNull(scale, "scale");
        validate();
    }

    private void validate() {
        if (translation.x() < MIN_TRANSLATION || translation.x() > MAX_TRANSLATION
                || translation.y() < MIN_TRANSLATION || translation.y() > MAX_TRANSLATION
                || translation.z() < MIN_TRANSLATION || translation.z() > MAX_TRANSLATION) {
            throw new IllegalArgumentException("Invalid translation (" + translation + ")" +
                    ", out of bounds (" + MIN_TRANSLATION + " to " + MAX_TRANSLATION + ")");
        }

        if (scale.x() < MIN_SCALE || scale.x() > MAX_SCALE
                || scale.y() < MIN_SCALE || scale.y() > MAX_SCALE
                || scale.z() < MIN_SCALE || scale.z() > MAX_SCALE) {
            throw new IllegalArgumentException("Invalid scale (" + scale + ")" +
                    ", out of bounds (" + MIN_SCALE + " to " + MAX_SCALE + ")");
        }
    }

    @Override
    public @NotNull Vector3Float rotation() {
        return rotation;
    }

    @Override
    public @NotNull Vector3Float translation() {
        return translation;
    }

    @Override
    public @NotNull Vector3Float scale() {
        return scale;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("translation", translation),
                ExaminableProperty.of("scale", scale)
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
        ItemTransformImpl that = (ItemTransformImpl) o;
        return rotation.equals(that.rotation)
                && translation.equals(that.translation)
                && scale.equals(that.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotation, translation, scale);
    }

    static final class BuilderImpl implements Builder {

        private Vector3Float rotation = DEFAULT_ROTATION;
        private Vector3Float translation = DEFAULT_TRANSLATION;
        private Vector3Float scale = DEFAULT_SCALE;

        @Override
        public @NotNull ItemTransform.Builder rotation(final @NotNull Vector3Float rotation) {
            this.rotation = requireNonNull(rotation, "rotation");
            return this;
        }

        @Override
        public @NotNull ItemTransform.Builder translation(final @NotNull Vector3Float translation) {
            this.translation = requireNonNull(translation, "translation");
            return this;
        }

        @Override
        public @NotNull ItemTransform.Builder scale(final @NotNull Vector3Float scale) {
            this.scale = requireNonNull(scale, "scale");
            return this;
        }

        @Override
        public @NotNull ItemTransform build() {
            return new ItemTransformImpl(rotation, translation, scale);
        }

    }
}
