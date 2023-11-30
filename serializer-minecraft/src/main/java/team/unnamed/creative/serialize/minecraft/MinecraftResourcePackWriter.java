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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

public interface MinecraftResourcePackWriter extends ResourcePackWriter<FileTreeWriter> {
    /**
     * Returns the standard {@link MinecraftResourcePackWriter} instance.
     *
     * <p>If any customization is needed, use the {@link #builder()} method
     * instead</p>
     *
     * @return The standard Minecraft resource pack writer instance
     * @since 1.5.0
     */
    static @NotNull MinecraftResourcePackWriter minecraft() {
        return MinecraftResourcePackWriterImpl.INSTANCE;
    }

    /**
     * Returns a new {@link Builder} instance.
     *
     * @return The builder instance
     * @since 1.5.0
     */
    @Contract("-> new")
    static @NotNull Builder builder() {
        return new MinecraftResourcePackWriterImpl.BuilderImpl();
    }

    @Override
    void write(final @NotNull FileTreeWriter tree, final @NotNull ResourcePack resourcePack);

    default void writeToZipFile(Path path, ResourcePack resourcePack) {
        try (ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            write(FileTreeWriter.zip(outputStream), resourcePack);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Failed to write resource pack to zip file: File not found: " + path, e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    default void writeToZipFile(File zipFile, ResourcePack resourcePack) {
        writeToZipFile(zipFile.toPath(), resourcePack);
    }

    default void writeToDirectory(File directory, ResourcePack resourcePack) {
        write(FileTreeWriter.directory(directory), resourcePack);
    }

    default BuiltResourcePack build(ResourcePack resourcePack) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot find SHA-1 algorithm");
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // write resource to zip
        try (FileTreeWriter writer = FileTreeWriter.zip(new ZipOutputStream(new DigestOutputStream(output, digest)))) {
            write(writer, resourcePack);
        }

        byte[] bytes = output.toByteArray();
        String hash;

        // stringify SHA-1 hash
        {
            byte[] hashBytes = digest.digest();
            StringBuilder builder = new StringBuilder(hashBytes.length * 2);
            for (byte b : hashBytes) {
                int part1 = (b >> 4) & 0xF;
                int part2 = b & 0xF;
                builder
                        .append(Character.forDigit(part1, 16))
                        .append(Character.forDigit(part2, 16));
            }
            hash = builder.toString();
        }

        return BuiltResourcePack.of(Writable.bytes(bytes), hash);
    }

    default BuiltResourcePack build(Consumer<ResourcePack> consumer) {
        ResourcePack resourcePack = ResourcePack.create();
        consumer.accept(resourcePack);
        return build(resourcePack);
    }

    /**
     * A builder for {@link MinecraftResourcePackWriter} instances.
     *
     * @since 1.5.0
     */
    interface Builder {
        /**
         * Sets whether the writer should use pretty printing
         * when writing JSON files.
         *
         * @param prettyPrinting Whether the writer should use pretty printing
         *                       when writing JSON files
         * @return This builder
         * @since 1.5.0
         */
        @NotNull Builder prettyPrinting(final boolean prettyPrinting);

        /**
         * Builds a new {@link MinecraftResourcePackWriter} instance.
         *
         * @return The built instance
         * @since 1.5.0
         */
        @Contract("-> new")
        @NotNull MinecraftResourcePackWriter build();
    }
}
