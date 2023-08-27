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

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.atlas.SingleAtlasSource;
import team.unnamed.creative.util.Keys;

import java.io.IOException;

final class AtlasSourceSerializer {

    private static final String TYPE_FIELD = "type";
    private static final String SINGLE_TYPE = "single";

    // ------- TYPES ---------
    // {
    //     "type": "single",
    //     "resource": <key>,
    //     "sprite": <optional key>
    // }
    // {
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
            default:
                throw new IllegalArgumentException("Unknown atlas source type: '" + type + "'.");
        }
    }

}
