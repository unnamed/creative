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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector3Float;

import java.util.Map;

/**
 * Represents a {@link Model} cubic element,
 * has "from" and "to" locations, rotation,
 * translation and scale
 *
 * @since 1.0.0
 */
public interface Element extends Examinable {
    /**
     * Creates a new {@link Element} builder
     *
     * @return The new builder
     * @since 1.2.0
     */
    @Contract("-> new")
    static @NotNull Builder element() {
        return new ElementImpl.BuilderImpl();
    }

    /**
     * Creates a new {@link Element} builder
     *
     * @return The new builder
     * @since 1.0.0
     * @deprecated Use {@link #element()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("-> new")
    static @NotNull Builder builder() {
        return element();
    }

    boolean DEFAULT_SHADE = true;

    float MIN_EXTENT = -16F;
    float MAX_EXTENT = 32F;

    /**
     * Returns the starting point of the
     * element cuboid.
     *
     * @return The cuboid origin
     * @since 1.0.0
     */
    @NotNull Vector3Float from();

    /**
     * Returns the stop point of the element
     * cuboid.
     *
     * @return The cuboid stop point
     * @since 1.0.0
     */
    @NotNull Vector3Float to();

    /**
     * Gets the element rotation.
     *
     * @return The element rotation
     * @since 1.0.0
     */
    @NotNull ElementRotation rotation();

    /**
     * Determines whether to render shadows
     * for this element or not
     *
     * @return True to render shadows
     * @since 1.0.0
     */
    boolean shade();

    /**
     * Returns an unmodifiable map of the element faces
     * specifications.
     *
     * <p>If a face is left out, it does not render</p>
     *
     * @return The element faces
     * @since 1.0.0
     */
    @Unmodifiable @NotNull Map<CubeFace, ElementFace> faces();

    int lightEmission();

    /**
     * A builder for {@link Element} instances.
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the starting point of the element cuboid.
         *
         * @param from The cuboid origin
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder from(final @NotNull Vector3Float from);

        /**
         * Sets the starting point of the element cuboid.
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @param z The z coordinate
         * @return This builder
         * @since 1.5.0
         */
        @Contract("_, _, _ -> this")
        default @NotNull Builder from(final float x, final float y, final float z) {
            return from(new Vector3Float(x, y, z));
        }

        /**
         * Sets the stop point of the element cuboid.
         *
         * @param to The cuboid stop point
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder to(final @NotNull Vector3Float to);

        /**
         * Sets the stop point of the element cuboid.
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @param z The z coordinate
         * @return This builder
         * @since 1.5.0
         */
        default @NotNull Builder to(final float x, final float y, final float z) {
            return to(new Vector3Float(x, y, z));
        }

        /**
         * Sets the element rotation.
         *
         * @param rotation The element rotation
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder rotation(final @Nullable ElementRotation rotation);

        /**
         * Sets whether to render shadows for this element or not.
         *
         * @param shade True to render shadows
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder shade(final boolean shade);

        /**
         * Sets the element faces.
         *
         * @param faces The element faces
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder faces(final @NotNull Map<CubeFace, ElementFace> faces);

        /**
         * Adds a face to the element.
         *
         * @param type Which face to add
         * @param face The face data
         * @return This builder
         * @since 1.2.0
         */
        @NotNull Builder addFace(final @NotNull CubeFace type, final @NotNull ElementFace face);

        /**
         * Adds a face to the element.
         *
         * @param type Which face to add
         * @param face The face data
         * @return This builder
         * @since 1.0.0
         * @deprecated Use {@link #addFace(CubeFace, ElementFace)} instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        @Contract("_, _ -> this")
        default @NotNull Builder face(final @NotNull CubeFace type, final @NotNull ElementFace face) {
            return addFace(type, face);
        }

        /**
         * Sets whether the light-emission of the element.
         *
         * @param lightEmission The light-level of the element
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder lightEmission(final int lightEmission);

        /**
         * Builds the element.
         *
         * @return The new element
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull Element build();

    }

}
