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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector3Float;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

final class ElementImpl implements Element {

    private final Vector3Float from;
    private final Vector3Float to;
    private final ElementRotation rotation;
    private final boolean shade;
    private final Map<CubeFace, ElementFace> faces;
    private final int lightEmission;

    ElementImpl(
            final @NotNull Vector3Float from,
            final @NotNull Vector3Float to,
            final @Nullable ElementRotation rotation,
            final boolean shade,
            final @NotNull Map<CubeFace, ElementFace> faces,
            final int lightEmission
    ) {
        this.from = requireNonNull(from, "from");
        this.to = requireNonNull(to, "to");
        this.rotation = rotation;
        this.shade = shade;
        this.faces = immutableMapOf(requireNonNull(faces, "faces"));
        this.lightEmission = lightEmission;
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
        if (faces.size() < 1 || faces.size() > 6)
            throw new IllegalArgumentException("Invalid amount of faces (" + faces.size() + ")");
    }

    @Override
    public @NotNull Vector3Float from() {
        return from;
    }

    @Override
    public @NotNull Vector3Float to() {
        return to;
    }

    @Override
    public @NotNull ElementRotation rotation() {
        return rotation;
    }

    @Override
    public boolean shade() {
        return shade;
    }

    @Override
    public @Unmodifiable @NotNull Map<CubeFace, ElementFace> faces() {
        return faces;
    }

    @Override
    public int lightEmission() {
        return lightEmission;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("from", from),
                ExaminableProperty.of("to", to),
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("shade", shade),
                ExaminableProperty.of("faces", faces),
                ExaminableProperty.of("light_emission", lightEmission)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementImpl element = (ElementImpl) o;
        return from.equals(element.from)
                && to.equals(element.to)
                && Objects.equals(rotation, element.rotation)
                && shade == element.shade
                && faces.equals(element.faces)
                && lightEmission == element.lightEmission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, rotation, shade, faces, lightEmission);
    }

    static final class BuilderImpl implements Builder {

        private Vector3Float from;
        private Vector3Float to;
        private ElementRotation rotation = null;
        private boolean shade = DEFAULT_SHADE;
        private Map<CubeFace, ElementFace> faces = new LinkedHashMap<>();
        private int lightEmission = 0;


        @Override
        public @NotNull Builder from(final @NotNull Vector3Float from) {
            this.from = requireNonNull(from, "from");
            return this;
        }

        @Override
        public @NotNull Builder to(final @NotNull Vector3Float to) {
            this.to = requireNonNull(to, "to");
            return this;
        }

        @Override
        public @NotNull Builder rotation(final @Nullable ElementRotation rotation) {
            this.rotation = rotation;
            return this;
        }

        @Override
        public @NotNull Builder shade(final boolean shade) {
            this.shade = shade;
            return this;
        }

        @Override
        public @NotNull Builder faces(final @NotNull Map<CubeFace, ElementFace> faces) {
            this.faces = new LinkedHashMap<>(requireNonNull(faces, "faces"));
            return this;
        }

        @Override
        public @NotNull Builder addFace(final @NotNull CubeFace type, final @NotNull ElementFace face) {
            requireNonNull(type, "type");
            requireNonNull(face, "face");
            this.faces.put(type, face);
            return this;
        }

        @Override
        public @NotNull Builder lightEmission(final int lightEmission) {
            this.lightEmission = lightEmission;
            return this;
        }

        @Override
        public @NotNull Element build() {
            return new ElementImpl(from, to, rotation, shade, faces, lightEmission);
        }
    }
}
