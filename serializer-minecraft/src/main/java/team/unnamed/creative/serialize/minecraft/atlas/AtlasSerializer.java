/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public final class AtlasSerializer implements JsonResourceSerializer<Atlas>, JsonResourceDeserializer<Atlas> {

    public static final AtlasSerializer INSTANCE;
    public static final ResourceCategory<Atlas> CATEGORY;

    private static final String SOURCES_FIELD = "sources";


    static {
        INSTANCE = new AtlasSerializer();
        CATEGORY = new ResourceCategory<>(
                "atlases",
                ".json",
                ResourceContainer::atlas,
                ResourceContainer::atlases,
                INSTANCE
        );
    }

    private AtlasSerializer() {
    }

    // {
    //     "sources": [
    //         <atlas source>,
    //         <atlas source>,
    //         <atlas source>
    //     ]
    // }

    @Override
    public void serializeToJson(Atlas object, JsonWriter writer) throws IOException {
        writer.beginObject()
                .name(SOURCES_FIELD)
                .beginArray();
        for (AtlasSource source : object.sources()) {
            AtlasSourceSerializer.serialize(source, writer);
        }
        writer.endArray().endObject();
    }

    @Override
    public Atlas deserializeFromJson(JsonElement node, Key key) {
        List<AtlasSource> sources = new ArrayList<>();
        for (JsonElement sourceElement : node.getAsJsonObject().getAsJsonArray(SOURCES_FIELD)) {
            sources.add(AtlasSourceSerializer.deserialize(sourceElement.getAsJsonObject()));
        }
        return Atlas.builder()
                .key(key)
                .sources(sources)
                .build();
    }

}
