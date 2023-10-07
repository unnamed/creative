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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

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
 * <p>Note that, since Minecraft 1.19.3, by default, textures not in
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
public interface Atlas extends Keyed, Examinable {

    Key BLOCKS = Key.key("blocks");
    Key BANNER_PATTERNS = Key.key("banner_patterns");
    Key BEDS = Key.key("beds");
    Key CHESTS = Key.key("chests");
    Key SHIELD_PATTERNS = Key.key("shield_patterns");
    Key SHULKER_BOXES = Key.key("shulker_boxes");
    Key SIGNS = Key.key("signs");
    Key MOB_EFFECTS = Key.key("mob_effects");
    Key PAINTINGS = Key.key("paintings");
    Key PARTICLES = Key.key("particles");
    Key ARMOR_TRIMS = Key.key("armor_trims");
    Key DECORATED_POT = Key.key("decorated_pot");

    /**
     * Gets the {@link Atlas} key, e.g. "test:fancy", note
     * that JSON extension is not included
     *
     * @return The atlas resource location
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * Gets the list of sources for this {@link Atlas}
     *
     * @return The atlas sources
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @see AtlasSource
     * @since 1.0.0
     */
    @Unmodifiable
    @NotNull List<AtlasSource> sources();

    /**
     * Converts this atlas instance to its builder type,
     * with all the properties already set
     *
     * @return The created builder
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    @Contract("-> new")
    @NotNull Builder toBuilder();

    /**
     * Creates a new {@link Atlas} instance.
     *
     * @param key     The atlas key, doesn't include JSON extension
     * @param sources The list of sources for the atlas
     * @return The created atlas instance
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    static @NotNull Atlas of(final @NotNull Key key, final @NotNull List<AtlasSource> sources) {
        return new AtlasImpl(key, sources);
    }

    /**
     * Creates a new {@link Atlas} builder instance.
     *
     * @return The created builder
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    static @NotNull Builder builder() {
        return new AtlasImpl.BuilderImpl();
    }

    /**
     * A mutable builder for {@link Atlas} instances.
     *
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    interface Builder {

        @Nullable Key key();

        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        @Nullable List<AtlasSource> sources();

        @Contract("_ -> this")
        @NotNull Builder sources(final @NotNull List<AtlasSource> sources);

        @Contract("_ -> this")
        @NotNull Builder sources(final @NotNull AtlasSource @NotNull ... sources);

        @Contract("_ -> this")
        @NotNull Builder addSource(final @NotNull AtlasSource source);

        @Contract("-> new")
        @NotNull Atlas build();

    }

}
