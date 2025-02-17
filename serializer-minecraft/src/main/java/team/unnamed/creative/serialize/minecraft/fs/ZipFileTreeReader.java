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
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Readable;
import team.unnamed.creative.base.Writable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.Objects.requireNonNull;

final class ZipFileTreeReader implements FileTreeReader {
    private final ZipFile zipFile;
    private final Enumeration<? extends ZipEntry> entries;

    private final Collection<WeakReference<ZipFileEntryReadable>> createdContent = new HashSet<>();

    private @Nullable ZipEntry currentEntry;
    private @Nullable ZipEntry nextEntry;

    ZipFileTreeReader(final @NotNull ZipFile zipFile) {
        this.zipFile = requireNonNull(zipFile, "zipFile");
        this.entries = zipFile.entries();
        this.next0();
    }

    private void next0() {
        nextEntry = null;
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                nextEntry = entry;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextEntry != null;
    }

    @Override
    public @NotNull String next() {
        if (nextEntry == null) {
            throw new NoSuchElementException();
        }
        this.currentEntry = nextEntry;
        this.next0();
        return currentEntry.getName();
    }

    @Override
    public @NotNull InputStream stream() {
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
    public @NotNull Readable content() {
        if (this.currentEntry == null) {
            throw new IllegalStateException("No current entry, call next() first");
        }
        final ZipFileEntryReadable readable = new ZipFileEntryReadable(currentEntry);
        createdContent.add(new WeakReference<>(readable));
        return readable;
    }

    @Override
    public void close() throws IOException {
        // memoize created content before closing
        for (final WeakReference<ZipFileEntryReadable> ref : createdContent) {
            final ZipFileEntryReadable readable = ref.get();
            if (readable != null) {
                // only memoize if the reference to this readable is still valid
                readable.memoize();
            }
        }

        // close zip file
        zipFile.close();
    }

    private class ZipFileEntryReadable implements Readable {
        private final ZipEntry entry;
        private byte @Nullable [] memoized;

        ZipFileEntryReadable(final @NotNull ZipEntry entry) {
            this.entry = requireNonNull(entry, "entry");
        }

        @Override
        public @NotNull InputStream open() throws IOException {
            if (memoized != null) {
                return new ByteArrayInputStream(memoized);
            } else {
                return zipFile.getInputStream(entry);
            }
        }

        @Override
        public @NotNull Writable asWritable() {
            if (memoized != null) {
                return Writable.bytes(memoized);
            } else {
                return Readable.super.asWritable();
            }
        }

        void memoize() {
            if (memoized != null) {
                // already memoized!
                return;
            }
            this.memoized = Readable.super.readAsByteArray();
        }

        @Override
        public byte @NotNull [] readAsByteArray() {
            if (memoized != null) {
                return memoized.clone();
            } else {
                return Readable.super.readAsByteArray();
            }
        }

        @Override
        public @NotNull String readAsUTF8String() throws IOException {
            if (memoized != null) {
                return new String(memoized, StandardCharsets.UTF_8);
            } else {
                return Readable.super.readAsUTF8String();
            }
        }
    }
}
