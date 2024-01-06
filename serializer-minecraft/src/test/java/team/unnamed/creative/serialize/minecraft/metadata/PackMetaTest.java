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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackMetaTest {

    @Test
    @DisplayName("Test pack meta serialization")
    void test_simple_serialization() {
        PackMeta packMeta = PackMeta.of(
                PackFormat.format(7),
                Component.text("Description!")
        );
        assertEquals(
                "{\"pack_format\":7,\"description\":\"Description!\"}",
                PackMetaCodec.INSTANCE.toJson(packMeta)
        );
    }

    @Test
    @DisplayName("Test pack meta serialization with version range")
    void test_version_range_serialization() {
        PackMeta packMeta = PackMeta.of(
                PackFormat.format(18, 18, 20),
                Component.text("Description!")
        );
        assertEquals(
                "{\"pack_format\":18,\"description\":\"Description!\",\"supported_formats\":[18,20]}",
                PackMetaCodec.INSTANCE.toJson(packMeta)
        );
    }

    @Test
    @DisplayName("Test pack meta serialization with component description")
    void test_complex_description_serialization() {
        PackMeta packMeta = PackMeta.of(
                PackFormat.format(12),
                Component.text()
                        .append(Component.text("Unnamed Team", TextColor.color(0xff8df8)))
                        .append(Component.text(" ftw ", NamedTextColor.GRAY))
                        .append(Component.keybind("i.dont.know"))
                        .build()
        );

        assertEquals(
                "{\"pack_format\":12,\"description\":{\"extra\":[{\"color\":\"#FF8DF8\",\"text\":\"Unnamed Team\"},{\"color\":\"gray\",\"text\":\" ftw \"},{\"keybind\":\"i.dont.know\"}],\"text\":\"\"}}",
                PackMetaCodec.INSTANCE.toJson(packMeta)
        );
    }

    @Test
    @DisplayName("Test pack meta serialization with version range and component description")
    void test_combined_serialization() {
        PackMeta packMeta = PackMeta.of(
                PackFormat.format(18, 18, 20),
                Component.text()
                        .append(Component.text("Unnamed Team", TextColor.color(0xff8df8)))
                        .append(Component.text(" ftw ", NamedTextColor.GRAY))
                        .append(Component.keybind("i.dont.know"))
                        .build()
        );

        assertEquals(
                "{\"pack_format\":18,\"description\":{\"extra\":[{\"color\":\"#FF8DF8\",\"text\":\"Unnamed Team\"},{\"color\":\"gray\",\"text\":\" ftw \"},{\"keybind\":\"i.dont.know\"}],\"text\":\"\"},\"supported_formats\":[18,20]}",
                PackMetaCodec.INSTANCE.toJson(packMeta)
        );
    }

    @Test
    @DisplayName("Test simple pack meta deserialization")
    void test_simple_deserialization() {
        PackMeta packMeta = PackMetaCodec.INSTANCE.fromJson("{\"pack_format\":7,\"description\":\"Description!\"}");
        assertEquals(
                PackMeta.of(
                        PackFormat.format(7),
                        Component.text("Description!")
                ),
                packMeta
        );
    }

    @Test
    @DisplayName("Test pack meta deserialization with version range")
    void test_version_range_deserialization() {
        PackMeta packMeta = PackMetaCodec.INSTANCE.fromJson("{\"pack_format\":18,\"description\":\"Description!\",\"supported_formats\":[18,20]}");
        assertEquals(
                PackMeta.of(
                        PackFormat.format(18, 18, 20),
                        Component.text("Description!")
                ),
                packMeta
        );
    }

    @Test
    @DisplayName("Test pack meta deserialization with component description")
    void test_complex_description_deserialization() {
        PackMeta packMeta = PackMetaCodec.INSTANCE.fromJson("{\"pack_format\":12,\"description\":{\"extra\":[{\"color\":\"#FF8DF8\",\"text\":\"Unnamed Team\"},{\"color\":\"gray\",\"text\":\" ftw \"},{\"keybind\":\"i.dont.know\"}],\"text\":\"\"}}");
        assertEquals(
                PackMeta.of(
                        PackFormat.format(12),
                        Component.text()
                                .append(Component.text("Unnamed Team", TextColor.color(0xff8df8)))
                                .append(Component.text(" ftw ", NamedTextColor.GRAY))
                                .append(Component.keybind("i.dont.know"))
                                .build()
                ),
                packMeta
        );
    }

    @Test
    @DisplayName("Test pack meta deserialization with version range and component description")
    void test_combined_deserialization() {
        PackMeta packMeta = PackMetaCodec.INSTANCE.fromJson("{\"pack_format\":18,\"description\":{\"extra\":[{\"color\":\"#FF8DF8\",\"text\":\"Unnamed Team\"},{\"color\":\"gray\",\"text\":\" ftw \"},{\"keybind\":\"i.dont.know\"}],\"text\":\"\"},\"supported_formats\":[18,20]}");
        assertEquals(
                PackMeta.of(
                        PackFormat.format(18, 18, 20),
                        Component.text()
                                .append(Component.text("Unnamed Team", TextColor.color(0xff8df8)))
                                .append(Component.text(" ftw ", NamedTextColor.GRAY))
                                .append(Component.keybind("i.dont.know"))
                                .build()
                ),
                packMeta
        );
    }

}
