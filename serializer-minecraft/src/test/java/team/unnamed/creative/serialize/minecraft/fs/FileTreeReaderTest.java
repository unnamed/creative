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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.base.Writable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

interface FileTreeReaderTest {

    FileTreeReader createReader() throws IOException;

    @Test
    @DisplayName("Test FileTreeReader implementation")
    default void test_file_tree_reading() throws IOException {
        try (FileTreeReader reader = createReader()) {
            Map<String, Writable> files = new HashMap<>();

            for (int i = 0; i < 20; i++) {
                Assertions.assertTrue(reader.hasNext(), "Reader must have next");
            }

            while (reader.hasNext()) {
                String path = reader.next();
                InputStream input = reader.stream();
                Assertions.assertNull(
                        files.putIfAbsent(path, Writable.copyInputStream(input)),
                        "Path was repeated: " + path
                );
            }

            for (int i = 0; i < 20; i++) {
                Assertions.assertFalse(reader.hasNext(), "Reader must not have next");
            }

            assertEquals(4, files.size());
            assertEquals("Hello, this is a cool file", files.get("file.txt").toUTF8String());
            assertEquals("This is the second file", files.get("dir/file2.txt").toUTF8String());
            assertEquals("This is the third file", files.get("dir/subdir/file3.txt").toUTF8String());
            assertEquals("This is a file without extension", files.get("dir/subdir/filenoext").toUTF8String());
        }

        try (FileTreeReader reader = createReader()) {
            // Test that reader works only using next()
            Set<String> output = new HashSet<>();

            output.add(reader.next());
            output.add(reader.next());
            output.add(reader.next());
            output.add(reader.next());

            assertEquals(4, output.size());
            assertTrue(output.contains("file.txt"));
            assertTrue(output.contains("dir/file2.txt"));
            assertTrue(output.contains("dir/subdir/file3.txt"));
            assertTrue(output.contains("dir/subdir/filenoext"));

            // calling next() again should throw NoSuchElementException
            Assertions.assertThrows(NoSuchElementException.class, reader::next);
        }
    }

}
