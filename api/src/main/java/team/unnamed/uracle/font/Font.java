/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;

import java.util.List;

/**
 * Represents a Minecraft Resource Pack Font provider,
 * can be bit-map, true type font, or (LEGACY) unicode
 * font
 *
 * @since 1.0.0
 */
public interface Font extends Examinable {

    /**
     * Creates a new bit-map font from the provided values
     *
     * @param file The bit-map texture location in PNG format
     * @param height The characters height
     * @param ascent The characters ascent
     * @param characters The characters string
     * @return A new bit-map font
     * @since 1.0.0
     */
    static BitMapFont bitMap(
            Key file,
            int height,
            int ascent,
            List<String> characters
    ) {
        return new BitMapFont(file, height, ascent, characters);
    }

    /**
     * Static factory method for {@link BitMapFont}
     * builder implementation
     *
     * @return A new builder for {@link BitMapFont} instances
     * @since 1.0.0
     */
    static BitMapFont.Builder bitMap() {
        return new BitMapFont.Builder();
    }

    /**
     * Creates a new legacy unicode font from the provided
     * values, this font type should not be used since it
     * is deprecated and is only prioritized when the "Force
     * Unicode Font" option is turned on
     *
     * @param sizes Location to the file that specifies the
     *              character sizes
     * @param template Location of the file that specifies
     *                 the character textures
     * @return A new {@link LegacyUnicodeFont} font
     * @since 1.0.0
     */
    static LegacyUnicodeFont legacyUnicode(Key sizes, Key template) {
        return new LegacyUnicodeFont(sizes, template);
    }

    /**
     * Static factory method for {@link TrueTypeFont}
     * builder implementation
     *
     * @return A new builder for {@link TrueTypeFont} instances
     * @since 1.0.0
     */
    static TrueTypeFont.Builder trueType() {
        return new TrueTypeFont.Builder();
    }

}
