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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Axis3D;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Readable;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.ElementRotation;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.serialize.minecraft.model.ModelSerializer;
import team.unnamed.creative.texture.TextureUV;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquipmentDeserializationTest {
    @Test
    @DisplayName("Test 'netherite.json' equipment deserialization")
    void test_deserialize_netherite() throws Exception {
        Equipment equipment = EquipmentSerializer.INSTANCE.deserialize(
                Readable.resource(getClass().getClassLoader(), "equipment/netherite.json"),
                Key.key("netherite")
        );

        assertEquals(
                Equipment.equipment()
                        .key(Key.key("minecraft:netherite"))
                        .humanoid(Key.key("minecraft:netherite"))
                        .humanoidLeggings(Key.key("minecraft:netherite"))
                        .build(),
                equipment
        );
    }

    @Test
    @DisplayName("Test 'wings.json' equipment deserialization")
    void test_deserialize_wings() throws Exception {
        Equipment equipment = EquipmentSerializer.INSTANCE.deserialize(
                Readable.resource(getClass().getClassLoader(), "equipment/elytra.json"),
                Key.key("elytra")
        );

        assertEquals(
                Equipment.equipment()
                        .key(Key.key("minecraft:elytra"))
                        .wings(Key.key("minecraft:elytra"))
                        .build(),
                equipment
        );
    }
}
