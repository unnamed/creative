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
import team.unnamed.creative.lang.Language;

import java.io.IOException;

public class LanguageSerializerTest {

    @Test
    public void test_serialization() throws IOException {

        TestMinecraftFileTree tree = TestMinecraftFileTree.create();

        LanguageSerializer.INSTANCE.write(
                Language.builder()
                        .key(Key.key("minecraft:en_us"))
                        .translation("ok", "ok uwu")
                        .translation("yes", "Yes sir!")
                        .translation("why", "Por que")
                        .build(),
                tree
        );

        tree.expect("assets/minecraft/lang/en_us.json")
                .toBeJson("{\"ok\":\"ok uwu\",\"yes\":\"Yes sir!\",\"why\":\"Por que\"}");


        LanguageSerializer.INSTANCE.write(
                Language.builder()
                        .key(Key.key("creative:es_pe"))
                        .translation("embarrassed", "palteado")
                        .translation("ok", "ya pe")
                        .build(),
                tree
        );

        tree.expect("assets/creative/lang/es_pe.json")
                .toBeJson("{\"embarrassed\":\"palteado\",\"ok\":\"ya pe\"}");

        LanguageSerializer.INSTANCE.write(
                Language.builder()
                        .key(Key.key("idk"))
                        .translation("foo", "bar")
                        .translation("bar", "foo")
                        .build(),
                tree
        );

        tree.expect("assets/minecraft/lang/idk.json")
                .toBeJson("{\"foo\":\"bar\",\"bar\":\"foo\"}");
    }

}
