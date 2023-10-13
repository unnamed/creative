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
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.texture.TextureUV;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ElementFaceImpl implements ElementFace {

    private final @Nullable TextureUV uv;
    private final String texture;
    private final @Nullable CubeFace cullFace;
    private final int rotation;
    private final int tintIndex;

    ElementFaceImpl(
            final @Nullable TextureUV uv,
            final @NotNull String texture,
            final @Nullable CubeFace cullFace,
            final int rotation,
            final int tintIndex
    ) {
        this.uv = uv;
        this.texture = requireNonNull(texture, "texture");
        this.cullFace = cullFace;
        this.rotation = rotation;
        this.tintIndex = tintIndex;
        validate();
    }

    private void validate() {
        if (rotation % 90 != 0 || rotation < 0 || rotation > 270)
            throw new IllegalArgumentException("Rotation must be a positive multiple of 90");
    }

    @Override
    public @Nullable TextureUV uv0() {
        return uv;
    }

    @Override
    public @NotNull String texture() {
        return texture;
    }

    @Override
    public @Nullable CubeFace cullFace() {
        return cullFace;
    }

    @Override
    public int rotation() {
        return rotation;
    }

    @Override
    public int tintIndex() {
        return tintIndex;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("uv", uv),
                ExaminableProperty.of("texture", texture),
                ExaminableProperty.of("cullFace", cullFace),
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("tintIndex", tintIndex)
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
        ElementFaceImpl that = (ElementFaceImpl) o;
        return rotation == that.rotation
                && Objects.equals(uv, that.uv)
                && texture.equals(that.texture)
                && cullFace == that.cullFace
                && tintIndex == that.tintIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uv, texture, cullFace, rotation, tintIndex);
    }

    static final class BuilderImpl implements Builder {

        private TextureUV uv;
        private String texture;
        private CubeFace cullFace;
        private int rotation = DEFAULT_ROTATION;
        private int tintIndex = DEFAULT_TINT_INDEX;

        @Override
        public @NotNull Builder uv(final @Nullable TextureUV uv) {
            this.uv = uv;
            return this;
        }

        @Override
        public @NotNull Builder texture(@NotNull String texture) {
            this.texture = requireNonNull(texture, "texture");
            return this;
        }

        @Override
        public @NotNull Builder cullFace(@Nullable CubeFace cullFace) {
            this.cullFace = cullFace;
            return this;
        }

        @Override
        public @NotNull Builder rotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        @Override
        public @NotNull Builder tintIndex(int tintIndex) {
            this.tintIndex = tintIndex;
            return this;
        }

        @Override
        public @NotNull ElementFace build() {
            return new ElementFaceImpl(uv, texture, cullFace, rotation, tintIndex);
        }
    }
}
