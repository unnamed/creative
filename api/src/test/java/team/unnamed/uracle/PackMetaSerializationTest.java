/*
 * This file is part of uracle, licensed under the MIT license
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
package team.unnamed.uracle;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.uracle.metadata.language.LanguageEntry;
import team.unnamed.uracle.metadata.pack.PackMeta;
import team.unnamed.uracle.mock.MockTreeOutputStream;
import team.unnamed.uracle.serialize.DefaultResourcePackSerializer;

import java.util.HashMap;
import java.util.Map;

public class PackMetaSerializationTest {

    @Test
    @DisplayName("Test that PackInfo is correctly serialized")
    public void test_serialize_info() {
        MockTreeOutputStream stream = new MockTreeOutputStream();
        ResourcePackWriter builder = new DefaultResourcePackSerializer(stream);

        PackMeta info = PackMeta.of(7, "Hello world");
        builder.meta(PackMeta.of(info));

        Assertions.assertEquals(
                "pack.mcmeta:\n" +
                        "{" +
                            "\"pack\":{" +
                                "\"format\":7," +
                                "\"description\":\"Hello world\"" +
                            "}" +
                        "}\n",
                stream.output()
        );
    }

    @Test
    @DisplayName("Test that PackInfo and LanguageEntry's are correctly serialized")
    public void test_serialize_meta() {
        MockTreeOutputStream stream = new MockTreeOutputStream();
        ResourcePackWriter builder = new DefaultResourcePackSerializer(stream);

        PackMeta info = PackMeta.of(7, "Hello world");
        Map<Key, LanguageEntry> languages = new HashMap<Key, LanguageEntry>() {{
            put(Key.key("uracle:es_test"), LanguageEntry.builder()
                    .name("Test Language")
                    .region("Somewhere")
                    .bidirectional(false)
                    .build());
        }};

        builder.meta(PackMeta.of(info, languages));

        Assertions.assertEquals(
                "pack.mcmeta:\n" +
                "{" +
                    "\"pack\":{" +
                        "\"format\":7," +
                        "\"description\":\"Hello world\"" +
                    "}," +
                    "\"language\":{" +
                        "\"uracle:es_test\":{" +
                            "\"name\":\"Test Language\"," +
                            "\"region\":\"Somewhere\"," +
                            "\"bidirectional\":false" +
                        "}" +
                    "}" +
                "}\n",
                stream.output()
        );
    }

}
