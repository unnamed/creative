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
package team.unnamed.creative.file;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.texture.Texture;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileTreeTest {

    @Test
    @DisplayName("Test DirectoryFileTree implementation")
    public void test_directory_tree(@TempDir File directory) {
        FileTree tree = FileTree.directory(directory);
        test_file_tree(tree);
    }

    @Test
    @DisplayName("Test ZipFileTree implementation")
    public void test_zip_tree() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutput = new ZipOutputStream(output)) {
            FileTree tree = FileTree.zip(zipOutput);
            test_file_tree(tree);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test ZipFileTree implementation using a buffered OutputStream")
    public void test_buffered_zip_tree() throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (FileTree tree = FileTree.zip(new ZipOutputStream(new BufferedOutputStream(output)))) {
            try (ResourceWriter writer = tree.open("test.txt")) {
                writer.startObject()
                        .key("hello").value("there")
                        .key("one").value(1)
                        .endObject();
            }
        }

        byte[] bytes = output.toByteArray();
        Assertions.assertEquals(175, bytes.length);

        try (ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            ZipEntry entry = inputStream.getNextEntry();

            Assertions.assertNotNull(entry, "No entries in the ZIP file");
            Assertions.assertEquals("test.txt", entry.getName());

            byte[] buf = new byte[256];
            int len = inputStream.read(buf);
            Assertions.assertEquals(
                    "{\"hello\":\"there\",\"one\":1}",
                    new String(buf, 0, len, StandardCharsets.UTF_8)
            );

            inputStream.closeEntry();
        }
    }

    public void test_file_tree(FileTree tree) {

        // writes to assets/minecraft/models/item/leather_horse_armor.json
        tree.write(
                Model.builder()
                        .key(Key.key("item/leather_horse_armor"))
                        .parent(Model.ITEM_HANDHELD)
                        .textures(ModelTexture.builder()
                                .layers(Collections.singletonList(
                                        Key.key("item/leather_horse_armor")
                                ))
                                .build())
                        .build()
        );

        // writes assets/creative/textures/texture0.png and
        // assets/creative/textures/texture0.png.mcmeta
        tree.write(
                Texture.builder()
                        .key(Key.key("creative:texture0"))
                        .data(Writable.bytes(new byte[0]))
                        .meta(Metadata.builder()
                                .add(AnimationMeta.builder()
                                        .height(8)
                                        .build())
                                .build())
                        .build()
        );

        String leatherHorseArmormodel = "assets/minecraft/models/item/leather_horse_armor.json";
        String texture0 = "assets/creative/textures/texture0.png";
        String texture0Meta = texture0 + ".mcmeta";

        assertTrue(tree.exists(leatherHorseArmormodel));
        assertTrue(tree.exists(texture0));
        assertTrue(tree.exists(texture0Meta));
    }

}
