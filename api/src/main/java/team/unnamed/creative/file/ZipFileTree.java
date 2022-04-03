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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final class ZipFileTree implements FileTree {

    private final Set<String> names = new HashSet<>();
    private final ZipOutputStream output;
    private final ResourceWriter resourceWriter;
    private final Function<String, ZipEntry> entryFactory;

    ZipFileTree(ZipOutputStream output, Function<String, ZipEntry> entryFactory) {
        this.output = output;
        this.entryFactory = entryFactory;
        this.resourceWriter = new ResourceWriter(output) {
            @Override
            public void close() {
                try {
                    output.closeEntry();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

    @Override
    public boolean exists(String path) {
        return names.contains(path);
    }

    @Override
    public ResourceWriter open(String path) {
        try {
            output.putNextEntry(entryFactory.apply(path));
            names.add(path);
            return resourceWriter;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
    public void write(FileResource resource) {
        try {
            String path = resource.path();
            output.putNextEntry(entryFactory.apply(path));
            names.add(path);
            resource.serialize(resourceWriter);

            Metadata meta = resource.meta();
            if (!meta.parts().isEmpty()) {
                path = resource.metaPath();
                output.putNextEntry(entryFactory.apply(path));
                names.add(path);
                meta.serialize(resourceWriter);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        try {
            output.finish();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
