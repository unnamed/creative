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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Enum of three-dimensional cube faces.
 *
 * <p>Every face has an {@link Axis3D axis} and
 * an orientation (positive or negative).</p>
 *
 * <p>Note that the directions specified here are
 * the same as the Minecraft ones.</p>
 *
 * @since 1.0.0
 */
public enum CubeFace {
    /**
     * The west face. It faces the negative
     * X direction.
     *
     * @since 1.0.0
     */
    WEST(Axis3D.X, -1),

    /**
     * The east face. It faces the positive
     * X direction.
     *
     * @since 1.0.0
     */
    EAST(Axis3D.X, 1),

    /**
     * The down face. It faces the negative
     * Y direction.
     *
     * @since 1.0.0
     */
    DOWN(Axis3D.Y, -1),

    /**
     * The up face. It faces the positive
     * Y direction.
     *
     * @since 1.0.0
     */
    UP(Axis3D.Y, 1),

    /**
     * The north face. It faces the negative
     * Z direction.
     *
     * @since 1.0.0
     */
    NORTH(Axis3D.Z, -1),

    /**
     * The south face. It faces the positive
     * Z direction.
     *
     * @since 1.0.0
     */
    SOUTH(Axis3D.Z, 1);

    private final Axis3D axis;
    private final int factor;

    CubeFace(final @NotNull Axis3D axis, final int factor) {
        this.axis = Objects.requireNonNull(axis, "axis");
        this.factor = factor;
    }

    /**
     * Returns the axis of this face.
     *
     * <p>The axis is X for west and east, Y for
     * down and up, and Z for north and south</p>
     *
     * @return The face axis
     * @since 1.0.0
     */
    public @NotNull Axis3D axis() {
        return axis;
    }

    /**
     * Returns the factor of this face direction.
     *
     * <p>The factor is 1 for positive directions
     * and -1 for negative directions</p>
     *
     * @return The face factor
     * @since 1.0.0
     */
    public int factor() {
        return factor;
    }

}
