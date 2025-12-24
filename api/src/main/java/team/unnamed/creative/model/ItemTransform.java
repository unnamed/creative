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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Vector3Float;

/**
 * Place where an item or block model is displayed. Holds its
 * rotation, translation and scale for the specified situation
 *
 * @since 1.0.0
 */
public interface ItemTransform extends Examinable {

    float MIN_TRANSLATION = -80F;
    float MAX_TRANSLATION = 80F;

    float MIN_SCALE = -4F;
    float MAX_SCALE = 4F;

    Vector3Float DEFAULT_ROTATION = Vector3Float.ZERO;
    Vector3Float DEFAULT_TRANSLATION = Vector3Float.ZERO;
    Vector3Float DEFAULT_SCALE = Vector3Float.ONE;

    /**
     * Returns the rotation for this model
     * display
     *
     * @return The model display rotation
     * @since 1.0.0
     */
    @NotNull Vector3Float rotation();

    /**
     * Returns the position of the model
     *
     * <p>Components are always between {@code MIN_TRANSLATION}
     * and {@code MAX_TRANSLATION}</p>
     *
     * @return The model display translation
     * @since 1.0.0
     */
    @NotNull Vector3Float translation();

    /**
     * Returns the scale of the mode
     *
     * <p>Components are always between {@code MIN_SCALE}
     * and {@code MAX_SCALE} constants</p>
     *
     * @return The model display scale
     * @since 1.0.0
     */
    @NotNull Vector3Float scale();

    /**
     * Enum of all possible model displays
     *
     * @since 1.0.0
     */
    enum Type {
        THIRDPERSON_RIGHTHAND,
        THIRDPERSON_LEFTHAND,
        FIRSTPERSON_RIGHTHAND,
        FIRSTPERSON_LEFTHAND,
        GUI,
        HEAD,
        GROUND,
        FIXED,
        ON_SHELF
    }

    /**
     * Creates a new {@link ItemTransform} instance from
     * the given values
     *
     * @param rotation    The display rotation
     * @param translation The display translation [-80, 80]
     * @param scale       The display scale [0, 4]
     * @return A new {@link ItemTransform} instance
     * @since 1.0.0
     */
    static @NotNull ItemTransform transform(final @NotNull Vector3Float rotation, final @NotNull Vector3Float translation, final @NotNull Vector3Float scale) {
        return new ItemTransformImpl(rotation, translation, scale);
    }

    /**
     * Static factory method for instantiating our
     * builder implementation, which eases the creation
     * of {@link ItemTransform} instances
     *
     * @return A new builder instance
     * @since 1.0.0
     */
    static @NotNull Builder transform() {
        return new ItemTransformImpl.BuilderImpl();
    }

    /**
     * Creates a new {@link ItemTransform} instance from
     * the given values
     *
     * @param rotation    The display rotation
     * @param translation The display translation [-80, 80]
     * @param scale       The display scale [0, 4]
     * @return A new {@link ItemTransform} instance
     * @since 1.0.0
     * @deprecated Use {@link #transform(Vector3Float, Vector3Float, Vector3Float)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull ItemTransform of(final @NotNull Vector3Float rotation, final @NotNull Vector3Float translation, final @NotNull Vector3Float scale) {
        return new ItemTransformImpl(rotation, translation, scale);
    }

    /**
     * Static factory method for instantiating our
     * builder implementation, which eases the creation
     * of {@link ItemTransform} instances
     *
     * @return A new builder instance
     * @since 1.0.0
     * @deprecated Use {@link #transform()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Builder builder() {
        return new ItemTransformImpl.BuilderImpl();
    }

    /**
     * Builder implementation for creating {@link ItemTransform}
     * instances
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the rotation for the model display.
         *
         * @param rotation The rotation
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder rotation(final @NotNull Vector3Float rotation);

        /**
         * Sets the translation for the model display [-80, 80]
         *
         * @param translation The translation
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder translation(final @NotNull Vector3Float translation);

        /**
         * Sets the scale for the model display [0, 4]
         *
         * @param scale The scale
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder scale(final @NotNull Vector3Float scale);

        /**
         * Finished building the {@link ItemTransform} instance
         * using the previously set values
         *
         * @return A new {@link ItemTransform} instance
         * @since 1.0.0
         */
        @Contract("-> this")
        @NotNull ItemTransform build();

    }

}