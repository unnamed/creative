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
package team.unnamed.creative.serialize.minecraft.metadata;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.animation.AnimationFrame;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.metadata.overlays.OverlayEntry;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;

import java.io.InputStreamReader;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetadataTest {

    @Test
    @DisplayName("Test Metadata deserialization from resources")
    void test_deserialization() throws Exception {
        Metadata metadata;
        try (JsonReader reader = new JsonReader(new InputStreamReader(MetadataTest.class.getClassLoader().getResourceAsStream("metadata/pack.mcmeta")))) {
            metadata = MetadataSerializer.INSTANCE.readFromTree(new JsonParser().parse(reader));
        }

        assertEquals(
                Metadata.builder()
                        .add(PackMeta.of(PackFormat.format(16, 16, 17), Component.text("Description!")))
                        .add(LanguageMeta.of(new HashMap<String, LanguageEntry>() {{
                            put("english", LanguageEntry.of("English", "US", false));
                        }}))
                        .add(FilterMeta.of(
                                KeyPattern.any(),
                                KeyPattern.ofNamespace("creative"),
                                KeyPattern.of("fancy", "thing/.+")
                        ))
                        .add(OverlaysMeta.of(
                                OverlayEntry.of(PackFormat.format(18), "abc"),
                                OverlayEntry.of(PackFormat.format(19, 19, 21), "def")
                        ))
                        .add(AnimationMeta.animation()
                                .width(16)
                                .height(16)
                                .frameTime(1)
                                .frames(
                                        AnimationFrame.frame(0, 0),
                                        AnimationFrame.frame(1, 10)
                                )
                                .build())
                        .build(),
                metadata
        );
    }

}
