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
package team.unnamed.creative.serialize.minecraft.resourcepack;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;
import team.unnamed.creative.texture.Texture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ResourcePackEqualityTest {
    @Test
    void test() {
        assertResourcePackZipsToTheSameFile(
                "Test with pack meta only",
                pack -> pack.packMeta(30, "This is the a resource-pack")
        );

        assertResourcePackZipsToTheSameFile(
                "Test with pack meta and textures",
                pack -> pack.packMeta(18, "This is another resource-pack"),
                pack -> pack.texture(Texture.texture(Key.key("minecraft:test_texture"), Writable.EMPTY)),
                pack -> pack.texture(Texture.texture(Key.key("minecraft:another_texture"), Writable.stringUtf8("asddasd")))
        );

        assertResourcePackZipsToTheSameFile(
                "Test with unknown files",
                pack -> pack.packMeta(18, "This is a test with unknown files"),
                pack -> pack.unknownFile("license.txt", Writable.stringUtf8("CC0 License")),
                pack -> pack.unknownFile("credits.txt", Writable.stringUtf8("Unnamed Team"))
        );

        assertResourcePackZipsToTheSameFile(
                "Test with fonts",
                pack -> pack.packMeta(18, "This is a test with fonts"),
                pack -> pack.font(Key.key("minecraft:default"), FontProvider.bitMap(Key.key("file.png"), 8, 6, Arrays.asList("asd", "fjm")))
        );
    }

    @SafeVarargs
    final void assertResourcePackZipsToTheSameFile(String message, Consumer<ResourcePack>... configurerActionsArray) {
        List<Consumer<ResourcePack>> configurerActions = Arrays.asList(configurerActionsArray);

        // todo: maybe ensure different order doesn't affect?

        ResourcePack resourcePack = ResourcePack.resourcePack();
        for (Consumer<ResourcePack> configurer : configurerActions) {
            configurer.accept(resourcePack);
        }

        ResourcePack resourcePack2 = ResourcePack.resourcePack();
        for (Consumer<ResourcePack> configurer : configurerActions) {
            configurer.accept(resourcePack2);
        }

        assertArrayEquals(zip(resourcePack), zip(resourcePack2), "Resource packs are not equal (" + message + ")");
    }

    byte[] zip(ResourcePack resourcePack) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (FileTreeWriter writer = FileTreeWriter.zip(new ZipOutputStream(output, StandardCharsets.UTF_8))) {
            MinecraftResourcePackWriter.minecraft().write(writer, resourcePack);
        }
        return output.toByteArray();
    }

    ResourcePack unzip(byte[] zip) {
        try (FileTreeReader reader = FileTreeReader.zip(new ZipInputStream(new ByteArrayInputStream(zip), StandardCharsets.UTF_8))) {
            return MinecraftResourcePackReader.minecraft().read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
