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
import team.unnamed.creative.serialize.minecraft.base.KeyPatternSerializer;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AtlasSourceSerializer {

    private static final String TYPE_FIELD = "type";

    private static final String SINGLE_TYPE = "single";
    private static final String DIRECTORY_TYPE = "directory";
    private static final String FILTER_TYPE = "filter";
    private static final String UNSTITCH_TYPE = "unstitch";
    private static final String PALETTED_PERMUTATIONS_TYPE = "paletted_permutations";

    // ------- TYPES ---------
    // {
    //     "type": "single",
    //     "resource": <key>,
    //     "sprite": <optional key>
    // },
    // {
    //     "type": "directory",
    //     "source": <string>,
    //     "prefix": <string>
    // },
    // {
    //     "type": "filter",
    //     "pattern": <KeyPattern>
    // },
    // {
    //     "type": "unstitch",
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
    //     "type": "paletted_permutations",
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
            writer.name(TYPE_FIELD).value(SINGLE_TYPE)
                    .name("resource").value(Keys.toString(resource));
            if (sprite != null && !sprite.equals(resource)) {
                writer.name("sprite").value(Keys.toString(sprite));
            }
        } else if (source instanceof DirectoryAtlasSource) {
            DirectoryAtlasSource dirSource = (DirectoryAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(DIRECTORY_TYPE)
                    .name("source").value(dirSource.source())
                    .name("prefix").value(dirSource.prefix());
        } else if (source instanceof FilterAtlasSource) {
            FilterAtlasSource filterSource = (FilterAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(FILTER_TYPE)
                    .name("pattern");
            KeyPatternSerializer.serialize(filterSource.pattern(), writer);
        } else if (source instanceof UnstitchAtlasSource) {
            UnstitchAtlasSource unstitchSource = (UnstitchAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(UNSTITCH_TYPE)
                    .name("resource").value(Keys.toString(unstitchSource.resource()));
            double divisorX = unstitchSource.xDivisor();
            if (divisorX != UnstitchAtlasSource.DEFAULT_X_DIVISOR) {
                writer.name("divisor_x").value(divisorX);
            }
            double divisorY = unstitchSource.yDivisor();
            if (divisorY != UnstitchAtlasSource.DEFAULT_Y_DIVISOR) {
                writer.name("divisor_y").value(divisorY);
            }
            writer.name("regions").beginArray();
            for (UnstitchAtlasSource.Region region : unstitchSource.regions()) {
                writer.beginObject()
                        .name("sprite").value(Keys.toString(region.sprite()))
                        .name("x").value(region.x())
                        .name("y").value(region.y())
                        .name("width").value(region.width())
                        .name("height").value(region.height())
                        .endObject();
            }
            writer.endArray();
        } else if (source instanceof PalettedPermutationsAtlasSource) {
            PalettedPermutationsAtlasSource ppSource = (PalettedPermutationsAtlasSource) source;
            writer
                    .name(TYPE_FIELD).value(PALETTED_PERMUTATIONS_TYPE)
                    .name("textures").beginArray();
            for (Key texture : ppSource.textures()) {
                writer.value(Keys.toString(texture));
            }
            writer.endArray();
            writer.name("palette_key").value(Keys.toString(ppSource.paletteKey()));
            writer.name("permutations").beginObject();
            for (Map.Entry<String, Key> entry : ppSource.permutations().entrySet()) {
                writer.name(entry.getKey()).value(Keys.toString(entry.getValue()));
            }
            writer.endObject();
        } else {
            throw new IllegalArgumentException("Unknown atlas source type: '" + source + "'.");
        }
        writer.endObject();
    }

    static AtlasSource deserialize(JsonObject node) {
        String type = node.get(TYPE_FIELD).getAsString();
        switch (type) {
            case SINGLE_TYPE: {
                @Subst("minecraft:resource")
                String resourceStr = node.get("resource").getAsString();
                @Subst("minecraft:resource")
                @Nullable
                String spriteStr = node.has("sprite") ? node.get("sprite").getAsString() : null;

                Key resource = Key.key(resourceStr);
                @Nullable Key sprite = spriteStr == null ? null : Key.key(spriteStr);
                return AtlasSource.single(resource, sprite);
            }
            case DIRECTORY_TYPE: {
                String source = node.get("source").getAsString();
                String prefix = node.get("prefix").getAsString();
                return AtlasSource.directory(source, prefix);
            }
            case FILTER_TYPE: {
                KeyPattern pattern = KeyPatternSerializer.deserialize(node.getAsJsonObject("pattern"));
                return AtlasSource.filter(pattern);
            }
            case UNSTITCH_TYPE: {
                @Subst("minecraft:resource")
                String resourceStr = node.get("resource").getAsString();
                Key resource = Key.key(resourceStr);
                double xDivisor = node.has("divisor_x") ? node.get("divisor_x").getAsDouble() : UnstitchAtlasSource.DEFAULT_X_DIVISOR;
                double yDivisor = node.has("divisor_y") ? node.get("divisor_y").getAsDouble() : UnstitchAtlasSource.DEFAULT_Y_DIVISOR;
                List<UnstitchAtlasSource.Region> regions = new ArrayList<>();
                for (JsonElement regionElement : node.getAsJsonArray("regions")) {
                    JsonObject regionNode = regionElement.getAsJsonObject();
                    @Subst("minecraft:resource")
                    String spriteStr = regionNode.get("sprite").getAsString();
                    regions.add(UnstitchAtlasSource.Region.of(
                            Key.key(spriteStr),
                            regionNode.get("x").getAsDouble(),
                            regionNode.get("y").getAsDouble(),
                            regionNode.get("width").getAsDouble(),
                            regionNode.get("height").getAsDouble()
                    ));
                }
                return AtlasSource.unstitch(resource, regions, xDivisor, yDivisor);
            }
            case PALETTED_PERMUTATIONS_TYPE: {
                List<Key> textures = new ArrayList<>();
                for (JsonElement keyElement : node.getAsJsonArray("textures")) {
                    @Subst("minecraft:resource")
                    String key = keyElement.getAsString();
                    textures.add(Key.key(key));
                }
                @Subst("minecraft:resource")
                String paletteKeyStr = node.get("palette_key").getAsString();
                Key paletteKey = Key.key(paletteKeyStr);
                Map<String, Key> permutations = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : node.getAsJsonObject("permutations").entrySet()) {
                    @Subst("minecraft:resource")
                    String value = entry.getValue().getAsString();
                    permutations.put(entry.getKey(), Key.key(value));
                }
                return AtlasSource.palettedPermutations(textures, paletteKey, permutations);
            }
            default:
                throw new IllegalArgumentException("Unknown atlas source type: '" + type + "'.");
        }
    }

}