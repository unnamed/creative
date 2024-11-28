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
package team.unnamed.creative.serialize.minecraft.equipment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceSerializer;

import java.io.IOException;

@ApiStatus.Internal
public class EquipmentSerializer implements JsonResourceSerializer<Equipment>, JsonResourceDeserializer<Equipment> {

    public static final EquipmentSerializer INSTANCE;
    public static final ResourceCategory<Equipment> CATEGORY;

    static {
        INSTANCE = new EquipmentSerializer();
        CATEGORY = new ResourceCategory<>(
                "equipment",
                ".json",
                ResourceContainer::equipment,
                ResourceContainer::equipments,
                EquipmentSerializer.INSTANCE
        );
    }

    @Override
    public void serializeToJson(Equipment equipment, JsonWriter writer) throws IOException {
        writer.beginObject().name("layers").beginObject();

        // humanoid
        Key humanoid = equipment.humanoid();
        if (humanoid != null) {
            writer.name("humanoid").beginArray();
            writer.name("texture").value(humanoid.toString());
            writer.endArray();
        }

        // humanoid_leggings
        Key humanoidLeggings = equipment.humanoidLeggings();
        if (humanoidLeggings != null) {
            writer.name("humanoid_leggings").beginArray();
            writer.name("texture").value(humanoidLeggings.toString());
            writer.endArray();
        }

        // wings
        Key wings = equipment.wings();
        if (wings != null) {
            writer.name("wings").beginArray();
            writer.name("texture").value(wings.toString());
            writer.endArray();
        }

        // horse_body
        Key horseBody = equipment.horseBody();
        if (horseBody != null) {
            writer.name("horse_body").beginArray();
            writer.name("texture").value(horseBody.toString());
            writer.endArray();
        }

        // llama_body
        Key llamaBody = equipment.llamaBody();
        if (llamaBody != null) {
            writer.name("llama_body").beginArray();
            writer.name("texture").value(llamaBody.toString());
            writer.endArray();
        }

        // wolf_body
        Key wolfBody = equipment.wolfBody();
        if (wolfBody != null) {
            writer.name("wolf_body").beginArray();
            writer.name("texture").value(wolfBody.toString());
            writer.endArray();
        }

        writer.endObject();
        writer.endObject();
    }

    @Override
    public Equipment deserializeFromJson(JsonElement node, Key key) {

        JsonObject objectNode = node.getAsJsonObject().getAsJsonObject("layers");

        // humanoid
        Key humanoid = null;
        if (objectNode.has("humanoid")) {
            humanoid = Key.key(objectNode.getAsJsonArray("humanoid").get(0).getAsJsonObject().get("texture").getAsString());
        }

        // humanoid_leggings
        Key humanoidLeggings = null;
        if (objectNode.has("humanoid_leggings")) {
            humanoidLeggings = Key.key(objectNode.getAsJsonArray("humanoid_leggings").get(0).getAsJsonObject().get("texture").getAsString());
        }

        // wings
        Key wings = null;
        if (objectNode.has("wings")) {
            wings = Key.key(objectNode.getAsJsonArray("wings").get(0).getAsJsonObject().get("texture").getAsString());
        }

        // horse_body
        Key horseBody = null;
        if (objectNode.has("horse_body")) {
            horseBody = Key.key(objectNode.getAsJsonArray("horse_body").get(0).getAsJsonObject().get("texture").getAsString());
        }

        // llama_body
        Key llamaBody = null;
        if (objectNode.has("llama_body")) {
            llamaBody = Key.key(objectNode.getAsJsonArray("llama_body").get(0).getAsJsonObject().get("texture").getAsString());
        }

        // wolf_body
        Key wolfBody = null;
        if (objectNode.has("wolf_body")) {
            wolfBody = Key.key(objectNode.getAsJsonArray("wolf_body").get(0).getAsJsonObject().get("texture").getAsString());
        }

        return Equipment.equipment().key(key)
                .humanoid(humanoid)
                .humanoidLeggings(humanoidLeggings)
                .wings(wings)
                .horseBody(horseBody)
                .llamaBody(llamaBody)
                .wolfBody(wolfBody)
                .build();
    }
}
