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
package team.unnamed.creative.serialize.minecraft.fs;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Writable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

interface FileTreeWriterTest {

    FileTreeWriter createWriter() throws IOException;

    void assertWritten() throws IOException;

    @Test
    @DisplayName("Test FileTreeWriter implementation")
    default void test_file_tree_writing() throws IOException {
        try (FileTreeWriter writer = createWriter()) {
            writer.write("file.txt", Writable.stringUtf8("Hello there"));
            writer.write("emptyfile.txt", Writable.EMPTY);
            writer.write("binaryfile.bin", Writable.bytes(new byte[]{(byte) 0xB0, 0x0B}));

            try (JsonWriter jsonWriter = new JsonWriter(writer.openWriter("file.json"))) {
                jsonWriter.beginObject()
                        .name("libraryMainDeveloper").value("yusshu")
                        .name("license").value("MIT")
                        .name("year").value(2023)
                        .endObject();
            }

            writer.write("dir/file.txt", Writable.stringUtf8("File in directory"));
            writer.write("dir/subdir/noext", Writable.stringUtf8("File without extension in subdirectory"));

            assertTrue(writer.exists("file.txt"));
            assertTrue(writer.exists("emptyfile.txt"));
            assertTrue(writer.exists("binaryfile.bin"));
            assertTrue(writer.exists("file.json"));
            assertTrue(writer.exists("dir/file.txt"));
            assertTrue(writer.exists("dir/subdir/noext"));
            assertFalse(writer.exists("thisFileDoesNotExist.json"));
            assertFalse(writer.exists("image.png"));
            assertFalse(writer.exists("dir/image.gif"));
        }

        assertWritten();
    }

}
