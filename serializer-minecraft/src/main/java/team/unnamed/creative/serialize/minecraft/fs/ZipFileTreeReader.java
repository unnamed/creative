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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.Objects.requireNonNull;

final class ZipFileTreeReader implements FileTreeReader {
    private final ZipFile zipFile;
    private final Enumeration<? extends ZipEntry> entries;
    private @Nullable ZipEntry currentEntry;

    ZipFileTreeReader(final @NotNull ZipFile zipFile) {
        this.zipFile = requireNonNull(zipFile, "zipFile");
        this.entries = zipFile.entries();
    }

    @Override
    public boolean hasNext() {
        return entries.hasMoreElements();
    }

    @Override
    public @NotNull String next() {
        return (this.currentEntry = entries.nextElement()).getName();
    }

    @Override
    public @NotNull InputStream input() {
        if (this.currentEntry == null) {
            throw new IllegalStateException("No current entry, call next() first");
        }
        try {
            return zipFile.getInputStream(currentEntry);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to get input stream for current entry: " + currentEntry, e);
        }
    }

    @Override
    public void close() throws IOException {
        zipFile.close();
    }
}
