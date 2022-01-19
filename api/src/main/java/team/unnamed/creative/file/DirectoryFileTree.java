/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.file;

import team.unnamed.creative.base.Writable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

final class DirectoryFileTree
        implements FileTree {

    private final File root;
    private ResourceWriter writer;

    DirectoryFileTree(File root) {
        this.root = root;
    }

    @Override
    public boolean exists(String path) {
        return getFile(path).exists();
    }

    @Override
    public ResourceWriter open(String path) {

        if (writer != null) {
            // close previous writer in case
            // it has not been closed yet
            Streams.closeUnchecked(writer);
        }

        writer = new ResourceWriter(openStream(path));
        return writer;
    }

    @Override
    public void write(String path, Writable data) {
        try (OutputStream output = openStream(path)) {
            data.write(output);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            Streams.closeUnchecked(writer);
        }
    }

    private File getFile(String path) {
        return new File(root, path);
    }

    private void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private OutputStream openStream(String path) {
        File file = getFile(path);

        if (file.exists()) {
            throw new IllegalStateException(
                    "File " + path + " already"
                            + "exists!"
            );
        } else {
            createFile(file);
        }

        try {
            return new FileOutputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
