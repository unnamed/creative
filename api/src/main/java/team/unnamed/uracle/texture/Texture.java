package team.unnamed.uracle.texture;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;
import team.unnamed.uracle.Writable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Immutable representation of a Minecraft texture, it
 * is the appearance of an object in game, like an item,
 * character, entity or block
 *
 * @since 1.0.0
 */
public class Texture implements Element, Keyed {

    /**
     * The location of this texture data, the {@code data}
     * property will be written there. Base path in this
     * context is assets/&lt;namespace&gt;/textures, this
     * path includes the file extension (.PNG)
     */
    private final Key key;

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
            Key key,
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation
    ) {
        this.key = requireNonNull(key, "key");
        this.data = requireNonNull(data, "data");
        this.meta = meta;
        this.animation = animation;
    }

    /**
     * Returns the resource location of this texture
     * using assets/&lt;namespace&gt;/textures as base
     * path
     *
     * @return This texture resource location (or key)
     */
    @Override
    public @NotNull Key key() {
        return key;
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

    /**
     * Writes this texture information into the given
     * {@code writer}, may contain more than one file
     * when required
     *
     * @param writer The target tree writer
     */
    @Override
    public void write(TreeWriter writer) {
        // write the actual texture PNG image
        try (TreeWriter.Context context = writer.join(key, "textures")) {
            data.write(context);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write texture", e);
        }

        boolean hasMeta = meta != null;
        boolean hasAnimation = animation != null;

        // write the metadata
        if (hasMeta || hasAnimation) {
            try (TreeWriter.Context context = writer.join(key, "textures", ".mcmeta")) {
                context.startObject();

                if (hasMeta) {
                    context.writeKey("texture");
                    context.writePart(meta);
                    if (hasAnimation) {
                        // write separator for next
                        // object
                        context.writeSeparator();
                    }
                }

                if (hasAnimation) {
                    context.writeKey("animation");
                    context.writePart(animation);
                }

                context.endObject();
            }
        }
    }

    @Override
    public String toString() {
        return "Texture{" +
                "key=" + key +
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
        return key.equals(texture.key)
                && data.equals(texture.data)
                && Objects.equals(meta, texture.meta)
                && Objects.equals(animation, texture.animation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data, meta, animation);
    }

    /**
     * Creates a new {@link Texture} instance using the
     * provided values
     *
     * @param key The texture location using
     *            assets/&lt;namespace&gt;/textures
     *            as base path
     * @param data The PNG texture data
     * @param meta The optional texture meta
     * @param animation The optional animation meta
     * @return A new {@link Texture} instance
     */
    public static Texture of(
            Key key,
            Writable data,
            @Nullable TextureMeta meta,
            @Nullable AnimationMeta animation
    ) {
        return new Texture(key, data, meta, animation);
    }

    /**
     * Creates a new {@link Texture} instance without metadata,
     * using the provided values
     *
     * @param key The texture location using
     *            assets/&lt;namespace&gt;/textures
     *            as base path
     * @param data The PNG texture data
     * @return A new {@link Texture} instance without
     * metadata
     */
    public static Texture of(
            Key key,
            Writable data
    ) {
        return new Texture(key, data, null, null);
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

        private Key key;
        private Writable data;
        private TextureMeta meta;
        private AnimationMeta animation;

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = requireNonNull(key, "key");
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
            return new Texture(key, data, meta, animation);
        }

    }

}