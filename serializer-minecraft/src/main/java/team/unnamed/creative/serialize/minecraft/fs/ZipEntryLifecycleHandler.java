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

import java.util.zip.ZipEntry;

/**
 * Handler of {@link ZipEntry} lifecycle when using {@link FileTreeWriter}
 * implementation for ZIPs, can handle ZipEntry creation and post-closing
 */
public interface ZipEntryLifecycleHandler {

    ZipEntryLifecycleHandler DEFAULT = new ZipEntryLifecycleHandler() {

        @Override
        public ZipEntry create(String path) {
            ZipEntry entry = ZipEntryLifecycleHandler.super.create(path);
            // ensures that the resulting zip file is always
            // the exact same (because of hashes)
            entry.setTime(0L);
            // tip: if you want to make the resource pack
            // harder to open, you can set an asi extra field
            // using ZipEntry#setExtra(byte[])
            return entry;
        }

    };

    /**
     * Creates a new {@link ZipEntry} with the given
     * {@code path}
     *
     * @param path The {@link ZipEntry} path
     * @return The created zip entry
     */
    default ZipEntry create(String path) {
        return new ZipEntry(path);
    }

    /**
     * Called after the {@link ZipEntry} is closed, useful
     * if you want to add some details to the entry
     *
     * @param entry The closed entry
     */
    default void onClose(ZipEntry entry) {
    }

}
