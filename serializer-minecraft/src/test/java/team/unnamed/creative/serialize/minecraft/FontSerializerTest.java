/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.creative.serialize.minecraft;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import java.io.IOException;

public class FontSerializerTest {

    @Test
    public void test() throws IOException {
        try (TestMinecraftFileTree tree = TestMinecraftFileTree.create()) {

            Font font = Font.of(
                    Key.key("minecraft:default"),
                    FontProvider.bitMap()
                            .file(Key.key("creative:font_textures/texture_1"))
                            .ascent(8)
                            .height(8)
                            .characters(
                                    "abcdef",
                                    "ghijkm"
                            )
                            .build(),
                    FontProvider.legacyUnicode(
                            Key.key("minecraft:some_sizes"),
                            Key.key("creative:a_template")
                    ),
                    FontProvider.trueType()
                            .file(Key.key("creative:the_file"))
                            .oversample(0.5f)
                            .shift(new Vector2Float(0.8f, 0.3f))
                            .size(1.2f)
                            .skip("abc", "def")
                            .build()
            );

            FontSerializer.INSTANCE.write(font, tree);

            tree.expect("assets/minecraft/font/default.json")
                    .toBeJson(
                            "{" +
                                "\"providers\":[" +
                                    "{" +
                                        "\"type\":\"bitmap\"," +
                                        "\"file\":\"creative:font_textures/texture_1.png\"," +
                                        "\"ascent\":8," +
                                        "\"chars\":[\"abcdef\",\"ghijkm\"]" +
                                    "}," +
                                    "{" +
                                        "\"type\":\"legacy_unicode\"," +
                                        "\"sizes\":\"some_sizes\"," +
                                        "\"template\":\"creative:a_template\"" +
                                    "}," +
                                    "{" +
                                        "\"type\":\"ttf\"," +
                                        "\"file\":\"creative:the_file\"," +
                                        "\"shift\":[0.8,0.3]," +
                                        "\"skip\":[\"abc\",\"def\"]," +
                                        "\"size\":1.2," +
                                        "\"oversample\":0.5" +
                                    "}" +
                                "]" +
                            "}"
                    );
        }
    }

}
