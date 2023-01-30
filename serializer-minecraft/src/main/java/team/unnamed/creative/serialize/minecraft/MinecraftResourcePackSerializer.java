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
import team.unnamed.creative.serialize.ResourcePackBuilder;
import team.unnamed.creative.serialize.ResourcePackInput;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.ResourcePackSerializer;
import team.unnamed.creative.serialize.minecraft.io.FileTreeReader;
import team.unnamed.creative.serialize.minecraft.io.FileTreeWriter;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

public final class MinecraftResourcePackSerializer implements ResourcePackSerializer<FileTreeReader, FileTreeWriter> {

    private MinecraftResourcePackSerializer() {
    }

    @Override
    public ResourcePackBuilder builder() {
        return MinecraftResourcePackBuilder.minecraft();
    }

    @Override
    public void deserialize(FileTreeReader reader, ResourcePackWriter<?> output) {
        MinecraftResourcePackReader.read(reader, output);
    }

    @Override
    public ResourcePackBuilder deserialize(FileTreeReader treeReader) {
        ResourcePackBuilder builder = MinecraftResourcePackBuilder.minecraft();
        deserialize(treeReader, builder);
        return builder;
    }

    @Override
    public void serialize(ResourcePackInput resourcePack, FileTreeWriter tree) {
        MinecraftResourcePackWriter.write(resourcePack, tree);
    }

    @Override
    public ResourcePack build(Consumer<ResourcePackBuilder> builder) {

        // build resource-pack
        ResourcePackBuilder resourcePack = builder();
        builder.accept(resourcePack);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot find SHA-1 algorithm");
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // write resource-pack to zip
        try (FileTreeWriter writer = FileTreeWriter.zip(new ZipOutputStream(new DigestOutputStream(output, digest)))) {
            serialize(resourcePack, writer);
        }

        byte[] bytes = output.toByteArray();
        String hash = hex(digest);

        return ResourcePack.of(bytes, hash);
    }

    private static String hex(MessageDigest digest) {
        byte[] bytes = digest.digest();
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int part1 = (b >> 4) & 0xF;
            int part2 = b & 0xF;
            builder
                    .append("0123456789abcdef".charAt(part1))
                    .append("0123456789abcdef".charAt(part2));
        }
        return builder.toString();
    }

    public static MinecraftResourcePackSerializer minecraft() {
        return new MinecraftResourcePackSerializer();
    }

}
