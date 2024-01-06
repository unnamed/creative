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
package team.unnamed.creative.serialize.minecraft.metadata;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.gui.GuiBorder;
import team.unnamed.creative.metadata.gui.GuiMeta;
import team.unnamed.creative.metadata.gui.GuiScaling;
import team.unnamed.creative.serialize.minecraft.GsonUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class GuiMetaTest {
    @Test
    void test_deserialization() throws IOException {
        final Map<String, GuiScaling> expectations = new HashMap<>();
        expectations.put("button.png.mcmeta", GuiScaling.nineSlice(200, 20, 3));
        expectations.put("button_disabled.png.mcmeta", GuiScaling.nineSlice(200, 20, 1));
        expectations.put("slider_handle.png.mcmeta", GuiScaling.nineSlice(8, 20, GuiBorder.border(2, 3, 2, 2)));
        expectations.put("slider_handle_highlighted.png.mcmeta", GuiScaling.nineSlice(8, 20, GuiBorder.border(2, 3, 2, 2)));
        expectations.put("stretch_custom.png.mcmeta", GuiScaling.stretch());
        expectations.put("tile_custom.png.mcmeta", GuiScaling.tile(8, 20));
        expectations.put("tile_custom_2.png.mcmeta", GuiScaling.tile(200, 20));
        expectations.put("tile_custom_3.png.mcmeta", GuiScaling.tile(200, 26));
        expectations.put("title_box.png.mcmeta", GuiScaling.nineSlice(200, 26, 10));

        for (final Map.Entry<String, GuiScaling> entry : expectations.entrySet()) {
            final Metadata metadata;
            try (final JsonReader reader = new JsonReader(new InputStreamReader(GuiMetaTest.class.getClassLoader().getResourceAsStream("metadata/gui/" + entry.getKey())))) {
                metadata = MetadataSerializer.INSTANCE.readFromTree(GsonUtil.parseReader(reader));
            }

            assertEquals(
                    Metadata.metadata()
                            .add(GuiMeta.of(entry.getValue()))
                            .build(),
                    metadata,
                    "Not equal for entry: " + entry.getKey()
            );
        }
    }
}
