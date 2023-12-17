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
import team.unnamed.creative.base.Readable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public interface FileTreeReader extends AutoCloseable {

    boolean hasNext();

    String next();

    /**
     * Returns the current entry's data as an input stream.
     *
     * <p>The returned input stream has a short lifetime and
     * will be closed when next() is called again.</p>
     *
     * <p>To store any content of the entry use the {@link #content()}
     * method.</p>
     *
     * @return The current entry's data as an input stream
     * @since 1.3.0
     */
    @NotNull InputStream stream();

    /**
     * Returns the current entry's data as a {@link Readable}.
     *
     * <p>The returned readable instance can be used to store
     * the entry's content.</p>
     *
     * @return The current entry's data as a readable
     * @since 1.3.0
     */
    default @NotNull Readable content() {
        return Readable.copyInputStream(stream());
    }

    @Override
    void close() throws IOException;

    static FileTreeReader zip(ZipInputStream zip) {
        return new ZipInputStreamFileTreeReader(zip);
    }

    /**
     * Creates a new {@link FileTreeReader} from the given {@link ZipFile}.
     *
     * @param zipFile The zip file to read
     * @return The created file tree reader
     * @since 1.3.0
     */
    static @NotNull FileTreeReader zip(final @NotNull ZipFile zipFile) {
        return new ZipFileTreeReader(zipFile);
    }

    static FileTreeReader directory(File root) {
        return new DirectoryFileTreeReader(root);
    }

}
