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

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.CubeFace;
import team.unnamed.uracle.Vector4Int;

/**
 * Defines the properties of a {@link CubeFace}
 * from a {@link Element}
 *
 * @since 1.0.0
 */
public class ElementFace {

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
    @Nullable
    private final Vector4Int uv;

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

    private ElementFace(
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
