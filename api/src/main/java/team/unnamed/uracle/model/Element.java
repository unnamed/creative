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
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.CubeFace;
import team.unnamed.uracle.Vector3Float;

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
public class Element implements Examinable {

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
    private final ElementRotation rotation;

    /**
     * Determines whether shadows are rendered
     * for this element
     */
    private final boolean shade;

    /**
     * Holds all the faces of the cuboid. If a face is
     * left out, it does not render
     */
    @Unmodifiable private final Map<CubeFace, ElementFace> faces;

    private Element(
            Vector3Float from,
            Vector3Float to,
            ElementRotation rotation,
            boolean shade,
            Map<CubeFace, ElementFace> faces
    ) {
        requireNonNull(faces, "faces");
        this.from = requireNonNull(from, "from");
        this.to = requireNonNull(to, "to");
        this.rotation = requireNonNull(rotation, "rotation");
        this.shade = shade;
        this.faces = unmodifiableMap(new HashMap<>(faces));
    }

}
