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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;

class MinecraftResourcePackDeserializeTest {
    @Test
    @DisplayName("Test downloading & deserializing the Minecraft default resource-pack")
    void test() throws Exception {
        final String ref = "1.20.2";
        final URL url = new URL("https://github.com/InventivetalentDev/minecraft-assets/zipball/refs/heads/" + ref);

        // download file from "url" to a temporary file
        System.out.println("Downloading resource-pack...");
        File file = null;
        try {
            file = File.createTempFile("minecraft-resource-pack-" + ref, ".zip");
            try (final OutputStream output = Files.newOutputStream(file.toPath())) {
                Writable.inputStream(url::openStream).write(output);
            }
        } finally {
            if (file != null) {
                file.deleteOnExit();
            }
        }
        System.out.println("Downloaded! (" + file.length() + " bytes)");
        System.out.println("Deserializing...");

        // read from temporary file
        try {
            final ResourcePack resourcePack = MinecraftResourcePackReader.minecraft().readFromZipFile(file);
            System.out.println("Deserialized! There are:");
            System.out.println("  " + resourcePack.textures().size() + " textures");
            System.out.println("  " + resourcePack.models().size() + " models");
            System.out.println("  " + resourcePack.sounds().size() + " sounds");
            System.out.println("  " + resourcePack.languages().size() + " languages");
            System.out.println("  " + resourcePack.fonts().size() + " fonts");
            System.out.println("  " + resourcePack.unknownFiles().size() + " unknown files (check)");
        } catch (final Exception e) {
            Assertions.fail("Exception thrown while deserializing", e);
        } catch (final Error e) {
            Assertions.fail("Error thrown while deserializing", e);
        }
    }
}
