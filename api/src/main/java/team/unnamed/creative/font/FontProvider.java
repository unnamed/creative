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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a Minecraft Resource Pack Font provider,
 * a part of a {@link Font}
 *
 * @see Font
 * @since 1.0.0
 */
public interface FontProvider extends Examinable {
    /**
     * Creates a new bit-map font from the provided values
     *
     * @param file       The bit-map texture location in PNG format
     * @param height     The characters height
     * @param ascent     The characters ascent
     * @param characters The characters string
     * @return A new bit-map font
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> new")
    static @NotNull BitMapFontProvider bitMap(final @NotNull Key file, final int height, final int ascent, final @NotNull List<String> characters) {
        return new BitMapFontProviderImpl(file, height, ascent, characters);
    }

    /**
     * Static factory method for {@link BitMapFontProvider}
     * builder implementation
     *
     * @return A new builder for {@link BitMapFontProvider} instances
     * @since 1.0.0
     */
    @Contract("-> new")
    static BitMapFontProvider.Builder bitMap() {
        return new BitMapFontProviderImpl.BuilderImpl();
    }

    /**
     * Creates a new legacy unicode font from the provided
     * values, this font type should not be used since it
     * is deprecated (now removed since Minecraft 1.20) and
     * is only prioritized when the "Force Unicode Font" option
     * is turned on
     *
     * @param sizes    Location to the file that specifies the
     *                 character sizes
     * @param template Location of the file that specifies
     *                 the character textures, it is a string
     *                 template and MUST contain a single '%s'
     *                 that will be replaced by the unicode page
     * @return A new {@link LegacyUnicodeFontProvider} font
     * @since 1.0.0
     * @deprecated Removed on Minecraft 1.20
     */
    @Deprecated
    static LegacyUnicodeFontProvider legacyUnicode(Key sizes, String template) {
        return new LegacyUnicodeFontProvider(sizes, template);
    }

    /**
     * Creates a new SpaceFontProvider builder
     * This font provider consists of a map of codepoints (characters) and integers (how many pixels to shift by)
     * If a character is used in a space font provider, it is not rendered, and is instead used as spacing.
     * You can not shift vertically with this font provider, for vertical offsets use {@link BitMapFontProvider}
     *
     * @return the newly created builder
     * @sincePackFormat 9
     */
    static SpaceFontProvider.Builder space() {
        return new SpaceFontProvider.Builder();
    }


    /**
     * Creates a new SpaceFontProvider instance from provided arguments
     * This font provider consists of a map of codepoints (characters) and integers (how many pixels to shift by)
     * If a character is used in a space font provider, it is not rendered, and is instead used as spacing.
     * You can not shift vertically with this font provider, for vertical offsets use {@link BitMapFontProvider}
     *
     * @return the newly created builder
     * @sincePackFormat 9
     */
    static SpaceFontProvider space(Map<String, Integer> advances) {
        return new SpaceFontProvider(advances);
    }

    /**
     * Static factory method for {@link TrueTypeFontProvider}
     * builder implementation
     *
     * @return A new builder for {@link TrueTypeFontProvider} instances
     * @since 1.0.0
     */
    static TrueTypeFontProvider.Builder trueType() {
        return new TrueTypeFontProvider.Builder();
    }

    /**
     * Creates a new {@link ReferenceFontProvider} instance that
     * references another font specified by the given {@code id},
     * which is the font key.
     *
     * @param id The referenced font key
     * @return A new instance of {@link ReferenceFontProvider}
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    static @NotNull ReferenceFontProvider reference(final @NotNull Key id) {
        return new ReferenceFontProviderImpl(id);
    }

    /**
     * Creates a new {@link ReferenceFontProvider} instance that
     * references another font specified by the given {@code font}.
     *
     * @param font The referenced font
     * @return A new instance of {@link ReferenceFontProvider}
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.2.0
     */
    static @NotNull ReferenceFontProvider reference(final @NotNull Font font) {
        return reference(font.key());
    }

    /**
     * Creates a new instance of {@link UnihexFontProvider}
     *
     * @param file  The zip file containing the HEX files
     * @param sizes The size overrides
     * @return A new font provider instance
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    @Contract("_, _ -> new")
    static @NotNull UnihexFontProvider unihex(final @NotNull Key file, final @NotNull List<UnihexFontProvider.SizeOverride> sizes) {
        return new UnihexFontProviderImpl(file, sizes);
    }

    /**
     * Creates a new builder for {@link UnihexFontProvider}
     * instances
     *
     * @return A new builder instance
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    @Contract("-> new")
    static @NotNull UnihexFontProvider.Builder unihex() {
        return new UnihexFontProviderImpl.BuilderImpl();
    }
}
