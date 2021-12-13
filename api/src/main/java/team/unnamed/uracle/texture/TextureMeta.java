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
package team.unnamed.uracle.texture;

import team.unnamed.uracle.Element;
import team.unnamed.uracle.TreeWriter;

public class TextureMeta implements Element.Part {

    /**
     * Causes the texture to blur when viewed
     * from close up
     */
    private final boolean blur;

    /**
     * Causes the texture to stretch instead of
     * tiling in cases where it otherwise would,
     * such as on the shadow
     */
    private final boolean clamp;

    /**
     * Custom mipmap values for the texture
     */
    private final int[] mipmaps;

    public TextureMeta(
            boolean blur,
            boolean clamp,
            int[] mipmaps
    ) {
        this.blur = blur;
        this.clamp = clamp;
        this.mipmaps = mipmaps;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isClamp() {
        return clamp;
    }

    public int[] getMipmaps() {
        return mipmaps;
    }

    @Override
    public void write(TreeWriter.Context context) {
        context.writeBooleanField("blur", blur);
        context.writeBooleanField("clamp", clamp);
        context.writeKey("mipmaps");
        context.startArray();
        for (int i = 0; i < mipmaps.length; i++) {
            if (i != 0) {
                // write separator from previous
                // value and current value
                context.writeSeparator();
            }
            context.writeIntValue(mipmaps[i]);
        }
        context.endArray();
    }

}
