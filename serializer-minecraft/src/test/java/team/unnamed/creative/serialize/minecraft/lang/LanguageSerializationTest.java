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
package team.unnamed.creative.serialize.minecraft.lang;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Readable;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.serialize.minecraft.language.LanguageSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageSerializationTest {

    @Test
    @DisplayName("Test Language JSON Serialization")
    void test_serialize() throws Exception {
        Language language = Language.language()
                .key(Key.key("minecraft", "en_us"))
                .translation("multiplayer.stopSleeping", "Leave Bed")
                .translation("multiplayer.title", "Play Multiplayer")
                .translation("chat.copy", "Copy to Clipboard")
                .build();
        assertEquals(
                "{\"multiplayer.stopSleeping\":\"Leave Bed\",\"multiplayer.title\":\"Play Multiplayer\",\"chat.copy\":\"Copy to Clipboard\"}",
                LanguageSerializer.INSTANCE.serializeToJsonString(language)
        );
    }

    @Test
    @DisplayName("Test Language JSON Deserialization from resources")
    void test_deserialization() throws Exception {
        Key key = Key.key("minecraft", "en_us");
        Language language = LanguageSerializer.INSTANCE.deserialize(
                Readable.resource(LanguageSerializationTest.class.getClassLoader(), "en_us_lang.json"),
                key
        );

        assertEquals(key, language.key(), "The language key should be " + key);
        assertEquals(6217, language.translations().size(), "There should be 6217 translations in en_us_lang.json");
        assertEquals("Leave Bed", language.translation("multiplayer.stopSleeping"));
        assertEquals("Play Multiplayer", language.translation("multiplayer.title"));
        assertEquals("Copy to Clipboard", language.translation("chat.copy"));
    }

}
