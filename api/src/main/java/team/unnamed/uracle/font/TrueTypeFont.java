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
import team.unnamed.uracle.TreeWriter;
import team.unnamed.uracle.Vector2Float;

import java.util.List;

public class TrueTypeFont implements Font {

    /**
     * The resource location of the TrueType font file within
     * assets/&lt;namespace&gt;/font
     */
    private final Key file;

    /**
     * The distance by which the characters of this provider are
     * shifted. X: Left shift, Y: Downwards shift
     */
    private final Vector2Float shift;

    /**
     * Font size to render at
     */
    private final float size;

    /**
     * Resolution to render at
     */
    private final float oversample;

    /**
     * List of strings to exclude
     */
    private final List<String> skip;

    public TrueTypeFont(
            Key file,
            Vector2Float shift,
            float size,
            float oversample,
            List<String> skip
    ) {
        this.file = file;
        this.shift = shift;
        this.size = size;
        this.oversample = oversample;
        this.skip = skip;
    }

    @Override
    public void write(TreeWriter writer) {

    }

    @Override
    public void write(TreeWriter.Context context) {

    }

    @Override
    public Type type() {
        return null;
    }
}
