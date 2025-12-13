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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.waypoint.WaypointStyle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaypointSerializationTest {
    @Test
    @DisplayName("Test Waypoint Style JSON Serialization")
    void test_serialize() throws Exception {
        WaypointStyle style = WaypointStyle.waypointStyle()
                .key(Key.key("minecraft", "test"))
                .nearDistance(0)
                .farDistance(100)
                .sprite(Key.key("test:abc"))
                .sprite(Key.key("test:def"))
                .build();

        assertEquals(
                "{\"near_distance\":0,\"far_distance\":100,\"sprites\":[\"test:abc\",\"test:def\"]}",
                WaypointStyleSerializer.INSTANCE.serializeToJsonString(style)
        );
    }

    @Test
    @DisplayName("Test Waypoint Style JSON Deserialization")
    void test_deserialize() throws Exception {
        String json = "{\"near_distance\":0,\"far_distance\":100,\"sprites\":[\"test:abc\",\"test:def\"]}";
        WaypointStyle style = WaypointStyleSerializer.INSTANCE.deserializeFromJsonString(json, Key.key("minecraft", "test"));

        assertEquals(Key.key("minecraft", "test"), style.key());
        assertEquals(0, style.nearDistance());
        assertEquals(100, style.farDistance());
        assertEquals(2, style.sprites().size());
        assertEquals(Key.key("test:abc"), style.sprites().get(0));
        assertEquals(Key.key("test:def"), style.sprites().get(1));
    }

}
