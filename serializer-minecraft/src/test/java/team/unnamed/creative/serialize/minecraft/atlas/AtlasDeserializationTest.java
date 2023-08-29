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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.atlas.DirectoryAtlasSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AtlasDeserializationTest {

    @Test
    @DisplayName("Test deserializing blocks.json atlas from Minecraft 1.19.3")
    public void test_blocks_atlas_deserialization() throws IOException {
        Atlas atlas;
        try (InputStream input = AtlasDeserializationTest.class.getClassLoader().getResourceAsStream("atlas/blocks.json")) {
            atlas = AtlasSerializer.INSTANCE.deserialize(input, Key.key("minecraft:blocks"));
        }

        List<AtlasSource> sources = atlas.sources();
        assertEquals(5, sources.size(), "There must be 5 atlas sources");

        // assert directory atlas sources
        {
            AtlasSource s1 = sources.get(0);
            assertTrue(s1 instanceof DirectoryAtlasSource, "First source must be a directory atlas source");
            DirectoryAtlasSource d1 = (DirectoryAtlasSource) s1;
            assertEquals("block", d1.source(), "First source must have source = 'block'");
            assertEquals("block/", d1.prefix(), "First source must have prefix = 'block/'");
        }
        {
            AtlasSource s2 = sources.get(1);
            assertTrue(s2 instanceof DirectoryAtlasSource, "Second source must be a directory atlas source");
            DirectoryAtlasSource d2 = (DirectoryAtlasSource) s2;
            assertEquals("item", d2.source(), "Second source must have source = 'item'");
            assertEquals("item/", d2.prefix(), "Second source must have prefix = 'item/'");
        }
    }

}
