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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.file.ResourceAssertions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FontProviderTest {

    @Test
    @DisplayName("Test that full space font serialization works")
    public void test_space_font_provider_serialization() {
        Map<Character, Integer> advances = new HashMap<>();
        advances.put("a".charAt(0),4);
        advances.put("b".charAt(0),8);
        FontProvider fontProvider = FontProvider.space()
                .advances(advances)
                .build();

        ResourceAssertions.assertSerializedResult(
                "{\"type\":\"space\",\"advances\":{\"a\":4,\"b\":8}}",
                fontProvider
        );
    }

    @Test
    @DisplayName("Test that full BitMap font serialization works")
    public void test_full_bitmap_font_serialization() {
        FontProvider fontProvider = FontProvider.bitMap()
                .file(Key.key("creative:test"))
                .height(16)
                .ascent(8)
                .characters(Arrays.asList(
                        "µŋ",
                        "tm"
                ))
                .build();

        ResourceAssertions.assertSerializedResult(
                "{\"type\":\"bitmap\",\"file\":\"creative:test.png\",\"height\":16,\"ascent\":8,\"chars\":[\"µŋ\",\"tm\"]}",
                fontProvider
        );
    }

    @Test
    @DisplayName("Test that BitMap font default values are not serialized")
    public void test_minimal_bitmap_font_serialization() {
        FontProvider fontProvider = FontProvider.bitMap()
                .file(Key.key("test"))
                .height(BitMapFontProvider.DEFAULT_HEIGHT)
                .ascent(8)
                .characters(Arrays.asList(
                        "creative",
                        "creative"
                ))
                .build();

        ResourceAssertions.assertSerializedResult(
                "{\"type\":\"bitmap\",\"file\":\"test.png\",\"ascent\":8,\"chars\":[\"creative\",\"creative\"]}",
                fontProvider
        );
    }

    @Test
    @DisplayName("Test that BitMap font values are validated")
    public void test_bitmap_font_validation() {
        assertThrows(IllegalArgumentException.class, () ->
                FontProvider.bitMap()
                        .file(Key.key("test"))
                        .height(8)
                        .ascent(16) // <--- Error here, ascent > height
                        .characters(Arrays.asList(
                                "creative",
                                "creative"
                        ))
                        .build());

        assertThrows(IllegalArgumentException.class, () ->
                FontProvider.bitMap()
                        .file(Key.key("test"))
                        .ascent(4)
                        .characters(Collections.emptyList()) // <--- Error here, empty char list
                        .build());

        assertThrows(IllegalArgumentException.class, () ->
                FontProvider.bitMap()
                        .file(Key.key("test"))
                        .ascent(4)
                        .characters(Arrays.asList(
                                "hello world",
                                "hello" // <--- Error here, elements do not have the same length
                        ))
                        .build());
    }

}
