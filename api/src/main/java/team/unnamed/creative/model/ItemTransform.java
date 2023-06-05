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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.util.Range;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Place where an item or block model is displayed. Holds its
 * rotation, translation and scale for the specified situation
 *
 * @since 1.0.0
 */
public class ItemTransform implements Examinable {

    public static final float MIN_TRANSLATION = -80F;
    public static final float MAX_TRANSLATION = 80F;

    public static final float MIN_SCALE = -4F;
    public static final float MAX_SCALE = 4F;

    public static final Vector3Float DEFAULT_ROTATION = Vector3Float.ZERO;
    public static final Vector3Float DEFAULT_TRANSLATION = Vector3Float.ZERO;
    public static final Vector3Float DEFAULT_SCALE = Vector3Float.ONE;

    private final Vector3Float rotation;
    private final Vector3Float translation;
    private final Vector3Float scale;

    private ItemTransform(
            Vector3Float rotation,
            Vector3Float translation,
            Vector3Float scale
    ) {
        this.rotation = requireNonNull(rotation, "rotation");
        this.translation = requireNonNull(translation, "translation");
        this.scale = requireNonNull(scale, "scale");
        validate();
    }

    private void validate() {
        if (Range.isBetween(translation, MIN_TRANSLATION, MAX_TRANSLATION)) {
            throw new IllegalArgumentException("Invalid translation (" + translation + ")" +
                    ", out of bounds (" + MIN_TRANSLATION + " to " + MAX_TRANSLATION + ")");
        }

        if (Range.isBetween(scale, MIN_SCALE, MAX_SCALE)) {
            throw new IllegalArgumentException("Invalid scale (" + scale + ")" +
                    ", out of bounds (" + MIN_SCALE + " to " + MAX_SCALE + ")");
        }
    }

    /**
     * Returns the rotation for this model
     * display
     *
     * @return The model display rotation
     */
    public Vector3Float rotation() {
        return rotation;
    }

    /**
     * Returns the position of the model
     *
     * <p>Components are always between {@code MIN_TRANSLATION}
     * and {@code MAX_TRANSLATION}</p>
     *
     * @return The model display translation
     */
    public Vector3Float translation() {
        return translation;
    }

    /**
     * Returns the scale of the mode
     *
     * <p>Components are always between {@code MIN_SCALE}
     * and {@code MAX_SCALE} constants</p>
     *
     * @return The model display scale
     */
    public Vector3Float scale() {
        return scale;
    }

    /**
     * Enum of all possible model displays
     *
     * @since 1.0.0
     */
    public enum Type {
        THIRDPERSON_RIGHTHAND,
        THIRDPERSON_LEFTHAND,
        FIRSTPERSON_RIGHTHAND,
        FIRSTPERSON_LEFTHAND,
        GUI,
        HEAD,
        GROUND,
        FIXED
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
        ItemTransform that = (ItemTransform) o;
        return rotation.equals(that.rotation)
                && translation.equals(that.translation)
                && scale.equals(that.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotation, translation, scale);
    }

    /**
     * Creates a new {@link ItemTransform} instance from
     * the given values
     *
     * @param rotation The display rotation
     * @param translation The display translation [-80, 80]
     * @param scale The display scale [0, 4]
     * @return A new {@link ItemTransform} instance
     * @since 1.0.0
     */
    public static ItemTransform of(
            Vector3Float rotation,
            Vector3Float translation,
            Vector3Float scale
    ) {
        return new ItemTransform(rotation, translation, scale);
    }

    /**
     * Static factory method for instantiating our
     * builder implementation, which eases the creation
     * of {@link ItemTransform} instances
     *
     * @return A new builder instance
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for creating {@link ItemTransform}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Vector3Float rotation = DEFAULT_ROTATION;
        private Vector3Float translation = DEFAULT_TRANSLATION;
        private Vector3Float scale = DEFAULT_SCALE;

        private Builder() {
        }

        public Builder rotation(Vector3Float rotation) {
            this.rotation = requireNonNull(rotation, "rotation");
            return this;
        }

        public Builder translation(Vector3Float translation) {
            this.translation = requireNonNull(translation, "translation");
            return this;
        }

        public Builder scale(Vector3Float scale) {
            this.scale = requireNonNull(scale, "scale");
            return this;
        }

        public Vector3Float getRotation() {
            return rotation;
        }

        public Vector3Float getTranslation() {
            return translation;
        }

        public Vector3Float getScale() {
            return scale;
        }

        /**
         * Finished building the {@link ItemTransform} instance
         * using the previously set values
         *
         * @return A new {@link ItemTransform} instance
         */
        public ItemTransform build() {
            return new ItemTransform(rotation, translation, scale);
        }

    }

}
