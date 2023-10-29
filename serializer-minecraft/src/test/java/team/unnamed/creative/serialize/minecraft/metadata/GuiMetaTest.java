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
import org.junit.jupiter.api.Test;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.gui.GuiBorder;
import team.unnamed.creative.metadata.gui.GuiMeta;
import team.unnamed.creative.metadata.gui.GuiScaling;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuiMetaTest {

    @Test
    void test_simple_serialization() throws IOException {
        Metadata metadata;
        try (JsonReader reader = new JsonReader(new InputStreamReader(GuiMetaTest.class.getClassLoader().getResourceAsStream("metadata/gui.mcmeta")))) {
            metadata = MetadataSerializer.INSTANCE.readFromTree(new JsonParser().parse(reader));
        }

        assertEquals(
                Metadata.metadata()
                        .add(GuiMeta.of(GuiScaling.nineSlice(
                                200,
                                20,
                                GuiBorder.border(4, 4, 20, 20)
                        )))
                        .build(),
                metadata
        );
    }
}
