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
package team.unnamed.creative.serialize.minecraft.atlas;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.atlas.DirectoryAtlasSource;
import team.unnamed.creative.atlas.FilterAtlasSource;
import team.unnamed.creative.atlas.PalettedPermutationsAtlasSource;
import team.unnamed.creative.atlas.SingleAtlasSource;
import team.unnamed.creative.atlas.UnstitchAtlasSource;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.serialize.minecraft.base.KeyPatternSerializer;
import team.unnamed.creative.serialize.minecraft.base.KeySerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class AtlasSourceSerializer {

    private static final String TYPE_FIELD = "type";

    private static final Key SINGLE_TYPE = Key.key("single");
    private static final Key DIRECTORY_TYPE = Key.key("directory");
    private static final Key FILTER_TYPE = Key.key("filter");
    private static final Key UNSTITCH_TYPE = Key.key("unstitch");
    private static final Key PALETTED_PERMUTATIONS_TYPE = Key.key("paletted_permutations");

    // ------- TYPES ---------
    // {
    //     "type": "minecraft:single",
    //     "resource": <key>,
    //     "sprite": <optional key>
    // },
    // {
    //     "type": "minecraft:directory",
    //     "source": <string>,
    //     "prefix": <string>
    // },
    // {
    //     "type": "minecraft:filter",
    //     "pattern": <KeyPattern>
    // },
    // {
    //     "type": "minecraft:unstitch",
    //     "resource": <key>,
    //     "regions": [<
    //         {
    //             "sprite": <key>,
    //             "x": <double>,
    //             "y": <double>,
    //             "width": <double>,
    //             "height": <double>
    //         }
    //     >],
    //     "divisor_x": <optional double = 1>,
    //     "divisor_y": <optional double = 1>
    // },
    // {
    //     "type": "minecraft:paletted_permutations",
    //     "textures": [<key>],
    //     "palette_key": <key>,
    //     "permutations": <{
    //         <string>: <key>
    //     }>
    // }

    static void serialize(AtlasSource source, JsonWriter writer) throws IOException {
        writer.beginObject();
        if (source instanceof SingleAtlasSource) {
            SingleAtlasSource singleSource = (SingleAtlasSource) source;
            Key resource = singleSource.resource();
            @Nullable Key sprite = singleSource.sprite();
            writer.name(TYPE_FIELD).value(KeySerializer.toString(SINGLE_TYPE))
                    .name("resource").value(KeySerializer.toString(resource));
            if (sprite != null && !sprite.equals(resource)) {
                writer.name("sprite").value(KeySerializer.toString(sprite));
            }
        } else if (source instanceof DirectoryAtlasSource) {
            DirectoryAtlasSource dirSource = (DirectoryAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(KeySerializer.toString(DIRECTORY_TYPE))
                    .name("source").value(dirSource.source())
                    .name("prefix").value(dirSource.prefix());
        } else if (source instanceof FilterAtlasSource) {
            FilterAtlasSource filterSource = (FilterAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(KeySerializer.toString(FILTER_TYPE))
                    .name("pattern");
            KeyPatternSerializer.serialize(filterSource.pattern(), writer);
        } else if (source instanceof UnstitchAtlasSource) {
            UnstitchAtlasSource unstitchSource = (UnstitchAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(KeySerializer.toString(UNSTITCH_TYPE))
                    .name("resource").value(KeySerializer.toString(unstitchSource.resource()));
            double divisorX = unstitchSource.divisor().x();
            if (divisorX != UnstitchAtlasSource.DEFAULT_DIVISOR.x()) {
                writer.name("divisor_x").value(divisorX);
            }
            double divisorY = unstitchSource.divisor().y();
            if (divisorY != UnstitchAtlasSource.DEFAULT_DIVISOR.y()) {
                writer.name("divisor_y").value(divisorY);
            }
            writer.name("regions").beginArray();
            for (UnstitchAtlasSource.Region region : unstitchSource.regions()) {
                writer.beginObject()
                        .name("sprite").value(KeySerializer.toString(region.sprite()))
                        .name("x").value(region.position().x())
                        .name("y").value(region.position().y())
                        .name("width").value(region.dimensions().x())
                        .name("height").value(region.dimensions().y())
                        .endObject();
            }
            writer.endArray();
        } else if (source instanceof PalettedPermutationsAtlasSource) {
            PalettedPermutationsAtlasSource ppSource = (PalettedPermutationsAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(KeySerializer.toString(PALETTED_PERMUTATIONS_TYPE))
                    .name("textures").beginArray();
            for (Key texture : ppSource.textures()) {
                writer.value(KeySerializer.toString(texture));
            }
            writer.endArray();
            writer.name("palette_key").value(KeySerializer.toString(ppSource.paletteKey()));
            writer.name("permutations").beginObject();
            for (Map.Entry<String, Key> entry : ppSource.permutations().entrySet()) {
                writer.name(entry.getKey()).value(KeySerializer.toString(entry.getValue()));
            }
            writer.endObject();
        } else {
            throw new IllegalArgumentException("Unknown atlas source type: '" + source + "'.");
        }
        writer.endObject();
    }

    static AtlasSource deserialize(JsonObject node) {
        Key type = Key.key(node.get(TYPE_FIELD).getAsString());
        if (type.equals(SINGLE_TYPE)) {
            @Subst("minecraft:resource")
            String resourceStr = node.get("resource").getAsString();
            @Subst("minecraft:resource")
            @Nullable
            String spriteStr = node.has("sprite") ? node.get("sprite").getAsString() : null;

            Key resource = Key.key(resourceStr);
            @Nullable Key sprite = spriteStr == null ? null : Key.key(spriteStr);
            return AtlasSource.single(resource, sprite);
        } else if (type.equals(DIRECTORY_TYPE)) {
            String source = node.get("source").getAsString();
            String prefix = node.get("prefix").getAsString();
            return AtlasSource.directory(source, prefix);
        } else if (type.equals(FILTER_TYPE)) {
            KeyPattern pattern = KeyPatternSerializer.deserialize(node.getAsJsonObject("pattern"));
            return AtlasSource.filter(pattern);
        } else if (type.equals(UNSTITCH_TYPE)) {
            @Subst("minecraft:resource")
            String resourceStr = node.get("resource").getAsString();
            Key resource = Key.key(resourceStr);
            float xDivisor = node.has("divisor_x") ? node.get("divisor_x").getAsFloat() : UnstitchAtlasSource.DEFAULT_DIVISOR.x();
            float yDivisor = node.has("divisor_y") ? node.get("divisor_y").getAsFloat() : UnstitchAtlasSource.DEFAULT_DIVISOR.y();
            List<UnstitchAtlasSource.Region> regions = new ArrayList<>();
            for (JsonElement regionElement : node.getAsJsonArray("regions")) {
                JsonObject regionNode = regionElement.getAsJsonObject();
                @Subst("minecraft:resource")
                String spriteStr = regionNode.get("sprite").getAsString();
                regions.add(UnstitchAtlasSource.Region.region(
                        Key.key(spriteStr),
                        new Vector2Float(
                                regionNode.get("x").getAsFloat(),
                                regionNode.get("y").getAsFloat()
                        ),
                        new Vector2Float(
                                regionNode.get("width").getAsFloat(),
                                regionNode.get("height").getAsFloat()
                        )
                ));
            }
            return AtlasSource.unstitch(resource, regions, new Vector2Float(xDivisor, yDivisor));
        } else if (type.equals(PALETTED_PERMUTATIONS_TYPE)) {
            List<Key> textures = new ArrayList<>();
            for (JsonElement keyElement : node.getAsJsonArray("textures")) {
                @Subst("minecraft:resource")
                String key = keyElement.getAsString();
                textures.add(Key.key(key));
            }
            @Subst("minecraft:resource")
            String paletteKeyStr = node.get("palette_key").getAsString();
            Key paletteKey = Key.key(paletteKeyStr);
            Map<String, Key> permutations = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : node.getAsJsonObject("permutations").entrySet()) {
                @Subst("minecraft:resource")
                String value = entry.getValue().getAsString();
                permutations.put(entry.getKey(), Key.key(value));
            }
            return AtlasSource.palettedPermutations(textures, paletteKey, permutations);
        } else {
            throw new IllegalArgumentException("Unknown atlas source type: '" + type + "'.");
        }
    }

}
