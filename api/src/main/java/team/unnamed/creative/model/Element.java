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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector3Float;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

/**
 * Represents a {@link Model} cubic element,
 * has "from" and "to" locations, rotation,
 * translation and scale
 *
 * @since 1.0.0
 */
public class Element implements Examinable {

    public static final boolean DEFAULT_SHADE = true;

    public static final float MIN_EXTENT = -16F;
    public static final float MAX_EXTENT = 32F;

    private final Vector3Float from;
    private final Vector3Float to;
    @Nullable private final ElementRotation rotation;
    private final boolean shade;
    @Unmodifiable private final Map<CubeFace, ElementFace> faces;

    private Element(
            Vector3Float from,
            Vector3Float to,
            @Nullable ElementRotation rotation,
            boolean shade,
            Map<CubeFace, ElementFace> faces
    ) {
        requireNonNull(from, "from");
        requireNonNull(to, "to");
        requireNonNull(faces, "faces");
        this.from = from;
        this.to = to;
        this.rotation = rotation;
        this.shade = shade;
        this.faces = immutableMapOf(faces);
        validate();
    }

    private void validateBound(float value, String axisName) {
        if (value < MIN_EXTENT || value > MAX_EXTENT)
            throw new IllegalArgumentException("Value at " + axisName + " axis (" + value + ") is out of bounds");
    }

    private void validateBound(Vector3Float vec) {
        validateBound(vec.x(), "X");
        validateBound(vec.y(), "Y");
        validateBound(vec.z(), "Z");
    }

    private void validate() {
        validateBound(from);
        validateBound(to);
        if (faces.size() > 6)
            throw new IllegalArgumentException("Invalid amount of faces (" + faces.size() + ")");
    }

    /**
     * Returns the starting point of the
     * element cuboid. Values must be
     * between -16 and 32
     *
     * @return The cuboid origin
     */
    public Vector3Float from() {
        return from;
    }

    /**
     * Returns the stop point of the element
     * cuboid. Values must be between -16 and
     * 32
     *
     * @return The cuboid stop point
     */
    public Vector3Float to() {
        return to;
    }

    /**
     * Returns the element rotation in a single
     * axis (it is not possible to use rotation
     * in multiple axis)
     *
     * @return The element rotation
     */
    public ElementRotation rotation() {
        return rotation;
    }

    /**
     * Determines whether to render shadows
     * or not for this element
     *
     * @return True to render shadows
     */
    public boolean shade() {
        return shade;
    }

    /**
     * Returns an unmodifiable map of the
     * element faces specifications. If a
     * face is left out, it does not render
     *
     * @return The element faces
     */
    public @Unmodifiable Map<CubeFace, ElementFace> faces() {
        return faces;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("from", from),
                ExaminableProperty.of("to", to),
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("shade", shade),
                ExaminableProperty.of("faces", faces)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return from.equals(element.from)
                && to.equals(element.to)
                && Objects.equals(rotation, element.rotation)
                && shade == element.shade
                && faces.equals(element.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, rotation, shade, faces);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Vector3Float from;
        private Vector3Float to;
        private ElementRotation rotation = null;
        private boolean shade = DEFAULT_SHADE;
        private Map<CubeFace, ElementFace> faces;

        private Builder() {
        }

        public Builder from(Vector3Float from) {
            this.from = requireNonNull(from, "from");
            return this;
        }

        public Builder to(Vector3Float to) {
            this.to = requireNonNull(to, "to");
            return this;
        }

        public Builder rotation(@Nullable ElementRotation rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder shade(boolean shade) {
            this.shade = shade;
            return this;
        }

        public Builder faces(Map<CubeFace, ElementFace> faces) {
            this.faces = requireNonNull(faces, "faces");
            return this;
        }

        public Builder face(CubeFace type, ElementFace face) {
            requireNonNull(type, "type");
            requireNonNull(face, "face");
            if (this.faces == null) {
                this.faces = new HashMap<>();
            }
            this.faces.put(type, face);
            return this;
        }

        public Element build() {
            if (this.faces == null) {
                this.faces = Collections.emptyMap();
            }
            return new Element(from, to, rotation, shade, faces);
        }

    }

}
