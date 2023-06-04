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
package team.unnamed.creative.serialize.minecraft;

import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.ResourcePackReader;
import team.unnamed.creative.serialize.minecraft.errorHandler.DeserializationErrorHandler;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;

public interface MinecraftResourcePackReader extends ResourcePackReader<FileTreeReader> {

    @Override
    ResourcePack read(FileTreeReader tree);

    default ResourcePack readFromZipFile(Path path) {
        try (ZipInputStream inputStream = new ZipInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            return read(FileTreeReader.zip(inputStream));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    default ResourcePack readFromZipFile(File zipFile) {
        return readFromZipFile(zipFile.toPath());
    }

    default ResourcePack readFromDirectory(File directory) {
        return read(FileTreeReader.directory(directory));
    }

    static MinecraftResourcePackReader minecraft() {
        return MinecraftResourcePackReaderImpl.INSTANCE;
    }

    @SuppressWarnings("PatternValidation")
    ResourcePack read(FileTreeReader reader, DeserializationErrorHandler errorHandler);
}
