/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.base.CubeFace;
import team.unnamed.uracle.base.Vector4Int;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines the properties of a {@link CubeFace}
 * from a {@link Element}
 *
 * @since 1.0.0
 */
public class ElementFace implements SerializableResource {

    @Nullable private final Vector4Int uv;
    private final String texture;
    @Nullable private final CubeFace cullFace;
    private final int rotation;
    @Nullable private final Integer tintIndex;

    private ElementFace(
            @Nullable Vector4Int uv,
            String texture,
            @Nullable CubeFace cullFace,
            int rotation,
            @Nullable Integer tintIndex
    ) {
        this.uv = uv;
        this.texture = requireNonNull(texture, "texture");
        this.cullFace = cullFace;
        this.rotation = rotation;
        this.tintIndex = tintIndex;
    }

    /**
     * Returns the area of the texture to
     * use according to the scheme [x1, y1, x2, y2]
     *
     * <p>The texture behavior is inconsistent if
     * UV extends below 0 or above 16. If the numbers
     * of x1 and x2 are swapped the texture flips</p>
     *
     * <p>UV is optional, and if not supplied it automatically
     * generates based on the element's position.</p>
     *
     * @return The texture area to use
     */
    public @Nullable Vector4Int uv() {
        return uv;
    }

    /**
     * Returns this face's texture in variable
     * form prepended with a "#"
     *
     * @return The face texture
     */
    public String texture() {
        return texture;
    }

    /**
     * Returns whether a face does not need to be rendered
     * when there is a block touching it in the specified
     * position. It also determines the side of the block to
     * use the light level from for lighting the face
     *
     * @return The element cull face
     */
    public @Nullable CubeFace cullFace() {
        return cullFace;
    }

    /**
     * Returns this face texture rotation, it rotates the texture
     * by the specified number of degrees. Can be 0, 90, 180, or
     * 270.
     *
     * <p>Rotation does not affect which part of the texture is
     * used. Instead, it amounts to permutation of the selected
     * texture vertexes (selected implicitly, or explicitly though
     * uv)</p>
     *
     * @return The face texture rotation
     */
    public int rotation() {
        return rotation;
    }

    /**
     * Determines whether to tint the texture using a hardcoded tint
     * index.
     *
     * <p>The default is not using tints (-1 for blocks, unset for
     * items), and for item elements, any number caused it to use tint</p>
     *
     * @return The face tint index
     */
    public @Nullable Integer tintIndex() {
        return tintIndex;
    }

    @Override
    public void serialize(AssetWriter writer) {
        writer.startObject();
        if (uv != null) {
            writer.key("uv").value(uv);
        }
        writer.key("texture").value(texture);
        if (cullFace != null) {
            writer.key("cullface").value(cullFace.name().toLowerCase(Locale.ROOT));
        }
        if (rotation != 0) {
            writer.key("rotation").value(rotation);
        }
        if (tintIndex != null) {
            writer.key("tintindex").value(tintIndex);
        }
        writer.endObject();
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
        ElementFace that = (ElementFace) o;
        return rotation == that.rotation
                && Objects.equals(uv, that.uv)
                && texture.equals(that.texture)
                && cullFace == that.cullFace
                && Objects.equals(tintIndex, that.tintIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uv, texture, cullFace, rotation, tintIndex);
    }

    /**
     * Creates a new {@link ElementFace} instance from
     * the provided values
     *
     * @param uv The texture area to use, will be automatically
     *           generated by the client if not set
     * @param texture The texture name
     * @param cullFace The element face cull face
     * @param rotation The face rotation, must be multiple
     *                 of 90 and be less than 360
     * @param tintIndex The face tint index
     * @return The recently created {@link ElementFace} instance
     * @since 1.0.0
     */
    public static ElementFace of(
            @Nullable Vector4Int uv,
            String texture,
            @Nullable CubeFace cullFace,
            int rotation,
            @Nullable Integer tintIndex
    ) {
        return new ElementFace(uv, texture, cullFace, rotation, tintIndex);
    }

    /**
     * Static factory method for creating a new
     * instance of our builder implementation,
     * which eases the creation of {@link ElementFace}
     * instances
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for creating
     * {@link ElementFace} instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Vector4Int uv;
        private String texture;
        private CubeFace cullFace;
        private int rotation;
        private Integer tintIndex;

        private Builder() {
        }

        public Builder uv(@Nullable Vector4Int uv) {
            this.uv = uv;
            return this;
        }

        public Builder texture(String texture) {
            this.texture = requireNonNull(texture, "texture");
            return this;
        }

        public Builder cullFace(@Nullable CubeFace cullFace) {
            this.cullFace = cullFace;
            return this;
        }

        public Builder rotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder tintIndex(@Nullable Integer tintIndex) {
            this.tintIndex = tintIndex;
            return this;
        }

        /**
         * Finishes building the {@link ElementFace}
         * instance with the previously set values
         *
         * @return The face instance
         */
        public ElementFace build() {
            return new ElementFace(uv, texture, cullFace, rotation, tintIndex);
        }

    }

}
