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
package team.unnamed.creative.serialize.minecraft.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FontDeserializationTest {

    @Test
    @DisplayName("Test deserialization of a Font with a single BitMap font provider")
    public void test_deserialize_bitmap_font() throws IOException {
        Font font = FontSerializer.INSTANCE.deserializeFromJsonString(
                "{" +
                        "\"providers\": [" +
                            "{" +
                                "\"type\": \"bitmap\"," +
                                "\"file\": \"creative:test\"," +
                                "\"ascent\": 8," +
                                "\"chars\": [\"a\"]" +
                            "}" +
                        "]" +
                    "}",
                Font.MINECRAFT_DEFAULT
        );

        assertEquals(Key.key("minecraft", "default"), font.key(), "font key must be minecraft:default!");
        List<FontProvider> providers = font.providers();
        assertEquals(1, providers.size(), "there must be only one provider!");
        FontProvider provider = providers.get(0);
        assertTrue(provider instanceof BitMapFontProvider, "Provider must be instance of BitMap provider");
        BitMapFontProvider bitMapProvider = (BitMapFontProvider) provider;
        assertEquals(Key.key("creative", "test"), bitMapProvider.file(), "file must be creative:test!");
        assertEquals(8, bitMapProvider.ascent(), "ascent must be 8!");
        assertEquals(BitMapFontProvider.DEFAULT_HEIGHT, bitMapProvider.height(), "height must be default!");
        List<String> chars = bitMapProvider.characters();
        assertEquals(1, chars.size(), "chars list must be singleton!");
        assertEquals("a", chars.get(0), "singleton string in list must be 'a'");
    }

}
