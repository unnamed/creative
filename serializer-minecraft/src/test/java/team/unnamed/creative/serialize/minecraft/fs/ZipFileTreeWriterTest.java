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
package team.unnamed.creative.serialize.minecraft.fs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import team.unnamed.creative.base.Writable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZipFileTreeWriterTest implements FileTreeWriterTest {

    private @TempDir Path tempDir;

    private Path zipPath() {
        return tempDir.resolve("test-output.zip");
    }

    @Override
    public FileTreeWriter createWriter() throws IOException {
        return FileTreeWriter.zip(new ZipOutputStream(
                Files.newOutputStream(zipPath()),
                StandardCharsets.UTF_8
        ));
    }

    @Override
    public void assertWritten() throws IOException {
        try (ZipInputStream input = new ZipInputStream(Files.newInputStream(zipPath()), StandardCharsets.UTF_8)) {
            Set<String> missing = new HashSet<>(Arrays.asList(
                    "file.txt",
                    "emptyfile.txt",
                    "binaryfile.bin",
                    "file.json",
                    "dir/file.txt",
                    "dir/subdir/noext"
            ));

            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                Assertions.assertTrue(missing.remove(name), "Repeated or unknown file: " + name);

                byte[] bytes = readBytes(input);
                String string = new String(bytes, StandardCharsets.UTF_8);
                System.out.println(name + " -> " + string);

                switch (name) {
                    case "file.txt": {
                        assertEquals("Hello there", string);
                        break;
                    }
                    case "emptyfile.txt": {
                        assertEquals(0, bytes.length);
                        break;
                    }
                    case "binaryfile.bin": {
                        assertEquals(2, bytes.length);
                        assertEquals((byte) 0xB0, bytes[0]);
                        assertEquals((byte) 0x0B, bytes[1]);
                        break;
                    }
                    case "file.json": {
                        assertEquals(
                                "{\"libraryMainDeveloper\":\"yusshu\",\"license\":\"MIT\",\"year\":2023}",
                                string
                        );
                        break;
                    }
                    case "dir/file.txt": {
                        assertEquals("File in directory", string);
                        break;
                    }
                    case "dir/subdir/noext": {
                        assertEquals("File without extension in subdirectory", string);
                        break;
                    }
                }
            }

            assertEquals(
                    0,
                    missing.size(),
                    "Missing files: " + String.join(", ", missing)
            );
        }
    }

    private static byte[] readBytes(InputStream input) throws IOException {
        return Writable.copyInputStream(input).toByteArray();
    }

}
