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
package team.unnamed.creative.serialize.minecraft;

import com.google.gson.stream.JsonWriter;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.font.LegacyUnicodeFontProvider;
import team.unnamed.creative.font.SpaceFontProvider;
import team.unnamed.creative.font.TrueTypeFontProvider;
import team.unnamed.creative.serialize.minecraft.io.FileTreeWriter;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class SerializerFont {

    static final SerializerFont INSTANCE = new SerializerFont();

    public void write(Font font, FileTreeWriter tree) throws IOException {
        try (JsonWriter writer = tree.openJsonWriter(MinecraftResourcePackStructure.pathOf(font))) {
            writer.beginObject()
                    .name("providers")
                    .beginArray();
            for (FontProvider provider : font.providers()) {
                if (provider instanceof BitMapFontProvider) {
                    writeBitMap(writer, (BitMapFontProvider) provider);
                } else if (provider instanceof LegacyUnicodeFontProvider) {
                    writeLegacyUnicode(writer, (LegacyUnicodeFontProvider) provider);
                } else if (provider instanceof SpaceFontProvider) {
                    writeSpace(writer, (SpaceFontProvider) provider);
                } else if (provider instanceof TrueTypeFontProvider) {
                    writeTrueType(writer, (TrueTypeFontProvider) provider);
                } else {
                    throw new IllegalStateException("Unknown font provider type: " + provider);
                }
            }
            writer.endArray().endObject();
        }
    }

    private static void writeBitMap(JsonWriter writer, BitMapFontProvider provider) throws IOException {
        String fileValue = Keys.toString(provider.file());
        if (!fileValue.endsWith(".png")) {
            fileValue += ".png";
        }

        writer.beginObject()
                .name("type").value("bitmap")
                .name("file").value(fileValue);

        int height = provider.height();
        if (height != BitMapFontProvider.DEFAULT_HEIGHT) {
            // only write if height is not equal to the default height
            writer.name("height").value(height);
        }

        writer.name("ascent").value(provider.ascent())
                .name("chars").beginArray();

        for (String row : provider.characters()) {
            writer.value(row);
        }

        writer.endArray()
                .endObject();
    }

    private static void writeLegacyUnicode(JsonWriter writer, LegacyUnicodeFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("legacy_unicode")
                .name("sizes").value(Keys.toString(provider.sizes()))
                .name("template").value(Keys.toString(provider.template()))
                .endObject();
    }

    private static void writeSpace(JsonWriter writer, SpaceFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("space")
                .name("advances").beginObject();
        for (Map.Entry<Character, Integer> entry : provider.advances().entrySet()) {
            writer.name(Character.toString(entry.getKey())).value(entry.getValue());
        }
        writer.endObject().endObject();
    }

    private static void writeTrueType(JsonWriter writer, TrueTypeFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("ttf")
                .name("file").value(Keys.toString(provider.file()));

        Vector2Float shift = provider.shift();
        if (!shift.equals(Vector2Float.ZERO)) {
            writer.name("shift").beginArray()
                    .value(shift.x())
                    .value(shift.y())
                    .endArray();
        }

        List<String> skip = provider.skip();
        if (!skip.isEmpty()) {
            // micro-optimization: vanilla client is a bit lenient
            // in this case, it can accept a string or a string array,
            // so we can optimize this
            writer.name("skip");
            if (skip.size() == 1) {
                writer.value(skip.get(0));
            } else {
                writer.beginArray();
                for (String row : skip) {
                    writer.value(row);
                }
                writer.endArray();
            }
        }

        float size = provider.size();
        if (size != TrueTypeFontProvider.DEFAULT_SIZE) {
            writer.name("size").value(size);
        }

        float oversample = provider.oversample();
        if (oversample != TrueTypeFontProvider.DEFAULT_OVERSAMPLE) {
            writer.name("oversample").value(oversample);
        }

        writer.endObject();
    }

}