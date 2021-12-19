/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.model;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.Axis3D;
import team.unnamed.uracle.CubeFace;
import team.unnamed.uracle.Vector3Float;
import team.unnamed.uracle.Vector4Int;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link Model} cubic element,
 * has "from" and "to" locations, rotation,
 * translation and scale
 *
 * @since 1.0.0
 */
public class ModelElement implements Examinable {

    /**
     * Start point of a cuboid. Values must be
     * between -16 and 32.
     */
    private final Vector3Float from;

    /**
     * Stop point of a cuboid. Values must be
     * between -16 and 32.
     */
    private final Vector3Float to;

    /**
     * Defines the element rotation in a single
     * axis (it is not possible to use rotation
     * in multiple axis)
     */
    private final ModelElement.Rotation rotation;

    /**
     * Determines whether shadows are rendered
     * for this element
     */
    private final boolean shade;

    /**
     * Holds all the faces of the cuboid. If a face is
     * left out, it does not render
     */
    @Unmodifiable private final Map<CubeFace, Face> faces;

    private ModelElement(
            Vector3Float from,
            Vector3Float to,
            ModelElement.Rotation rotation,
            boolean shade,
            Map<CubeFace, Face> faces
    ) {
        requireNonNull(faces, "faces");
        this.from = requireNonNull(from, "from");
        this.to = requireNonNull(to, "to");
        this.rotation = requireNonNull(rotation, "rotation");
        this.shade = shade;
        this.faces = unmodifiableMap(new HashMap<>(faces));
    }

    /**
     * Class for representing {@link ModelElement}
     * rotations in a single axis
     *
     * @since 1.0.0
     */
    public static class Rotation {

        private final Vector3Float origin;
        private final Axis3D axis;
        private final float angle;
        private final boolean rescale;

        private Rotation(
                Vector3Float origin,
                Axis3D axis,
                float angle,
                boolean rescale
        ) {
            this.origin = requireNonNull(origin, "origin");
            this.axis = requireNonNull(axis, "axis");
            this.angle = angle;
            this.rescale = rescale;
        }
    }

    /**
     * Defines the properties of a {@link CubeFace}
     * from a {@link ModelElement}
     *
     * @since 1.0.0
     */
    public static class Face {

        /**
         * Defines the area of the texture to
         * use according to the scheme [x1, y1, x2, y2]
         *
         * <p>The texture behavior is inconsistent if
         * UV extends below 0 or above 16. If the numbers
         * of x1 and x2 are swapped the texture flips</p>
         *
         * <p>UV is optional, and if not supplied it automatically
         * generates based on the element's position.</p>
         */
        @Nullable private final Vector4Int uv;

        /**
         * Specifies the face texture in form of the
         * texture variable prepended with a "#"
         */
        private final String texture;

        /**
         * Specifies whether a face does not need to be rendered
         * when there is a block touching it in the specified
         * position. It also determines the side of the block to
         * use the light level from for lighting the face
         */
        @Nullable private final CubeFace cullFace;

        /**
         * Rotates the texture by the specified number of degrees.
         * Can be 0, 90, 180, or 270. Rotation does not affect which
         * part of the texture is used. Instead, it amounts to permutation
         * of the selected texture vertexes (selected implicitly, or
         * explicitly though uv)
         */
        private final int rotation;

        /**
         * Determines whether to tint the texture using a hardcoded tint
         * index. The default is not using tints (-1 for blocks, unset for
         * items), and for item elements, any number caused it to use tint
         */
        @Nullable private final Integer tintIndex;

        private Face(
                @Nullable Vector4Int uv,
                String texture,
                @Nullable CubeFace cullFace,
                int rotation,
                @Nullable Integer tintIndex
        ) {
            this.uv = uv;
            this.texture = texture;
            this.cullFace = cullFace;
            this.rotation = rotation;
            this.tintIndex = tintIndex;
        }
    }

}
