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

import team.unnamed.uracle.TreeWriter;
import team.unnamed.uracle.texture.Texture;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bitmap font (font that uses a set of
 * string characters and PNG images to render) for
 * Minecraft resource packs
 */
public class BitMapFont implements Font {

    /**
     * Default bitmap font height
     */
    public static final int DEFAULT_HEIGHT = 8;

    /**
     * The texture of this bitmap font, must be
     * a PNG image
     */
    private final Texture texture;

    /**
     * Optional. The height of the character, measured in pixels.
     * Can be negative. This tag is separate from the area used
     * in the source texture and just rescales the displayed
     * result
     */
    private final int height;

    /**
     * The ascent of the character, measured in pixels. This value
     * adds a vertical shift to the displayed result.
     */
    private final int ascent;

    /**
     * A list of strings containing the characters replaced by this
     * provider, as well as their order within the texture. All elements
     * must describe the same number of characters. The texture is split
     * into one equally sized row for each element of this list. Each
     * row is split into one equally sized character for each character
     * within one list element.
     */
    private final List<String> characters;

    public BitMapFont(
            Texture texture,
            int height,
            int ascent,
            List<String> characters
    ) {
        this.texture = texture;
        this.height = height;
        this.ascent = ascent;
        this.characters = characters;
    }

    @Override
    public Type getType() {
        return Type.BITMAP;
    }

    /**
     * Returns the texture of this bitmap font
     *
     * @return The font texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Returns the font characters height
     *
     * @return The font characters height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the font characters ascent, this
     * value as a vertical shift to the fonts
     *
     * @return The font characters ascent
     */
    public int getAscent() {
        return ascent;
    }

    /**
     * Writes the bitmap required information, if the
     * {@link BitMapFont#write(TreeWriter.Context)} method
     * is not called, the written files will be ignored
     *
     * @param writer The target tree writer
     */
    @Override
    public void write(TreeWriter writer) {
        // delegate to write the texture
        texture.write(writer);
    }

    /**
     * Writes the bitmap font provider registration, it is
     * part of the actual font
     *
     * @param context The target context, will not close
     */
    @Override
    public void write(TreeWriter.Context context) {
        context.writeStringField("type", "bitmap");
        context.writeStringField("file", texture.getLocation().toString());
        if (height != DEFAULT_HEIGHT) {
            // only write if height is not equal to
            // the default height
            context.writeIntField("height", height);
        }
        context.writeIntField("ascent", ascent);

        context.writeKey("chars");
        context.startArray();
        {
            Iterator<String> iterator = characters.iterator();
            while (iterator.hasNext()) {
                context.writeStringValue(iterator.next());
                if (iterator.hasNext()) {
                    context.writeSeparator();
                }
            }
        }
        context.endArray();
    }

    @Override
    public String toString() {
        return "BitMapFont{" +
                "texture=" + texture +
                ", height=" + height +
                ", ascent=" + ascent +
                ", characters=" + characters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitMapFont that = (BitMapFont) o;
        return height == that.height
                && ascent == that.ascent
                && texture.equals(that.texture)
                && characters.equals(that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, height, ascent, characters);
    }

}
