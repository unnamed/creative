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

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

final class ZipInputStreamFileTreeReader implements FileTreeReader {

    private final ZipInputStream zip;
    private ZipEntry current;
    private boolean consumed;

    public ZipInputStreamFileTreeReader(ZipInputStream zip) {
        this.zip = zip;
    }

    private void nextEntry() {
        try {
            ZipEntry entry;
            do {
                entry = zip.getNextEntry();
            } while (entry != null && entry.isDirectory());
            current = entry;
            consumed = false;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (current == null) {
            if (consumed) {
                return false;
            } else {
                nextEntry();
                return current != null;
            }
        } else if (consumed) {
            nextEntry();
            return current != null;
        } else {
            // not null and not consumed, can call
            // next() and should remain the same
            return true;
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        consumed = true;
        return current.getName();
    }

    @Override
    public @NotNull InputStream stream() {
        return new FilterInputStream(zip) {
            @Override
            public void close() throws IOException {
                // do not close the zip stream
                zip.closeEntry();
            }
        };
    }

    @Override
    public void close() throws IOException {
        zip.close();
    }
}
