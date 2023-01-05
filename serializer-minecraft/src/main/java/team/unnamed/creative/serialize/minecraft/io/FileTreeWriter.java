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

import com.google.gson.stream.JsonWriter;
import team.unnamed.creative.base.Writable;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Represents a file tree, which may be implemented by a
 * real file system with real files, or ZIP files
 *
 * @since 1.0.0
 */
public interface FileTreeWriter extends AutoCloseable {

    /**
     * Determines if a file exists in this file
     * tree
     *
     * @param path The file path
     * @return True if it exists
     * @since 1.0.0
     */
    boolean exists(String path);

    /**
     * Opens the file at the given path and starts
     * to write it using the {@link ResourceWriter}
     * class
     *
     * @param path The file path
     * @return A new {@link ResourceWriter} for the
     * given file
     * @since 1.0.0
     */
    OutputStream openStream(String path);

    /**
     * Opens the file at the given path and starts
     * to write it using the {@link Writer} class,
     * which is used for character streams
     *
     * @param path The file path
     * @return A new {@link Writer} for the given file
     * @since 1.0.0
     */
    default Writer openWriter(String path) {
        return new OutputStreamWriter(openStream(path), StandardCharsets.UTF_8);
    }

    default JsonWriter openJsonWriter(String path) {
        return new JsonWriter(openWriter(path));
    }

    /**
     * Opens and writes the given data to the
     * specified file path
     *
     * @param path The file path
     * @param data The file data
     * @since 1.0.0
     */
    void write(String path, Writable data);

    /**
     * Finishes writing the file tree without
     * closing the underlying resource, if any
     *
     * @since 1.0.0
     */
    default void finish() {
    }

    /**
     * Closes this file tree, necessary for virtual
     * file trees in a single file, like a ZIP file
     * tree
     *
     * @since 1.0.0
     */
    @Override
    void close();

    /**
     * Creates a new {@link FileTreeWriter} instance for
     * the given {@link File} folder, all files will
     * be written inside it
     *
     * <p>Will delete all the contents of the {@code root}
     * folder if {@code clear} is set to true</p>
     *
     * @param root The root folder
     * @param clear True to delete the folder contents
     * @return The created file tree for the given folder
     */
    static FileTreeWriter directory(File root, boolean clear) {
        return new DirectoryFileTreeWriter(root, clear);
    }

    /**
     * Creates a new {@link FileTreeWriter} instance for
     * the given {@link File} folder, all files will
     * be written inside it
     *
     * <p>Will delete all the contents of the {@code root}
     * folder</p>
     *
     * @param root The root folder
     * @return The created file tree for the given folder
     */
    static FileTreeWriter directory(File root) {
        return directory(root, true);
    }

    /**
     * Creates a new {@link FileTreeWriter} instance for
     * the given {@link ZipOutputStream}, will not
     * be closed
     *
     * <p>Note that the created file tree will never
     * close the given output stream, but it may be
     * finished ({@link ZipOutputStream#finish()})</p>
     *
     * @param zipStream The underlying zip stream
     * @param entryFactory The ZIP archive entry factory
     * @return The file tree for the given zip output
     * stream
     */
    static FileTreeWriter zip(ZipOutputStream zipStream, Function<String, ZipEntry> entryFactory) {
        return new ZipFileTreeWriter(zipStream, entryFactory);
    }

    /**
     * Creates a new {@link FileTreeWriter} instance for
     * the given {@link ZipOutputStream}, will not
     * be closed
     *
     * <p>Note that the created file tree will never
     * close the given output stream, but it may be
     * finished ({@link ZipOutputStream#finish()})</p>
     *
     * @param zipStream The underlying zip stream
     * @return The file tree for the given zip output
     * stream
     */
    static FileTreeWriter zip(ZipOutputStream zipStream) {
        return zip(zipStream, path -> {
            ZipEntry entry = new ZipEntry(path);
            // ensures that the resulting zip file is always
            // the exact same (because of hashes)
            entry.setTime(0L);
            // tip: if you want to make the resource pack
            // harder to open, you can set an asi extra field
            // using ZipEntry#setExtra(byte[])
            return entry;
        });
    }

}
