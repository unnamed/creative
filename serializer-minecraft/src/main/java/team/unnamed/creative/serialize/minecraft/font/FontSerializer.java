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
package team.unnamed.creative.serialize.minecraft.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.font.LegacyUnicodeFontProvider;
import team.unnamed.creative.font.ReferenceFontProvider;
import team.unnamed.creative.font.SpaceFontProvider;
import team.unnamed.creative.font.TrueTypeFontProvider;
import team.unnamed.creative.font.UnihexFontProvider;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.GsonUtil;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.base.KeySerializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FontSerializer implements JsonResourceSerializer<Font>, JsonResourceDeserializer<Font> {

    public static final FontSerializer INSTANCE;
    public static final ResourceCategory<Font> CATEGORY;

    static {
        INSTANCE = new FontSerializer();
        CATEGORY = new ResourceCategory<>(
                "font",
                ".json",
                ResourceContainer::font,
                ResourceContainer::fonts,
                INSTANCE
        );
    }

    @Override
    public void serializeToJson(Font font, JsonWriter writer) throws IOException {
        writer.beginObject()
                .name("providers")
                .beginArray();
        for (FontProvider provider : font.providers()) {
            if (provider instanceof BitMapFontProvider) {
                writeBitMap(writer, (BitMapFontProvider) provider);
            } else if (provider instanceof LegacyUnicodeFontProvider) { // TODO: Should we warn about deprecated stuff?
                writeLegacyUnicode(writer, (LegacyUnicodeFontProvider) provider);
            } else if (provider instanceof SpaceFontProvider) {
                writeSpace(writer, (SpaceFontProvider) provider);
            } else if (provider instanceof TrueTypeFontProvider) {
                writeTrueType(writer, (TrueTypeFontProvider) provider);
            } else if (provider instanceof ReferenceFontProvider) {
                writeReference(writer, (ReferenceFontProvider) provider);
            } else if (provider instanceof UnihexFontProvider) {
                writeUnihex(writer, (UnihexFontProvider) provider);
            } else {
                throw new IllegalStateException("Unknown font provider type: " + provider);
            }
        }
        writer.endArray().endObject();
    }

    @Override
    public Font deserializeFromJson(JsonElement node, Key key) {
        JsonObject objectNode = node.getAsJsonObject();
        List<FontProvider> providers = new ArrayList<>();
        for (JsonElement providerNode : objectNode.getAsJsonArray("providers")) {
            JsonObject providerObjectNode = providerNode.getAsJsonObject();
            String type = providerObjectNode.get("type").getAsString();
            switch (type) {
                case "bitmap": {
                    providers.add(readBitMap(providerObjectNode));
                    break;
                }
                case "legacy_unicode": {
                    providers.add(readLegacyUnicode(providerObjectNode));
                    break;
                }
                case "space": {
                    providers.add(readSpace(providerObjectNode));
                    break;
                }
                case "ttf": {
                    providers.add(readTrueType(providerObjectNode));
                    break;
                }
                case "reference": {
                    providers.add(readReference(providerObjectNode));
                    break;
                }
                case "unihex": {
                    providers.add(readUnihex(providerObjectNode));
                    break;
                }
                default:
                    throw new IllegalStateException("Unknown font provider type: " + type);
            }
        }
        return Font.of(key, providers);
    }

    private static void writeBitMap(JsonWriter writer, BitMapFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("bitmap")
                .name("file").value(KeySerializer.toString(provider.file()));

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

    private static BitMapFontProvider readBitMap(JsonObject node) {
        List<String> characters = new ArrayList<>();
        for (JsonElement line : node.getAsJsonArray("chars")) {
            characters.add(line.getAsString());
        }

        return FontProvider.bitMap()
                .file(Key.key(node.get("file").getAsString()))
                .height(GsonUtil.getInt(node, "height", BitMapFontProvider.DEFAULT_HEIGHT))
                .ascent(node.get("ascent").getAsInt())
                .characters(characters)
                .build();
    }

    private static void writeLegacyUnicode(JsonWriter writer, LegacyUnicodeFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("legacy_unicode")
                .name("sizes").value(KeySerializer.toString(provider.sizes()))
                .name("template").value(provider.template())
                .endObject();
    }

    private static LegacyUnicodeFontProvider readLegacyUnicode(JsonObject node) {
        // TODO: Should not be keys, they are formatted using String#format(...)
        return FontProvider.legacyUnicode(
                Key.key(node.get("sizes").getAsString()),
                node.get("template").getAsString()
        );
    }

    private static void writeSpace(JsonWriter writer, SpaceFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("space")
                .name("advances").beginObject();
        for (Map.Entry<String, Integer> entry : provider.advances().entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
        }
        writer.endObject().endObject();
    }

    private static SpaceFontProvider readSpace(JsonObject node) {
        JsonObject advancesNode = node.getAsJsonObject("advances");
        Map<String, Integer> advances = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> advanceEntryNode : advancesNode.entrySet()) {
            String character = advanceEntryNode.getKey();
            int advance = advanceEntryNode.getValue().getAsInt();
            advances.put(character, advance);
        }
        return FontProvider.space(advances);
    }

    private static void writeUnihex(JsonWriter writer, UnihexFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("unihex")
                .name("hex_file").value(KeySerializer.toString(provider.file()))
                .name("size_overrides").beginArray();
        for (UnihexFontProvider.SizeOverride sizeOverride : provider.sizes()) {
            writer.beginObject()
                    .name("from").value(new StringBuilder().appendCodePoint(sizeOverride.from()).toString())
                    .name("to").value(new StringBuilder().appendCodePoint(sizeOverride.to()).toString())
                    .name("left").value(sizeOverride.left())
                    .name("right").value(sizeOverride.right())
                    .endObject();
        }
        writer.endArray().endObject();
    }

    private static UnihexFontProvider readUnihex(JsonObject node) {
        List<UnihexFontProvider.SizeOverride> sizes = new ArrayList<>();
        for (JsonElement element : node.getAsJsonArray("size_overrides")) {
            JsonObject overrideNode = element.getAsJsonObject();
            sizes.add(UnihexFontProvider.SizeOverride.override(
                    overrideNode.get("from").getAsString(),
                    overrideNode.get("to").getAsString(),
                    overrideNode.get("left").getAsInt(),
                    overrideNode.get("right").getAsInt()
            ));
        }
        return FontProvider.unihex()
                .file(Key.key(node.get("hex_file").getAsString()))
                .sizes(sizes)
                .build();
    }

    private static void writeTrueType(JsonWriter writer, TrueTypeFontProvider provider) throws IOException {
        writer.beginObject()
                .name("type").value("ttf")
                .name("file").value(KeySerializer.toString(provider.file()));

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

    private static TrueTypeFontProvider readTrueType(JsonObject node) {
        Vector2Float shift = Vector2Float.ZERO;
        if (node.has("shift")) {
            JsonArray shiftNode = node.getAsJsonArray("shift");
            shift = new Vector2Float(
                    (float) shiftNode.get(0).getAsDouble(),
                    (float) shiftNode.get(1).getAsDouble()
            );
        }

        List<String> skip = new ArrayList<>();
        if (node.has("skip")) {
            JsonElement skipNode = node.get("skip");
            if (skipNode.isJsonArray()) {
                // multiple skip
                for (JsonElement skipped : skipNode.getAsJsonArray()) {
                    skip.add(skipped.getAsString());
                }
            } else {
                // single
                skip.add(skipNode.getAsString());
            }
        }

        return FontProvider.trueType()
                .file(Key.key(node.get("file").getAsString()))
                .shift(shift)
                .skip(skip)
                .size(GsonUtil.getFloat(node, "size", TrueTypeFontProvider.DEFAULT_SIZE))
                .oversample(GsonUtil.getFloat(node, "oversample", TrueTypeFontProvider.DEFAULT_OVERSAMPLE))
                .build();
    }

    private static void writeReference(JsonWriter writer, ReferenceFontProvider provider) throws IOException {
        writer.beginObject();
        writer.name("type").value("reference");
        writer.name("id").value(KeySerializer.toString(provider.id()));
        writer.endObject();
    }

    private static ReferenceFontProvider readReference(JsonObject node) {
        return FontProvider.reference(Key.key(node.get("id").getAsString()));
    }
}