/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectoryFileTreeReaderTest implements FileTreeReaderTest {

    @Override
    public FileTreeReader createReader() {
        return FileTreeReader.directory(new File("src/test/resources/folder"));
    }

    @Test
    @DisplayName("Test that a directory is always read in the same order")
    void test_read_order_is_sorted_by_name(final @TempDir Path root) throws IOException {
        // written in an order that is not alphabetical, so a reader that follows
        // the file system order is unlikely to match the expected list below
        Files.write(root.resolve("z.txt"), "z".getBytes());
        Files.write(root.resolve("m.txt"), "m".getBytes());
        Files.write(root.resolve("a.txt"), "a".getBytes());

        final Path sub = Files.createDirectory(root.resolve("sub"));
        Files.write(sub.resolve("y.txt"), "y".getBytes());
        Files.write(sub.resolve("b.txt"), "b".getBytes());

        final List<String> paths = new ArrayList<>();
        try (FileTreeReader reader = FileTreeReader.directory(root.toFile())) {
            while (reader.hasNext()) {
                paths.add(reader.next());
            }
        }

        assertEquals(Arrays.asList("a.txt", "m.txt", "z.txt", "sub/b.txt", "sub/y.txt"), paths);
    }

}
