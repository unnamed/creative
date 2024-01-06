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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.atlas.DirectoryAtlasSource;
import team.unnamed.creative.atlas.PalettedPermutationsAtlasSource;
import team.unnamed.creative.atlas.SingleAtlasSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AtlasDeserializationTest {
    @Test
    @DisplayName("Test deserializing blocks.json atlas from Minecraft 1.20.2")
    void test_blocks_atlas_deserialization() throws IOException {
        Atlas atlas;
        try (InputStream input = AtlasDeserializationTest.class.getClassLoader().getResourceAsStream("atlas/blocks.json")) {
            atlas = AtlasSerializer.INSTANCE.deserialize(input, Key.key("minecraft:blocks"));
        }

        assertEquals(Key.key("minecraft:blocks"), atlas.key());

        List<AtlasSource> sources = atlas.sources();
        assertEquals(7, sources.size(), "There must be 7 atlas sources");

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
        {
            AtlasSource s3 = sources.get(2);
            assertTrue(s3 instanceof DirectoryAtlasSource, "Third source must be a directory atlas source");
            DirectoryAtlasSource d2 = (DirectoryAtlasSource) s3;
            assertEquals("entity/conduit", d2.source(), "Third source must have source = 'entity/conduit'");
            assertEquals("entity/conduit/", d2.prefix(), "Second source must have prefix = 'entity/conduit/'");
        }
        {
            AtlasSource s4 = sources.get(3);
            assertTrue(s4 instanceof SingleAtlasSource, "Fourth source must be a single atlas source");
            SingleAtlasSource single1 = (SingleAtlasSource) s4;
            assertEquals(Key.key("entity/bell/bell_body"), single1.resource(), "Fourth source must have resource = 'minecraft:entity/bell/bell_body'");
            assertNull(single1.sprite(), "Fourth source must have sprite = null");
        }
        {
            AtlasSource s5 = sources.get(4);
            assertTrue(s5 instanceof SingleAtlasSource, "Fifth source must be a single atlas source");
            SingleAtlasSource single2 = (SingleAtlasSource) s5;
            assertEquals(Key.key("entity/decorated_pot/decorated_pot_side"), single2.resource(), "Fifth source must have resource = 'minecraft:entity/decorated_pot/decorated_pot_side'");
            assertNull(single2.sprite(), "Fifth source must have sprite = null");
        }
        {
            AtlasSource s6 = sources.get(5);
            assertTrue(s6 instanceof SingleAtlasSource, "Sixth source must be a single atlas source");
            SingleAtlasSource single3 = (SingleAtlasSource) s6;
            assertEquals(Key.key("entity/enchanting_table_book"), single3.resource(), "Sixth source must have resource = 'minecraft:entity/enchanting_table_book'");
        }
        {
            AtlasSource s7 = sources.get(6);
            assertTrue(s7 instanceof PalettedPermutationsAtlasSource, "Seventh source must be a paletted permutations atlas source");
            PalettedPermutationsAtlasSource permutations = (PalettedPermutationsAtlasSource) s7;
            assertEquals(Key.key("trims/color_palettes/trim_palette"), permutations.paletteKey(), "Seventh source must have palette key = 'minecraft:trims/color_palettes/trim_palette'");
            assertEquals(Arrays.asList(
                    Key.key("trims/items/leggings_trim"),
                    Key.key("trims/items/chestplate_trim"),
                    Key.key("trims/items/helmet_trim"),
                    Key.key("trims/items/boots_trim")
            ), permutations.textures(), "Seventh source must have textures = [ 'minecraft:trims/items/leggings_trim', 'minecraft:trims/items/chestplate_trim', 'minecraft:trims/items/helmet_trim', 'minecraft:trims/items/boots_trim' ]");

            Map<String, Key> expectedPermutations = new HashMap<>();
            expectedPermutations.put("quartz", Key.key("trims/color_palettes/quartz"));
            expectedPermutations.put("iron", Key.key("trims/color_palettes/iron"));
            expectedPermutations.put("gold", Key.key("trims/color_palettes/gold"));
            expectedPermutations.put("diamond", Key.key("trims/color_palettes/diamond"));
            expectedPermutations.put("netherite", Key.key("trims/color_palettes/netherite"));
            expectedPermutations.put("redstone", Key.key("trims/color_palettes/redstone"));
            expectedPermutations.put("copper", Key.key("trims/color_palettes/copper"));
            expectedPermutations.put("emerald", Key.key("trims/color_palettes/emerald"));
            expectedPermutations.put("lapis", Key.key("trims/color_palettes/lapis"));
            expectedPermutations.put("amethyst", Key.key("trims/color_palettes/amethyst"));
            expectedPermutations.put("iron_darker", Key.key("trims/color_palettes/iron_darker"));
            expectedPermutations.put("gold_darker", Key.key("trims/color_palettes/gold_darker"));
            expectedPermutations.put("diamond_darker", Key.key("trims/color_palettes/diamond_darker"));
            expectedPermutations.put("netherite_darker", Key.key("trims/color_palettes/netherite_darker"));
            assertEquals(expectedPermutations, permutations.permutations(), "Unexpected permutations for seventh source");
        }
    }
}
