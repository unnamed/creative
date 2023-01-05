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
package team.unnamed.creative.serialize.minecraft.io;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final class ZipFileTreeWriter implements FileTreeWriter {

    private final Set<String> names = new HashSet<>();
    private final ZipOutputStream output;
    private final Function<String, ZipEntry> entryFactory;

    private ZipEntryOutputStream current;

    ZipFileTreeWriter(ZipOutputStream output, Function<String, ZipEntry> entryFactory) {
        this.output = output;
        this.entryFactory = entryFactory;
    }

    @Override
    public boolean exists(String path) {
        return names.contains(path);
    }

    @Override
    public OutputStream openStream(String path) {

        if (!names.add(path)) {
            throw new IllegalStateException("File " + path + " already exists!");
        }

        try {
            if (current != null) {
                // did you forgor to close it?
                current.close();
            }

            output.putNextEntry(entryFactory.apply(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return current = new ZipEntryOutputStream();
    }

    @Override
    public void write(String path, Writable data) {
        try {
            output.putNextEntry(entryFactory.apply(path));
            data.write(output);
            names.add(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void finish() {
        try {
            output.finish();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        try {
            output.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private class ZipEntryOutputStream extends OutputStream {

        private boolean closed;

        @Override
        public void write(byte @NotNull [] b) throws IOException {
            ensureValid();
            output.write(b);
        }

        @Override
        public void write(byte @NotNull [] b, int off, int len) throws IOException {
            ensureValid();
            output.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            ensureValid();
            output.write(b);
        }

        @Override
        public void flush() throws IOException {
            ensureValid();
            output.flush();
        }

        @Override
        public void close() throws IOException {
            if (!closed) {
                output.closeEntry();
                current = null;
                closed = true;
            }
        }

        private void ensureValid() throws IOException {
            if (closed) {
                if (current != this) {
                    // !!! A new entry output stream was opened,
                    // we are not anymore the current entry os
                    throw new IOException("A new output stream has been " +
                            "opened, this one is no longer usable");
                }
                throw new IOException("Stream closed");
            }
        }

    }

}
