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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

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
    InputStream open() throws IOException;

    /**
     * Opens, reads and writes this {@link Readable} information
     * and transfers/writes it to the given {@link OutputStream}
     *
     * <p>Note that this operation <strong>won't close</strong> the
     * given {@link OutputStream}</p>
     *
     * @param output The output stream to transfer
     * @throws IOException If transferring fails
     */
    default void readAndWrite(OutputStream output) throws IOException {
        try (InputStream input = this.open()) {
            byte[] buf = new byte[Writable.DEFAULT_BUFFER_LENGTH];
            int len;
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        }
    }

    /**
     * Opens and reads this {@link Readable} instance to
     * a byte array, it is recommended to cache the returned
     * result if you need to re-use it, since this method
     * reads the data every time it is called
     *
     * @return The data in a byte array
     * @throws IOException If conversion fails
     */
    default byte[] readAsByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
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
     */
    default String readAsUTF8String() throws IOException {
        return new String(readAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the named resource at the specified class loader
     *
     * @param loader The class loader that holds the resource
     * @param name The full resource name
     * @return The {@link Readable} representation
     * @since 1.0.0
     */
    static Readable resource(ClassLoader loader, String name) {
        return () -> loader.getResourceAsStream(name);
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the given {@link File}, which will be opened every time
     * {@link Readable#open()} is called
     *
     * @param file The wrapped file, must exist
     * @return The {@link Readable} representation for this file
     */
    static Readable file(File file) {
        return () -> new FileInputStream(file);
    }

    /**
     * Creates a new {@link Readable} instance that represents
     * the given {@link Path}, which will be opened every time
     * {@link Readable#open()} is called
     *
     * @param path The file path
     * @param options The options {@link Files#newInputStream}
     * @return The {@link Readable} representation for this path
     */
    static Readable path(Path path, OpenOption... options) {
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
     * @throws IOException If reading the input stream fails
     */
    static Readable copyInputStream(InputStream inputStream) throws IOException {
        byte[] bytes = ((Readable) (() -> inputStream)).readAsByteArray();
        return new Readable() {

            @Override
            public InputStream open() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public byte[] readAsByteArray() {
                return bytes.clone();
            }

            @Override
            public String readAsUTF8String() {
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
     */
    static Readable bytes(byte[] bytes) {
        byte[] b = bytes.clone();
        return new Readable() {

            @Override
            public InputStream open() {
                return new ByteArrayInputStream(b);
            }

            @Override
            public byte[] readAsByteArray() {
                return b.clone();
            }

            @Override
            public String readAsUTF8String() {
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
     */
    static Readable stringUtf8(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return new Readable() {

            @Override
            public InputStream open() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public byte[] readAsByteArray() {
                return bytes.clone();
            }

            @Override
            public String readAsUTF8String() {
                return string;
            }

            @Override
            public String toString() {
                return "Readable { type='utf8', value='" + string + "' }";
            }

        };
    }

}
