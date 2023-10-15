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

import org.jetbrains.annotations.NotNull;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * Interface for representing objects that can
 * be written to a {@link OutputStream}, this
 * class is util for representing assets that
 * can be exported anytime, without necessarily
 * loading them
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface Writable {

    Writable EMPTY = new Writable() {

        @Override
        public void write(final @NotNull OutputStream output) {
        }

        @Override
        public String toString() {
            return "Writable.EMPTY";
        }

    };

    /**
     * Determines the default buffer size used when
     * copying data from an input stream to an output
     * stream
     */
    int DEFAULT_BUFFER_LENGTH = 1024;

    /**
     * Writes this object information to a
     * {@link OutputStream}, this method can be
     * called anytime, it should be consistent
     * with its data and generate the exact same
     * output for the same input (attributes) of
     * the implementation
     *
     * @param output The target output stream
     * @throws IOException If write fails
     * @since 1.0.0
     */
    void write(final @NotNull OutputStream output) throws IOException;

    /**
     * Converts this {@link Writable} instance to a byte
     * array, it is not recommended invoking this method
     * so often
     *
     * @return This writable instance as a byte array
     * @throws IOException If conversion fails
     */
    default byte @NotNull [] toByteArray() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(output);
        return output.toByteArray();
    }

    /**
     * Converts this {@link Writable} instance to an UTF-8
     * string, it is not recommended invoking this method so
     * often, it exists only for easing debugging
     *
     * @return This writable instance as an UTF-8 string
     * @throws IOException If conversion fails
     */
    default @NotNull String toUTF8String() throws IOException {
        return new String(toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the named resource at the specified class loader
     *
     * @param loader The class loader that holds the resource
     * @param name   The full resource name
     * @return The {@link Writable} representation
     * @since 1.0.0
     */
    static @NotNull Writable resource(final @NotNull ClassLoader loader, final @NotNull String name) {
        requireNonNull(loader, "loader");
        requireNonNull(name, "name");
        return inputStream(() -> {
            final InputStream resource = loader.getResourceAsStream(name);
            if (resource == null) {
                throw new IOException("Resource not found: " + name);
            }
            return resource;
        });
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the named resource at the caller class loader.
     *
     * @param name The full resource name
     * @return The {@link Writable} representation
     * @since 1.1.0
     */
    @CallerSensitive
    static @NotNull Writable resource(final @NotNull String name) {
        requireNonNull(name, "name");
        final Class<?> caller = Reflection.getCallerClass();
        final ClassLoader classLoader = caller.getClassLoader();
        return resource(classLoader, name);
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the given {@link File}, which will be copied to the
     * given {@link OutputStream} when calling {@link Writable#write}
     *
     * @param file The wrapped file, must exist
     * @return The {@link Writable} representation for this file
     * @since 1.0.0
     */
    static @NotNull Writable file(final @NotNull File file) {
        requireNonNull(file, "file");
        return inputStream(() -> Files.newInputStream(file.toPath()));
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the given {@link Path}, which will be copied to the given
     * {@link OutputStream} when calling {@link Writable#write}
     *
     * @param path    The file path
     * @param options The options ({@link Files#newInputStream})
     * @return The {@link Writable} representation for this path
     * @since 1.0.0
     */
    static @NotNull Writable path(final @NotNull Path path, final @NotNull OpenOption @NotNull ... options) {
        requireNonNull(path, "path");
        requireNonNull(options, "options");
        return inputStream(() -> Files.newInputStream(path, options));
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the given {@link InputStream} supplier, the supplied
     * input stream will be opened, read and copied every
     * time it is required
     *
     * @param inputStreamSupplier The input stream supplier
     * @return The {@link Writable} representation
     * @since 1.0.0
     */
    static @NotNull Writable inputStream(final @NotNull Callable<InputStream> inputStreamSupplier) {
        requireNonNull(inputStreamSupplier, "inputStreamSupplier");
        return output -> {
            try (final InputStream input = inputStreamSupplier.call()) {
                final byte[] buf = new byte[DEFAULT_BUFFER_LENGTH];
                int len;
                while ((len = input.read(buf)) != -1) {
                    output.write(buf, 0, len);
                }
            } catch (final IOException e) {
                throw e;
            } catch (final Exception e) {
                throw new IOException("Failed to open InputStream", e);
            }
        };
    }

    /**
     * Creates a new {@link Writable} instance that represents
     * the given {@link InputStream} data, in order to preserve
     * the data, it reads the input stream to a byte array when
     * this method is called
     *
     * @param inputStream The input stream to copy
     * @return The {@link Writable} representation
     * @throws IOException If reading the input stream fails
     * @since 1.0.0
     */
    static @NotNull Writable copyInputStream(final @NotNull InputStream inputStream) throws IOException {
        requireNonNull(inputStream, "inputStream");

        // read input stream data to a byte array
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buf = new byte[DEFAULT_BUFFER_LENGTH];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            output.write(buf, 0, len);
        }
        final byte[] bytes = output.toByteArray();
        return new Writable() {

            @Override
            public void write(final @NotNull OutputStream output) throws IOException {
                requireNonNull(output, "output");
                output.write(bytes);
            }

            @Override
            public byte @NotNull [] toByteArray() {
                return bytes.clone();
            }

            @Override
            public String toString() {
                return "Writable.copyInputStream";
            }

        };
    }

    /**
     * Creates a new {@link Writable} instance representing
     * the given byte array, which is written using the
     * {@link OutputStream#write(byte[])}} method
     *
     * @param bytes The wrapped bytes
     * @return The {@link Writable} representation
     * @since 1.0.0
     */
    static @NotNull Writable bytes(final byte @NotNull [] bytes) {
        final byte[] b = bytes.clone();
        return new Writable() {

            @Override
            public void write(final @NotNull OutputStream output) throws IOException {
                requireNonNull(output, "output");
                output.write(b);
            }

            @Override
            public byte @NotNull [] toByteArray() {
                return b.clone();
            }

            @Override
            public String toUTF8String() {
                return new String(b, StandardCharsets.UTF_8);
            }
        };
    }


    /**
     * Creates a new {@link Writable} instance representing
     * the given string, which is written using the UTF-8
     * {@link StandardCharsets#UTF_8} encoding
     *
     * @param string The wrapped string
     * @return The {@link Writable} representation
     * @since 1.0.0
     */
    static @NotNull Writable stringUtf8(final @NotNull String string) {
        requireNonNull(string, "string");
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return new Writable() {

            @Override
            public void write(final @NotNull OutputStream output) throws IOException {
                requireNonNull(output, "output");
                output.write(bytes);
            }

            @Override
            public byte @NotNull [] toByteArray() {
                return bytes.clone();
            }

            @Override
            public @NotNull String toUTF8String() {
                return string;
            }

            @Override
            public String toString() {
                return "Writable { type='utf8', value='" + string + "' }";
            }
        };
    }

}
