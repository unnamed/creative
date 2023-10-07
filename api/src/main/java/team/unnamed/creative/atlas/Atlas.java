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

package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.MoreCollections;
import team.unnamed.creative.util.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents an atlas, atlases are configuration files that control
 * which images are included in texture atlases.
 *
 * <p>When the game loads all textures used by block and item models,
 * the textures need to be stitched (merged) into a single image, called
 * the "atlas".</p>
 *
 * <p>Individual textures that are stitched onto the atlas are called
 * "sprites".</p>
 *
 * <p>Note that, since Minecraft M.19.3, by default, textures not in
 * "item/" or "block/" directories are no longer automatically recognized
 * and will fail to load.</p>
 *
 * <p>See the <a href="https://www.minecraft.net/en-us/article/minecraft-java-edition-1-19-3">1.19.3 change-log</a>
 * and the atlas structure in the resource-pack <a href="https://minecraft.fandom.com/wiki/Resource_pack#Atlases">here</a></p>
 *
 * @sincePackFormat 12
 * @sinceMinecraft 1.19.3
 * @since 1.0.0
 */
public class Atlas implements Keyed, Examinable {

    public static final Key BLOCKS = Key.key("blocks");
    public static final Key BANNER_PATTERNS = Key.key("banner_patterns");
    public static final Key BEDS = Key.key("beds");
    public static final Key CHESTS = Key.key("chests");
    public static final Key SHIELD_PATTERNS = Key.key("shield_patterns");
    public static final Key SHULKER_BOXES = Key.key("shulker_boxes");
    public static final Key SIGNS = Key.key("signs");
    public static final Key MOB_EFFECTS = Key.key("mob_effects");
    public static final Key PAINTINGS = Key.key("paintings");
    public static final Key PARTICLES = Key.key("particles");
    public static final Key ARMOR_TRIMS = Key.key("armor_trims");
    public static final Key DECORATED_POT = Key.key("decorated_pot");

    private final Key key;
    private final List<AtlasSource> sources;

    private Atlas(Key key, List<AtlasSource> sources) {
        Validate.isNotNull(key, "key");
        Validate.isNotNull(sources);
        this.key = key;
        this.sources = MoreCollections.immutableListOf(sources);
    }

    /**
     * Gets the {@link Atlas} key, e.g. "test:fancy", note
     * that JSON extension is not included
     *
     * @return The atlas resource location
     * @since 1.0.0
     */
    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * Gets the list of sources for this {@link Atlas}
     *
     * @return The atlas sources
     * @see AtlasSource
     * @since 1.0.0
     */
    public @Unmodifiable List<AtlasSource> sources() {
        return sources;
    }

    /**
     * Converts this atlas instance to its builder type,
     * with all the properties already set
     *
     * @return The created builder
     * @since 1.0.0
     */
    public Builder toBuilder() {
        return builder().key(key).sources(sources);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("sources", sources)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atlas atlas = (Atlas) o;
        if (!key.equals(atlas.key)) return false;
        return sources.equals(atlas.sources);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + sources.hashCode();
        return result;
    }

    /**
     * Creates a new {@link Atlas} instance.
     *
     * @param key     The atlas key, doesn't include JSON extension
     * @param sources The list of sources for the atlas
     * @return The created atlas instance
     * @since 1.0.0
     */
    public static Atlas of(Key key, List<AtlasSource> sources) {
        return new Atlas(key, sources);
    }

    /**
     * Creates a new {@link Atlas} builder instance.
     *
     * @return The created builder
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private List<AtlasSource> sources;

        private Builder() {
        }

        public Key key() {
            return key;
        }

        public Builder key(Key key) {
            this.key = Validate.isNotNull(key, "key");
            return this;
        }

        public List<AtlasSource> sources() {
            return sources;
        }

        public Builder sources(List<AtlasSource> sources) {
            this.sources = new ArrayList<>(sources);
            return this;
        }

        public Builder sources(AtlasSource... sources) {
            this.sources = new ArrayList<>(Arrays.asList(sources));
            return this;
        }

        public Builder addSource(AtlasSource source) {
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(source);
            return this;
        }

        public Atlas build() {
            return new Atlas(key, sources);
        }

    }

}
