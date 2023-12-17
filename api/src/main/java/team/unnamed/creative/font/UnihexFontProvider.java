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
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A {@link FontProvider} implementation that uses Unifont
 * HEX files
 *
 * @sinceMinecraft 1.20
 * @sincePackFormat 15
 * @see FontProvider#unihex()
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface UnihexFontProvider extends FontProvider {
    /**
     * Returns the location of a ZIP file containing the
     * HEX files. All the files that end with ".hex" are
     * loaded. If a file doesn't end with ".hex", it is
     * silently ignored.
     *
     * <p>The file path is relative to <code>assets/&lt;namespace&gt;/</code>
     * <i>(Note that it is not inside "fonts")</i></p>
     *
     * @return The location of the ZIP file of HEX files
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    @NotNull Key file();

    /**
     * A list of size overrides, an override contains an
     * (inclusive) range of codepoints and their custom
     * dimensions
     *
     * @return The size overrides
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    @Unmodifiable @NotNull List<SizeOverride> sizes();

    /**
     * A range of codepoints that should have a different width
     * than the auto-detected.
     *
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    interface SizeOverride extends Examinable {
        /**
         * Creates a new {@link SizeOverride} instance
         * with the given parameters.
         *
         * @param from  The start of the codepoint range (inclusive)
         * @param to    The end of the codepoint range (inclusive)
         * @param left  The left-most column of the glyph
         * @param right The right-most column of the glyph
         * @return The new instance
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.2.0
         */
        static @NotNull SizeOverride override(final int from, final int to, final int left, final int right) {
            return new UnihexFontProviderImpl.SizeOverrideImpl(from, to, left, right);
        }

        /**
         * Creates a new {@link SizeOverride} instance
         * with the given parameters.
         *
         * @param from  The start of the codepoint range (inclusive),
         *              the given string must have a single codepoint
         * @param to    The end of the codepoint range (inclusive),
         *              the given string must have a single codepoint
         * @param left  The left-most column of the glyph
         * @param right The right-most column of the glyph
         * @return The new instance
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.2.0
         */
        static @NotNull SizeOverride override(final String from, final String to, final int left, final int right) {
            return new UnihexFontProviderImpl.SizeOverrideImpl(from, to, left, right);
        }

        /**
         * The start of the codepoint range (inclusive).
         *
         * @return The start of the codepoint range
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        int from();

        /**
         * The end of the codepoint range (inclusive).
         *
         * @return The end of the codepoint range
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        int to();

        /**
         * The left-most column of the glyph in this
         * range.
         *
         * @return The left-most column of the glyph
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        int left();

        /**
         * The right-most column of the glyph in this
         * range.
         *
         * @return The right-most column of the glyph
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        int right();
    }

    /**
     * Mutable and fluent-style builder for {@link UnihexFontProvider}
     * instances
     *
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets the location of the ZIP file containing the HEX files
         * for this font provider.
         *
         * @param file The location of the ZIP file
         * @return This builder
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder file(final @NotNull Key file);

        /**
         * Sets the size overrides for this font provider.
         *
         * @param sizes The size overrides
         * @return This builder
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder sizes(final @NotNull List<SizeOverride> sizes);

        /**
         * Sets the size overrides for this font provider.
         *
         * @param sizes The size overrides
         * @return This builder
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.2.0
         */
        @Contract("_ -> this")
        default @NotNull Builder sizes(final @NotNull SizeOverride @NotNull ... sizes) {
            requireNonNull(sizes, "sizes");
            return sizes(Arrays.asList(sizes));
        }

        /**
         * Adds a new size override to this font provider.
         *
         * @param size The size override
         * @return This builder
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.2.0
         */
        @Contract("_ -> this")
        @NotNull Builder addSize(final @NotNull SizeOverride size);

        /**
         * Builds a new {@link UnihexFontProvider} instance
         * with the current state of this builder.
         *
         * @return The new instance
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull UnihexFontProvider build();
    }
}
