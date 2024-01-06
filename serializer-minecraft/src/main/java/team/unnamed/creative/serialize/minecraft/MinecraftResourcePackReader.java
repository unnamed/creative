/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.ResourcePackReader;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;

public interface MinecraftResourcePackReader extends ResourcePackReader<FileTreeReader> {
    /**
     * Returns the standard {@link MinecraftResourcePackReader} instance.
     *
     * <p>If any customization is needed, use the {@link #builder()} method
     * to change any options.</p>
     *
     * @return The standard Minecraft resource pack reader instance
     * @since 1.0.0
     */
    static @NotNull MinecraftResourcePackReader minecraft() {
        return MinecraftResourcePackReaderImpl.INSTANCE;
    }

    /**
     * Returns a new {@link Builder} instance.
     *
     * @return The builder instance
     * @since 1.3.0
     */
    @Contract("-> new")
    static @NotNull Builder builder() {
        return new MinecraftResourcePackReaderImpl.BuilderImpl();
    }

    @Override
    @NotNull ResourcePack read(final @NotNull FileTreeReader tree);

    /**
     * Reads a {@link ResourcePack} from a ZIP file at the given
     * {@link Path path}.
     *
     * @param path The path to the ZIP file
     * @return The read resource pack
     * @since 1.0.0
     */
    default @NotNull ResourcePack readFromZipFile(final @NotNull Path path) {
        requireNonNull(path, "path");
        return readFromZipFile(path.toFile());
    }

    /**
     * Reads a {@link ResourcePack} from a given ZIP {@link File file}.
     *
     * @param file The ZIP file
     * @return The read resource pack
     * @since 1.0.0
     */
    default @NotNull ResourcePack readFromZipFile(final @NotNull File file) {
        try (final FileTreeReader reader = FileTreeReader.zip(new ZipFile(file))) {
            return read(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Reads a {@link ResourcePack} from a given {@link InputStream}.
     *
     * <p>Note that this method WILL NOT close the given {@code stream}.</p>
     *
     * @param stream The input stream
     * @return The read resource pack
     * @since 1.3.0
     */
    default @NotNull ResourcePack readFromInputStream(final @NotNull InputStream stream) {
        requireNonNull(stream, "stream");
        return read(FileTreeReader.zip(stream instanceof ZipInputStream ? (ZipInputStream) stream : new ZipInputStream(stream, StandardCharsets.UTF_8)));
    }

    default ResourcePack readFromDirectory(File directory) {
        return read(FileTreeReader.directory(directory));
    }

    /**
     * A builder for {@link MinecraftResourcePackReader} instances.
     *
     * @since 1.3.0
     */
    interface Builder {
        /*
         * Makes the reader lenient.
         *
         * <p>A lenient reader will:</p>
         * <ul>
         *     <li>Use a <a href="https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.0/com/google/gson/stream/JsonReader.html#setLenient-boolean-">lenient JSON reader</a></li>
         * </ul>
         *
         * @param lenient Whether the reader should be lenient
         * @return This builder
         * @since 1.3.0
         */
        @Contract("_ -> this")
        @NotNull Builder lenient(final boolean lenient);

        /**
         * Builds a new {@link MinecraftResourcePackReader} instance.
         *
         * @return The built instance
         * @since 1.3.0
         */
        @Contract("-> new")
        @NotNull MinecraftResourcePackReader build();
    }
}
