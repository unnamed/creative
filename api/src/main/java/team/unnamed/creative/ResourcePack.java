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
package team.unnamed.creative;

import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.file.FileTreeWriter;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a built server-side resource-pack ready
 * to be sent to a player, contains the resource-pack
 * content bytes (of the resource-pack ZIP archive)
 * and its SHA-1 hash
 *
 * @since 1.0.0
 */
public final class ResourcePack {

    private final byte[] bytes;
    private final String hash;

    public ResourcePack(
            byte[] bytes,
            String hash
    ) {
        this.bytes = requireNonNull(bytes, "bytes");
        this.hash = requireNonNull(hash, "hash");
    }

    /**
     * Returns the resource-pack zip archive
     * bytes
     *
     * @return The resource-pack file data
     */
    public byte[] bytes() {
        return bytes;
    }

    /**
     * Returns the SHA-1 hash of the
     * resource-pack
     *
     * @return The SHA-1 hash of the
     * resource-pack
     */
    public String hash() {
        return hash;
    }

    public static ResourcePack build(Iterable<? extends FileTreeWriter> writers) {

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot find SHA-1 algorithm");
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (FileTree tree = FileTree.zip(new ZipOutputStream(new DigestOutputStream(output, digest)))) {
            for (FileTreeWriter writer : writers) {
                writer.write(tree);
            }
        }

        byte[] hash = digest.digest();
        StringBuilder hexHash = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            int part1 = (b >> 4) & 0xF;
            int part2 = b & 0xF;
            hexHash
                    .append(hex(part1))
                    .append(hex(part2));
        }

        return new ResourcePack(output.toByteArray(), hexHash.toString());
    }

    public static ResourcePack build(FileTreeWriter... writers) {
        return build(Arrays.asList(writers));
    }

    @Deprecated
    public static ResourcePack build() {
        return build(Collections.emptyList());
    }

    private static char hex(int c) {
        return "0123456789abcdef".charAt(c);
    }

}
