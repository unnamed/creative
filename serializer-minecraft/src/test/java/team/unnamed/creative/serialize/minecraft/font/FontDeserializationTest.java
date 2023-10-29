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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Readable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.font.UnihexFontProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontDeserializationTest {
    @Test
    @DisplayName("Test deserialization of a Font with a single BitMap font provider")
    void test_deserialize_bitmap_font() throws IOException {
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

    @Test
    @DisplayName("Test deserialization of default fonts")
    void test_deserialize_from_resources() throws IOException {
        // default.json
        final Font _default = Font.font()
                .key(Font.MINECRAFT_DEFAULT)
                .addProvider(FontProvider.reference(Key.key("include/space")))
                .addProvider(FontProvider.reference(Key.key("include/default")))
                .addProvider(FontProvider.reference(Key.key("include/unifont")))
                .build();

        assertEquals(_default, deserializeFont(Font.MINECRAFT_DEFAULT, "font/default.json"));

        // alt.json
        final Font alt = Font.font()
                .key(Font.MINECRAFT_ALT)
                .addProvider(FontProvider.reference(Key.key("include/space")))
                .addProvider(FontProvider.bitMap()
                        .file(Key.key("font/ascii_sga.png"))
                        .ascent(7)
                        .characters(
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000ABCDEFGHIJKLMNO",
                                "PQRSTUVWXYZ\u0000\u0000\u0000\u0000\u0000",
                                "\u0000abcdefghijklmno",
                                "pqrstuvwxyz\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
                                "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                        )
                        .build())
                .build();

        assertEquals(alt, deserializeFont(Font.MINECRAFT_ALT, "font/alt.json"));

        // uniform.json
        final Font uniform = Font.font()
                .key(Font.MINECRAFT_UNIFORM)
                .addProvider(FontProvider.reference(Key.key("include/space")))
                .addProvider(FontProvider.reference(Key.key("include/unifont")))
                .build();

        assertEquals(uniform, deserializeFont(Font.MINECRAFT_UNIFORM, "font/uniform.json"));

        // illageralt.json
        final Font illagerAlt = Font.font()
                .key(Font.MINECRAFT_ILLAGERALT)
                .addProvider(FontProvider.reference(Key.key("include/space")))
                .addProvider(FontProvider.bitMap()
                        .file(Key.key("font/asciillager.png"))
                        .ascent(7)
                        .characters(
                                "!,-.0123456789?a",
                                "bcdefghijklmnopq",
                                "rstuvwxyzABCDEFG",
                                "HIJKLMNOPQRSTUVW",
                                "XYZ\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                        )
                        .build())
                .build();

        assertEquals(illagerAlt, deserializeFont(Font.MINECRAFT_ILLAGERALT, "font/illageralt.json"));

        // include/space.json
        final Font includeSpace = Font.font()
                .key(Key.key("include/space"))
                .addProvider(FontProvider.space()
                        .advance(" ", 4)
                        .advance("\u200c", 0)
                        .build())
                .build();

        assertEquals(includeSpace, deserializeFont(Key.key("include/space"), "font/include/space.json"));

        // include/unifont.json
        final Font includeUnifont = Font.font()
                .key(Key.key("include/unifont"))
                .addProvider(FontProvider.unihex()
                        .file(Key.key("font/unifont.zip"))
                        .sizes(Arrays.asList(
                                UnihexFontProvider.SizeOverride.of(
                                        "\u3001".codePointAt(0),
                                        "\u30FF".codePointAt(0),
                                        0,
                                        15
                                ),
                                UnihexFontProvider.SizeOverride.of(
                                        "\u3200".codePointAt(0),
                                        "\u9FFF".codePointAt(0),
                                        0,
                                        15
                                ),
                                UnihexFontProvider.SizeOverride.of(
                                        "\uAC00".codePointAt(0),
                                        "\uD7AF".codePointAt(0),
                                        0,
                                        14
                                ),
                                UnihexFontProvider.SizeOverride.of(
                                        "\uF900".codePointAt(0),
                                        "\uFAFF".codePointAt(0),
                                        0,
                                        15
                                ),
                                UnihexFontProvider.SizeOverride.of(
                                        "\uFF01".codePointAt(0),
                                        "\uFF5E".codePointAt(0),
                                        0,
                                        15
                                )
                        ))
                        .build())
                .build();

        assertEquals(includeUnifont, deserializeFont(Key.key("include/unifont"), "font/include/unifont.json"));
    }

    private static @NotNull Font deserializeFont(final @NotNull Key key, final @NotNull String path) throws IOException {
        return FontSerializer.INSTANCE.deserialize(
                Readable.resource(
                        FontDeserializationTest.class.getClassLoader(),
                        path
                ),
                key
        );
    }
}
