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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.Metadatable;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.metadata.texture.TextureMeta;
import team.unnamed.creative.metadata.villager.VillagerMeta;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

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
@ApiStatus.NonExtendable
public interface Texture extends ResourcePackPart, Keyed, Examinable, Metadatable {
    /**
     * Creates a texture.
     *
     * @param key  The texture key
     * @param data The texture data
     * @return The created texture
     * @since 1.1.0
     */
    static @NotNull Texture texture(final @NotNull Key key, final @NotNull Writable data) {
        return texture(key, data, Metadata.empty());
    }

    /**
     * Creates a texture.
     *
     * @param key  The texture key
     * @param data The texture data
     * @param meta The texture metadata
     * @return The created texture
     * @since 1.1.0
     */
    static @NotNull Texture texture(final @NotNull Key key, final @NotNull Writable data, final @NotNull Metadata meta) {
        return new TextureImpl(key, data, meta);
    }

    /**
     * Creates a new builder for textures.
     *
     * @return The created builder
     * @since 1.1.0
     */
    static @NotNull Builder texture() {
        return new TextureImpl.BuilderImpl();
    }

    /**
     * Creates a texture.
     *
     * @param key  The texture key
     * @param data The texture data
     * @return The created texture
     * @since 1.0.0
     * @deprecated Use {@link Texture#texture(Key, Writable)} instead,
     * it's better for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Texture of(final @NotNull Key key, final @NotNull Writable data) {
        return of(key, data, Metadata.empty());
    }

    /**
     * Creates a texture.
     *
     * @param key  The texture key
     * @param data The texture data
     * @param meta The texture metadata
     * @return The created texture
     * @since 1.0.0
     * @deprecated Use {@link Texture#texture(Key, Writable)} instead,
     * it's better for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Texture of(final @NotNull Key key, final @NotNull Writable data, final @NotNull Metadata meta) {
        return new TextureImpl(key, data, meta);
    }

    /**
     * Creates a new builder for textures.
     *
     * @return The created builder
     * @since 1.0.0
     * @deprecated Use {@link Texture#texture()} instead,
     * it's better for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Builder builder() {
        return new TextureImpl.BuilderImpl();
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
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * Returns an updated texture with the given key.
     *
     * @param key The texture key
     * @return The new texture
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    default @NotNull Texture key(final @NotNull Key key) {
        return toBuilder().key(key).build();
    }

    /**
     * Returns the texture's PNG image data, as a
     * {@link Writable} object, never null
     *
     * @return The PNG image data
     * @since 1.0.0
     */
    @NotNull Writable data();

    /**
     * Returns an updated texture with the given data.
     *
     * @param data The texture data
     * @return The new texture
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    default @NotNull Texture data(final @NotNull Writable data) {
        return toBuilder().data(data).build();
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
    @NotNull Metadata meta();

    /**
     * Returns an updated texture with the given metadata.
     *
     * @param meta The new metadata
     * @return The new texture
     * @since 1.0.0
     */
    @Override
    @Contract(value = "_ -> new", pure = true)
    default @NotNull Texture meta(final @NotNull Metadata meta) {
        return toBuilder().meta(meta).build();
    }

    /**
     * Returns a new texture builder with all properties
     * from this texture.
     *
     * @return The created builder
     * @since 1.0.0
     */
    @Contract("-> new")
    default @NotNull Builder toBuilder() {
        return builder()
                .key(this.key())
                .data(this.data())
                .meta(this.meta());
    }

    /**
     * Adds this texture to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.texture(this);
    }

    /**
     * A builder for texture instances.
     *
     * <p>Key and data are required, all other options
     * are optional.</p>
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the texture key.
         *
         * @param key The texture key
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        /**
         * Gets the texture key.
         *
         * @return The texture key
         * @since 1.0.0
         */
        @Nullable Key key();

        /**
         * Sets the texture data.
         *
         * @param data The texture data
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder data(final @NotNull Writable data);

        /**
         * Gets the texture data.
         *
         * @return The texture data
         * @since 1.0.0
         */
        @Nullable Writable data();

        /**
         * Sets the texture metadata.
         *
         * @param meta The texture metadata
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder meta(final @NotNull Metadata meta);

        /**
         * Gets the texture metadata.
         *
         * @return The texture metadata
         * @since 1.0.0
         */
        @NotNull Metadata meta();

        /**
         * Adds a animation metadata part to the texture metadata.
         *
         * @param animationMeta The animation metadata
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder animationMeta(final @NotNull AnimationMeta animationMeta) {
            return meta(meta().toBuilder().add(animationMeta).build());
        }

        /**
         * Adds a villager metadata part to the texture metadata.
         *
         * @param villagerMeta The villager metadata
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder villagerMeta(final @NotNull VillagerMeta villagerMeta) {
            return meta(meta().toBuilder().add(villagerMeta).build());
        }

        /**
         * Adds a texture metadata part to the texture metadata.
         *
         * @param textureMeta The texture metadata
         * @return This builder
         * @since 1.0.0
         */
        default @NotNull Builder textureMeta(final @NotNull TextureMeta textureMeta) {
            return meta(meta().toBuilder().add(textureMeta).build());
        }

        /**
         * Builds the texture instance.
         *
         * @return The built texture
         * @since 1.0.0
         */
        @NotNull Texture build();
    }
}
