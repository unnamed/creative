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
package team.unnamed.creative.serialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

final class DirectoryFileTree implements FileTree {

    private final File root;
    private OutputStream stream;

    DirectoryFileTree(File root) {
        this.root = root;
    }

    @Override
    public boolean exists(String path) {
        return getFile(path).exists();
    }

    @Override
    public OutputStream openStream(String path) {

        // close previous stream if it has
        // not been closed yet
        close();

        File file = getFile(path);

        if (file.exists()) {
            throw new IllegalStateException("File " + path + " already exists!");
        } else {
            createFile(file);
        }

        try {
            return stream = new FileOutputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            stream = null;
        }
    }

    private File getFile(String path) {
        return new File(root, path);
    }

    private void createFile(File file) {
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
