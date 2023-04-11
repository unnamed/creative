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
package team.unnamed.creative.texture;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.Metadatable;
import team.unnamed.creative.metadata.texture.TextureMeta;
import team.unnamed.creative.metadata.villager.VillagerMeta;
import team.unnamed.creative.metadata.animation.AnimationMeta;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Minecraft texture (PNG image) in the
 * resource pack, textures are an essential part of
 * the game graphics
 *
 * <p>Textures determine the colors/skin of every block,
 * item, mob, font, etc. in the game</p>
 *
 * <p>The dimensions of the texture must be square, or the
 * height must be a multiple of the width (for animated
 * textures only, see {@link AnimationMeta})</p>
 *
 * @since 1.0.0
 */
public class Texture implements Keyed, Examinable, Metadatable {

    private final Key key;
    private final Writable data;
    private final Metadata meta;

    private Texture(
            Key key,
            Writable data,
            Metadata meta
    ) {
        this.key = requireNonNull(key, "key");
        this.data = requireNonNull(data, "data");
        this.meta = requireNonNull(meta, "meta");
    }

    /**
     * Returns this texture's key (location), a key contains
     * the texture namespace and path, so that the full texture
     * path is formatted like {@code assets/<namespace>/textures/<path>}
     *
     * <p>Example key for a texture: <pre>{@code
     *
     *   Key key = Key.key("minecraft", "block/allium.png");
     * }</pre>
     * Note that the key value must have the file full path,
     * this means that the key value must also contain the file
     * extension, if any.</p>
     *
     * @return The texture key
     */
    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * Returns the texture's PNG image data, as a
     * {@link Writable} object, never null
     *
     * @return The PNG image data
     * @since 1.0.0
     */
    public Writable data() {
        return data;
    }

    /**
     * Returns the metadata object for this texture,
     * contains some extra information for the texture
     *
     * <p>Default Minecraft client only supports {@link AnimationMeta},
     * {@link VillagerMeta} and {@link TextureMeta}</p>
     *
     * @return The metadata container
     * @since 1.0.0
     */
    @Override
    public Metadata meta() {
        return meta;
    }

    @Override
    public Texture meta(Metadata meta) {
        return toBuilder().meta(meta).build();
    }

    public Texture.Builder toBuilder() {
        return Texture.builder()
                .key(key)
                .data(data)
                .meta(meta);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("data", data),
                ExaminableProperty.of("meta", meta)
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
        Texture texture = (Texture) o;
        return key.equals(texture.key)
                && data.equals(texture.data)
                && meta.equals(texture.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data, meta);
    }

    public static Texture of(Key key, Writable data, Metadata meta) {
        return new Texture(key, data, meta);
    }

    public static Texture of(Key key, Writable data) {
        return new Texture(key, data, Metadata.EMPTY);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private Writable data;
        private Metadata meta = Metadata.EMPTY;

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = key;
            return this;
        }

        public Key key() {
            return key;
        }

        public Builder data(Writable data) {
            this.data = data;
            return this;
        }

        public Writable data() {
            return data;
        }

        public Builder meta(Metadata meta) {
            this.meta = meta;
            return this;
        }

        public Builder animationMeta(AnimationMeta animationMeta) {
            this.meta = meta.toBuilder().add(animationMeta).build();
            return this;
        }

        public Builder villagerMeta(VillagerMeta villagerMeta) {
            this.meta = meta.toBuilder().add(villagerMeta).build();
            return this;
        }

        public Builder textureMeta(TextureMeta textureMeta) {
            this.meta = meta.toBuilder().add(textureMeta).build();
            return this;
        }

        public Metadata meta() {
            return meta;
        }

        public Texture build() {
            return new Texture(key, data, meta);
        }

    }

}
