/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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

public class LegacyUnicodeFont implements Font {

    /**
     * The resource location inside assets/&lt;namespace&gt;/font describing a
     * binary file describing the horizontal start and end positions for
     * each character from 0 to 15. The file extension of the target file
     * should be .bin
     */
    private final Key sizes;

    /**
     * The resource location inside assets/&lt;namespace&gt;/textures that leads to
     * the texture files that should be used for this provider. The game replaces
     * %s from the value of this tag with the first two characters of the hex code
     * of the replaced characters, so a single provider of this type can point
     * into multiple texture files.
     */
    private final Key template;

    public LegacyUnicodeFont(Key sizes, Key template) {
        this.sizes = sizes;
        this.template = template;
    }

    public Key getSizes() {
        return sizes;
    }

    public Key getTemplate() {
        return template;
    }

}
