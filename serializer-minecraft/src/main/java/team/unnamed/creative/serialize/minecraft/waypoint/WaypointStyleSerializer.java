/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
package team.unnamed.creative.serialize.minecraft.waypoint;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategoryImpl;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;
import team.unnamed.creative.waypoint.WaypointStyle;

import java.io.IOException;

public class WaypointStyleSerializer implements JsonResourceSerializer<WaypointStyle>, JsonResourceDeserializer<WaypointStyle> {
    public static final WaypointStyleSerializer INSTANCE;
    public static final ResourceCategoryImpl<WaypointStyle> CATEGORY;

    static {
        INSTANCE = new WaypointStyleSerializer();
        CATEGORY = new ResourceCategoryImpl<>(
                "waypoint_style",
                ".json",
                ResourceContainer::waypointStyles,
                INSTANCE
        );
    }

    @Override
    public WaypointStyle deserializeFromJson(JsonElement node, Key key) throws IOException {
        WaypointStyle.Builder builder = WaypointStyle.waypointStyle()
                .key(key);

        JsonObject objectNode = node.getAsJsonObject();

        if (objectNode.has("near_distance"))
            builder.nearDistance(objectNode.get("near_distance").getAsInt());

        if (objectNode.has("far_distance"))
            builder.farDistance(objectNode.get("far_distance").getAsInt());

        if (objectNode.has("sprites")) {
            for (JsonElement textureKeyElement : objectNode.getAsJsonArray("sprites")) {
                @Subst("minecraft:a") String asString = textureKeyElement.getAsString();
                Key textureKey = Key.key(asString);
                builder.sprite(textureKey);
            }
        }

        return builder.build();
    }

    @Override
    public void serializeToJson(WaypointStyle object, JsonWriter writer, int targetPackFormat) throws IOException {
        writer.beginObject();

        writer.name("near_distance");
        writer.value(object.nearDistance());

        writer.name("far_distance");
        writer.value(object.farDistance());

        writer.name("sprites");
        writer.beginArray();
        for (Key sprite : object.sprites()) {
            writer.value(sprite.asString());
        }
        writer.endArray();

        writer.endObject();
    }
}
