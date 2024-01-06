/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Represents a bitmap font (font that uses a set of
 * string characters and PNG images to render) for
 * Minecraft resource packs
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface BitMapFontProvider extends FontProvider {

    /**
     * Default bitmap font height
     */
    int DEFAULT_HEIGHT = 8;

    /**
     * Returns the texture location of this
     * bitmap font, must be a PNG image
     *
     * @return The font texture
     * @since 1.0.0
     */
    @NotNull Key file();

    /**
     * Returns a new bitmap font with the given
     * texture location
     *
     * @param file The new texture location
     * @return A new bitmap font
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull BitMapFontProvider file(final @NotNull Key file);

    /**
     * Returns the height of the character, measured in pixels.
     * Can be negative. This tag is separate from the area used
     * in the source texture and just rescales the displayed
     * result
     *
     * @return The font characters height
     * @since 1.0.0
     */
    int height();

    /**
     * Returns a new bitmap font with the given
     * height
     *
     * @param height The new height
     * @return A new bitmap font
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull BitMapFontProvider height(final int height);

    /**
     * Returns the font characters ascent, measured in
     * pixels, this value as a vertical shift to displayed
     * result
     *
     * @return The font characters ascent
     * @since 1.0.0
     */
    int ascent();

    /**
     * Returns a new bitmap font with the given
     * ascent
     *
     * @param ascent The new ascent
     * @return A new bitmap font
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull BitMapFontProvider ascent(final int ascent);

    /**
     * Returns a list of strings containing the characters replaced by
     * this provider, as well as their order within the texture. All
     * elements must describe the same number of characters. The texture
     * is split into one equally sized row for each element of this list.
     * Each row is split into one equally sized character for each character
     * within one list element.
     *
     * @return The font characters
     * @since 1.0.0
     */
    @Unmodifiable @NotNull List<String> characters();

    /**
     * Returns a new bitmap font with the given
     * characters
     *
     * @param characters The new characters
     * @return A new bitmap font
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull BitMapFontProvider characters(final @NotNull List<String> characters);

    /**
     * Converts this {@link BitMapFontProvider} instance
     * to a {@link BitMapFontProvider.Builder} instance
     * with the same values as this font provider.
     *
     * @return A new builder with the same values as this font provider
     * @since 1.0.0
     */
    @Contract("-> new")
    default @NotNull BitMapFontProvider.Builder toBuilder() {
        return FontProvider.bitMap()
                .file(file())
                .height(height())
                .ascent(ascent())
                .characters(characters());
    }

    /**
     * Mutable and fluent-style builder for {@link BitMapFontProvider}
     * instances
     *
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets the font texture location
         *
         * @param file The font texture location
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder file(final @NotNull Key file);

        /**
         * Sets the font height
         *
         * @param height The font height
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder height(final int height);

        /**
         * Sets the font ascent
         *
         * @param ascent The font ascent
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder ascent(final int ascent);

        /**
         * Sets the font characters
         *
         * @param characters The font characters
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder characters(final @NotNull List<String> characters);

        /**
         * Sets the font characters
         *
         * @param characters The font characters
         * @return This builder instance
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder characters(final @NotNull String @NotNull ... characters) {
            requireNonNull(characters, "characters");
            return characters(Arrays.asList(characters));
        }

        /**
         * Builds a new {@link BitMapFontProvider} instance.
         *
         * @return A new {@link BitMapFontProvider} instance
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull BitMapFontProvider build();
    }
}
