/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.serialize;

import team.unnamed.uracle.Writable;

/**
 * Represents a file tree, which may be implemented by a
 * real file system with real files, or ZIP files
 *
 * @since 1.0.0
 */
public interface FileTree extends AutoCloseable {

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
     * to write it using the {@link AssetWriter}
     * class
     *
     * @param path The file path
     * @return A new {@link AssetWriter} for the
     * given file
     * @since 1.0.0
     */
    AssetWriter open(String path);

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
     * Closes this file tree, necessary for virtual
     * file trees in a single file, like a ZIP file
     * tree
     *
     * @since 1.0.0
     */
    @Override
    void close();

}
