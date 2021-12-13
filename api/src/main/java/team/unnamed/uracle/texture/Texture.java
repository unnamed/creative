package team.unnamed.uracle.texture;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.ResourceLocation;
import team.unnamed.uracle.Writable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Immutable representation of a Minecraft texture, it
 * is the appearance of an object in game, like an item,
 * character, entity or block
 *
 * @since 1.0.0
 */
public class Texture {

    /**
     * The location of this texture data, the {@code data}
     * property will be written there. Base path in this
     * context is assets/&lt;namespace&gt;/textures
     */
    private final ResourceLocation location;

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

    private Texture(
            ResourceLocation location,
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation
    ) {
        this.location = requireNonNull(location, "location");
        this.data = requireNonNull(data, "data");
        this.meta = meta;
        this.animation = animation;
    }

    /**
     * Returns the resource location of this texture
     * using assets/&lt;namespace&gt;/textures as base
     * path
     *
     * @return This texture resource location
     */
    public ResourceLocation getLocation() {
        return location;
    }

    /**
     * Returns the actual PNG data of this texture
     *
     * @return This texture's PNG data
     */
    public Writable getData() {
        return data;
    }

    /**
     * Returns this texture general metadata, or
     * null if not set
     *
     * @return This texture metadata
     */
    public @Nullable TextureMeta getMeta() {
        return meta;
    }

    /**
     * Returns this texture animation metadata, or
     * null if not set
     *
     * @return This texture animation metadata
     */
    public @Nullable AnimationMeta getAnimation() {
        return animation;
    }

    @Override
    public String toString() {
        return "Texture{" +
                "location=" + location +
                ", data=" + data +
                ", meta=" + meta +
                ", animation=" + animation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Texture texture = (Texture) o;
        return location.equals(texture.location)
                && data.equals(texture.data)
                && Objects.equals(meta, texture.meta)
                && Objects.equals(animation, texture.animation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, data, meta, animation);
    }

    /**
     * Creates a new {@link Texture} instance using the
     * provided values
     *
     * @param location The texture location using
     *                 assets/&lt;namespace&gt;/textures
     *                 as base path
     * @param data The PNG texture data
     * @param meta The optional texture meta
     * @param animation The optional animation meta
     * @return A new {@link Texture} instance
     */
    public static Texture of(
            ResourceLocation location,
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation
    ) {
        return new Texture(location, data, meta, animation);
    }

    /**
     * Creates a new {@link Texture} instance without metadata,
     * using the provided values
     *
     * @param location The texture location using
     *                 assets/&lt;namespace&gt;/textures
     *                 as base path
     * @param data The PNG texture data
     * @return A new {@link Texture} instance without
     * metadata
     */
    public static Texture of(
            ResourceLocation location,
            Writable data
    ) {
        return new Texture(location, data, null, null);
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

        private ResourceLocation location;
        private Writable data;
        private TextureMeta meta;
        private AnimationMeta animation;

        private Builder() {
        }

        public Builder location(ResourceLocation location) {
            this.location = requireNonNull(location, "location");
            return this;
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

        /**
         * Finishes building the {@link Texture} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created texture
         */
        public Texture build() {
            return new Texture(location, data, meta, animation);
        }

    }

}