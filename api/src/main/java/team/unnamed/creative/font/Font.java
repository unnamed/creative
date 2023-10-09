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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a resource-pack font.
 *
 * <p>A font is composed by a {@link FontProvider font providers},
 * providers are the responsible for mapping characters to their
 * respective character texture.</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface Font extends Keyed, Examinable {
    Key MINECRAFT_DEFAULT = Key.key("default");
    Key MINECRAFT_ALT = Key.key("alt");
    Key MINECRAFT_ILLAGERALT = Key.key("illageralt");
    Key MINECRAFT_UNIFORM = Key.key("uniform");

    /**
     * Creates a new {@link Font} instance from
     * the given provider list.
     *
     * @param key       The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.1.0
     */
    static @NotNull Font font(final @NotNull Key key, final @NotNull List<FontProvider> providers) {
        return new FontImpl(key, providers);
    }

    /**
     * Creates a new {@link Font} instance from
     * the given providers
     *
     * @param key       The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.1.0
     */
    static @NotNull Font font(final @NotNull Key key, final @NotNull FontProvider @NotNull ... providers) {
        return of(key, Arrays.asList(providers));
    }

    /**
     * Creates a new builder for {@link Font} instances.
     *
     * @return The created builder
     * @since 1.1.0
     */
    static @NotNull Builder font() {
        return new FontImpl.BuilderImpl();
    }

    /**
     * Creates a new {@link Font} instance from
     * the given provider list
     *
     * @param key       The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.0.0
     * @deprecated Use {@link Font#font} as it is better
     * for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Font of(final @NotNull Key key, final @NotNull List<FontProvider> providers) {
        return font(key, providers);
    }

    /**
     * Creates a new {@link Font} instance from
     * the given providers
     *
     * @param key       The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.0.0
     * @deprecated Use {@link Font#font} as it is better
     * for static imports
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Font of(final @NotNull Key key, final @NotNull FontProvider @NotNull ... providers) {
        return font(key, Arrays.asList(providers));
    }

    /**
     * Returns the font key.
     *
     * @return The font key
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * Returns an updated {@link Font} instance
     * with the given key.
     *
     * @param key The font key
     * @return An updated {@link Font} instance
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull Font key(final @NotNull Key key);

    /**
     * Returns a list of font providers
     * that are merged onto this font
     *
     * @return The font providers
     * @since 1.0.0
     */
    @NotNull List<FontProvider> providers();

    /**
     * Returns an updated {@link Font} instance
     * with the given providers.
     *
     * @param providers The font providers
     * @return An updated {@link Font} instance
     * @since 1.1.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull Font providers(final @NotNull List<FontProvider> providers);

    /**
     * Returns a new {@link Builder} instance
     * with the same values as this font.
     *
     * @return A new {@link Builder} instance
     * @since 1.1.0
     */
    default @NotNull Builder toBuilder() {
        return font()
                .key(this.key())
                .providers(this.providers());
    }

    /**
     * A builder for {@link Font} instances.
     *
     * @since 1.1.0
     */
    interface Builder {
        /**
         * Sets the font key.
         *
         * @param key The font key
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        /**
         * Sets the font providers.
         *
         * @param providers The font providers
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        @NotNull Builder providers(final @NotNull List<FontProvider> providers);

        /**
         * Sets the font providers.
         *
         * @param providers The font providers
         * @return This builder
         * @since 1.1.0
         */
        @NotNull Builder providers(final @NotNull FontProvider @NotNull ... providers);

        /**
         * Adds a font provider to the font.
         *
         * @param provider The font provider
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        @NotNull Builder provider(final @NotNull FontProvider provider);

        /**
         * Builds the font.
         *
         * @return The font
         * @since 1.1.0
         */
        @NotNull Font build();
    }
}
