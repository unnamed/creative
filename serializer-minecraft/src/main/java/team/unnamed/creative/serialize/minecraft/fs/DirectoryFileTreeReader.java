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

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

final class DirectoryFileTreeReader implements FileTreeReader {

    private final File root;
    private final List<File> folders = new ArrayList<>();
    private int folderCursor;
    private File @Nullable [] files;
    private int fileCursor;

    private File current;
    private InputStream currentStream;

    DirectoryFileTreeReader(File root) {
        this.root = root;
        folders.add(root);
    }

    @Override
    public boolean hasNext() {
        while (true) {
            if (files == null) {
                // we must look for a folder and make
                // files be equal to its children
                if (folderCursor >= folders.size()) {
                    // no more folders
                    return false;
                }

                File folder = folders.get(folderCursor++);
                files = folder.listFiles();

                if (files == null) {
                    throw new IllegalStateException("Null children from file " + folder);
                }
            }

            while (fileCursor < files.length) {
                File file = files[fileCursor];
                if (file.isDirectory()) {
                    folders.add(file);
                    fileCursor++;
                } else {
                    return true;
                }
            }
            files = null;
            fileCursor = 0;
        }
    }

    @Override
    public String next() {
        if (currentStream != null) {
            Streams.closeUnchecked(currentStream);
            currentStream = null;
        }

        if (files == null || fileCursor >= files.length) {
            throw new NoSuchElementException("No more elements");
        } else {
            current = files[fileCursor++];
            try {
                currentStream = new FileInputStream(current);
            } catch (IOException e) {
                throw new IllegalStateException("Couldn't open InputStream for: " + current, e);
            }
            return relativize(root, current);
        }
    }

    @Override
    public InputStream input() {
        return currentStream;
    }

    @Override
    public void close() {
        if (currentStream != null) {
            Streams.closeUnchecked(currentStream);
            currentStream = null;
        }
    }

    private static String relativize(File parent, File child) {
        StringBuilder builder = new StringBuilder();
        builder.append(child.getName());

        while ((child = child.getParentFile()) != null) {
            if (parent.equals(child)) {
                // finished!
                return builder.toString();
            }
            // prepend current folder to the string builder
            builder.insert(0, child.getName() + File.separator);
        }
        throw new IllegalStateException("");
    }

}
