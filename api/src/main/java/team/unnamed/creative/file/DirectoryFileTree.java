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
import team.unnamed.creative.metadata.Metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

final class DirectoryFileTree
        implements FileTree {

    private final Set<String> names = new HashSet<>();

    private final File root;
    private ResourceWriter writer;

    DirectoryFileTree(File root, boolean clear) {
        this.root = root;
        if (clear) {
            Streams.deleteContents(root);
        }
    }

    @Override
    public boolean exists(String path) {
        return names.contains(path);
    }

    @Override
    public ResourceWriter open(String path) {

        if (writer != null) {
            // close previous writer in case
            // it has not been closed yet
            Streams.closeUnchecked(writer);
        }

        writer = new ResourceWriter(openStream(path));
        names.add(path);
        return writer;
    }

    @Override
    public void write(String path, Writable data) {
        try (OutputStream output = openStream(path)) {
            data.write(output);
            names.add(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(FileResource resource) {
        try (ResourceWriter writer = open(resource.path())) {
            resource.serialize(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Metadata meta = resource.meta();
        if (!meta.parts().isEmpty()) {
            try (ResourceWriter writer = open(resource.metaPath())) {
                meta.serialize(writer);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
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

    private OutputStream openStream(String path) {
        File file = getFile(path);

        if (names.contains(path)) {
            throw new IllegalStateException(
                    "File " + path + " already"
                            + "exists!"
            );
        }

        if (file.exists()) {
            file.delete();
        } else {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }

        try {
            file.createNewFile();
            return new FileOutputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
