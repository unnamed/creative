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
package team.unnamed.creative.base;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * Interface for representing data that can be read
 * via a {@link InputStream} such as resources, files,
 * bytes in memory, etc. without having them always
 * loaded
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface Readable {

    /**
     * Opens this readable object with a {@link InputStream},
     * this method can be called anytime, it should be consistent
     * with its data and return it exactly every time.
     *
     * <p>Implementations should be state-less and not necessarily
     * thread-safe</p>
     *
     * @return The input stream to be read
     * @throws IOException If opening fails
     * @since 1.0.0
     */
    @NotNull InputStream open() throws IOException;

    /**
     * Opens, reads and writes this {@link Readable} information
     * and transfers/writes it to the given {@link OutputStream}
     *
     * <p>Note that this operation <strong>won't close</strong> the
     * given {@link OutputStream}</p>
     *
     * @param output The output stream to transfer
     * @since 1.0.0
     */
    default void readAndWrite(final @NotNull OutputStream output) {
        requireNonNull(output, "output");
        try (final InputStream input = this.open()) {
            final byte[] buf = new byte[Writable.DEFAULT_BUFFER_LENGTH];
            int len;
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to read and write", e);
        }
    }

    /**
     * Opens and reads this {@link Readable} instance to
     * a byte array, it is recommended to cache the returned
     * result if you need to re-use it, since this method
     * reads the data every time it is called
     *
     * @return The data in a byte array
     * @since 1.0.0
     */
    default byte @NotNull [] readAsByteArray() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        readAndWrite(output);
        return output.toByteArray();
    }

    /**
     * Opens and reads this {@link Readable} instance to
     * an UTF-8 string, it is recommended to cache the returned
     * result if you need to re-use it, since this method
     * (may) read the data every time is called
     *
     * @return This readable data, as a string
     * @throws IOException If conversion fails
     * @since 1.0.0
     */
    default @NotNull String readAsUTF8String() throws IOException {
        return new String(readAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * this {@link Readable} data.
     *
     * <p>The returned writable instance will open the readable
     * everytime it needs to write the data, and will then write
     * it to the output stream.</p>
     *
     * @return The writable representation
     * @since 1.3.0
     */
    default @NotNull Writable asWritable() {
        return Writable.inputStream(this::open);
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the named resource at the specified class loader
     *
     * @param loader The class loader that holds the resource
     * @param name   The full resource name
     * @return The {@link Readable} representation
     * @since 1.0.0
     */
    static @NotNull Readable resource(final @NotNull ClassLoader loader, final @NotNull String name) {
        requireNonNull(loader, "loader");
        requireNonNull(name, "name");
        return () -> {
            final InputStream input = loader.getResourceAsStream(name);
            if (input == null) {
                throw new IOException("Resource not found: " + name);
            }
            return input;
        };
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the named resource at the caller class loader.
     *
     * @param name The full resource name
     * @return The {@link Readable} representation
     * @since 1.1.0
     * @deprecated Use {@link #resource(ClassLoader, String)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @CallerSensitive
    static @NotNull Readable resource(final @NotNull String name) {
        requireNonNull(name, "name");
        final Class<?> caller = Reflection.getCallerClass();
        final ClassLoader classLoader = caller.getClassLoader();
        return resource(classLoader, name);
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the given {@link File}, which will be opened every time
     * {@link Readable#open()} is called
     *
     * @param file The wrapped file, must exist
     * @return The {@link Readable} representation for this file
     * @since 1.0.0
     */
    static @NotNull Readable file(final @NotNull File file) {
        requireNonNull(file, "file");
        return () -> Files.newInputStream(file.toPath());
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the given {@link Path}, which will be opened every time
     * {@link Readable#open()} is called
     *
     * @param path    The file path
     * @param options The options {@link Files#newInputStream}
     * @return The {@link Readable} representation for this path
     * @since 1.0.0
     */
    static @NotNull Readable path(final @NotNull Path path, final @NotNull OpenOption @NotNull ... options) {
        requireNonNull(path, "path");
        requireNonNull(options, "options");
        return () -> Files.newInputStream(path, options);
    }

    /**
     * Creates a new {@link Readable} instance that will copy,
     * save and return the given {@link InputStream} data. In
     * order to preserve the data, it reads the input stream as
     * a byte array when this method is called.
     *
     * @param inputStream The input stream to copy
     * @return The {@link Readable} representation
     * @since 1.0.0
     */
    static @NotNull Readable copyInputStream(final @NotNull InputStream inputStream) {
        requireNonNull(inputStream, "inputStream");
        // read the input stream as a byte array (kinda hacky)
        final byte[] bytes = ((Readable) (() -> inputStream)).readAsByteArray();
        return new Readable() {

            @Override
            public @NotNull InputStream open() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public byte @NotNull [] readAsByteArray() {
                return bytes.clone();
            }

            @Override
            public @NotNull String readAsUTF8String() {
                return new String(bytes, StandardCharsets.UTF_8);
            }

            @Override
            public String toString() {
                return "Readable.copyInputStream";
            }
        };
    }

    /**
     * Creates a new {@link Readable} instance representing
     * the given byte array, which is read via a {@link ByteArrayInputStream}
     * when required
     *
     * @param bytes The wrapped bytes
     * @return The {@link Readable} representation
     * @since 1.0.0
     */
    static @NotNull Readable bytes(final byte @NotNull [] bytes) {
        requireNonNull(bytes, "bytes");
        final byte[] b = bytes.clone();
        return new Readable() {

            @Override
            public @NotNull InputStream open() {
                return new ByteArrayInputStream(b);
            }

            @Override
            public byte @NotNull [] readAsByteArray() {
                return b.clone();
            }

            @Override
            public @NotNull String readAsUTF8String() {
                return new String(b, StandardCharsets.UTF_8);
            }

        };
    }

    /**
     * Creates a new {@link Readable} instance representing
     * the given string, which is read via a {@link ByteArrayInputStream}
     * when needed
     *
     * @param string The wrapped string
     * @return The {@link Readable representation}
     * @since 1.0.0
     */
    static @NotNull Readable stringUtf8(final @NotNull String string) {
        requireNonNull(string, "string");
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return new Readable() {

            @Override
            public @NotNull InputStream open() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public byte @NotNull [] readAsByteArray() {
                return bytes.clone();
            }

            @Override
            public @NotNull String readAsUTF8String() {
                return string;
            }

            @Override
            public String toString() {
                return "Readable { type='utf8', value='" + string + "' }";
            }

        };
    }

}
