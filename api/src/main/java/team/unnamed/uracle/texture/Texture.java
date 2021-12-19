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
package team.unnamed.uracle.texture;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.Writable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Immutable representation of a Minecraft texture, it
 * is the appearance of an object in game, like an item,
 * character, entity or block
 *
 * @since 1.0.0
 */
public class Texture implements Examinable {

    /**
     * The actual PNG image of this texture, stored using
     * a {@link Writable} instance, to avoid maintaining
     * it loaded
     */
    private final Writable data;

    /**
     * The (general) texture meta-data, all textures can
     * have this metadata. It is then written to a '.mcmeta'
     * file in the same texture folder
     */
    @Nullable private final TextureMeta meta;

    /**
     * The animated texture meta-data, only applicable for
     * animatable textures. It is then written to a '.mcmeta'
     * file in the same texture folder
     */
    @Nullable private final AnimationMeta animation;

    /**
     * The metadata for villager textures (entity/villager
     * and entity/zombie_villager), contains additional
     * effects to apply to the hat layer
     */
    @Nullable private final VillagerMeta villager;

    private Texture(
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation,
            @Nullable VillagerMeta villager
    ) {
        this.data = requireNonNull(data, "data");
        this.meta = meta;
        this.animation = animation;
        this.villager = villager;
    }

    /**
     * Returns the actual PNG data of this texture
     *
     * @return This texture's PNG data
     */
    public Writable data() {
        return data;
    }

    /**
     * Returns this texture general metadata, or
     * null if not set
     *
     * @return This texture metadata
     */
    public @Nullable TextureMeta meta() {
        return meta;
    }

    /**
     * Returns this texture animation metadata, or
     * null if not set
     *
     * @return This texture animation metadata
     */
    public @Nullable AnimationMeta animation() {
        return animation;
    }

    /**
     * Returns this texture villager metadata, or
     * null if not set
     *
     * @return This texture villager metadata
     */
    public @Nullable VillagerMeta villager() {
        return villager;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("data", data),
                ExaminableProperty.of("meta", meta),
                ExaminableProperty.of("animation", animation),
                ExaminableProperty.of("villager", villager)
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
        return data.equals(texture.data)
                && Objects.equals(meta, texture.meta)
                && Objects.equals(animation, texture.animation)
                && Objects.equals(villager, texture.villager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta, animation, villager);
    }

    /**
     * Creates a new {@link Texture} instance using the
     * provided values
     *
     * @param data The PNG texture data
     * @param meta The optional texture meta
     * @param animation The optional animation meta
     * @return A new {@link Texture} instance
     */
    public static Texture of(
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation,
            @Nullable VillagerMeta villager
    ) {
        return new Texture(data, meta, animation, villager);
    }

    /**
     * Creates a new {@link Texture} instance without metadata,
     * using the provided values
     *
     * @param data The PNG texture data
     * @return A new {@link Texture} instance without
     * metadata
     */
    public static Texture of(Writable data) {
        return new Texture(data, null, null, null);
    }

    /**
     * Static factory method for our builder implementation
     * @return A new builder for {@link Texture} instances
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable and fluent-style builder for {@link Texture}
     * instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Writable data;
        private TextureMeta meta;
        private AnimationMeta animation;
        private VillagerMeta villager;

        private Builder() {
        }

        public Builder data(Writable data) {
            this.data = requireNonNull(data, "data");
            return this;
        }

        public Builder meta(@Nullable TextureMeta meta) {
            this.meta = meta;
            return this;
        }

        public Builder animation(@Nullable AnimationMeta animation) {
            this.animation = animation;
            return this;
        }

        public Builder villager(@Nullable VillagerMeta villager) {
            this.villager = villager;
            return this;
        }

        /**
         * Finishes building the {@link Texture} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created texture
         */
        public Texture build() {
            return new Texture(data, meta, animation, villager);
        }

    }

}