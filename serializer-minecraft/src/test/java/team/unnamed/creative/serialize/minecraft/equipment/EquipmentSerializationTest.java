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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.equipment.EquipmentLayer;
import team.unnamed.creative.equipment.EquipmentLayerType;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentSerializationTest {
    @Test
    void test_serialization() throws Exception {
        Equipment equipment = Equipment.equipment()
                .key(Key.key("creative:equipment"))
                .addHumanoidLayer(EquipmentLayer.layer(Key.key("creative:layer")))
                .addHorseBodyLayer(EquipmentLayer.layer(Key.key("creative:layer"), true))
                .build();

        String serialized = EquipmentSerializer.INSTANCE.serializeToJsonString(equipment);

        assertEquals("{\"layers\":{\"humanoid\":[{\"texture\":\"creative:layer\"}],\"horse_body\":[{\"texture\":\"creative:layer\",\"use_player_texture\":true}]}}", serialized);
    }

    @Test
    void test_deserialization() throws Exception {
        String serialized = "{\"layers\":{\"humanoid\":[{\"texture\":\"creative:layer\"}],\"horse_body\":[{\"texture\":\"creative:layer\",\"use_player_texture\":true}]}}";
        Equipment deserialized = EquipmentSerializer.INSTANCE.deserializeFromJsonString(serialized, Key.key("creative:equipment"));

        assertEquals("creative:equipment", deserialized.key().asString());
        assertEquals(2, deserialized.layers().size());
        assertEquals(1, deserialized.layers().get(EquipmentLayerType.HUMANOID).size());
        assertEquals(1, deserialized.layers().get(EquipmentLayerType.HORSE_BODY).size());

        EquipmentLayer humanoidLayer = deserialized.layers().get(EquipmentLayerType.HUMANOID).get(0);
        assertEquals("creative:layer", humanoidLayer.texture().asString());
        assertFalse(humanoidLayer.usePlayerTexture());

        EquipmentLayer horseBodyLayer = deserialized.layers().get(EquipmentLayerType.HORSE_BODY).get(0);
        assertEquals("creative:layer", horseBodyLayer.texture().asString());
        assertTrue(horseBodyLayer.usePlayerTexture());
    }
}
