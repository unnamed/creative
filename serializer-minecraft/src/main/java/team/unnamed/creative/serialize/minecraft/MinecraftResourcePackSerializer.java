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

import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.ResourcePackBuilder;
import team.unnamed.creative.serialize.ResourcePackReader;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.ResourcePackSerializer;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;
import team.unnamed.creative.texture.Texture;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

public final class MinecraftResourcePackSerializer implements ResourcePackSerializer<FileTreeReader, FileTreeWriter> {

    private MinecraftResourcePackSerializer() {
    }

    @Override
    public ResourcePackReader reader(FileTreeReader input) {
        return new MinecraftResourcePackReader(input);
    }

    @Override
    public ResourcePackWriter<?> writer(FileTreeWriter output) {
        return new MinecraftResourcePackWriter(output);
    }

    @Override
    public ResourcePackBuilder builder() {
        return MinecraftResourcePackBuilder.minecraft();
    }

    @Override
    public void pipe(ResourcePackReader reader, ResourcePackWriter<?> writer) {
        Map<Key, Texture> waitingForMeta = new HashMap<>();
        while (reader.hasNext()) {
            reader.next();
            try {
                switch (reader.type()) {
                    case PACK_ICON: {
                        writer.icon(reader.icon());
                        break;
                    }
                    case PACK_METADATA: {
                        writer.metadata(reader.metadata());
                        break;
                    }
                    case LANGUAGE: {
                        writer.language(reader.language());
                        break;
                    }
                    case BLOCK_STATE: {
                        writer.blockState(reader.blockState());
                        break;
                    }
                    case FONT: {
                        writer.font(reader.font());
                        break;
                    }
                    case MODEL: {
                        writer.model(reader.model());
                        break;
                    }
                    case TEXTURE: {
                        // a texture file
                        Texture read = reader.texture();
                        Texture texture = waitingForMeta.remove(read.key());

                        if (texture != null) {
                            // meta was found first
                            writer.texture(read.meta(texture.meta()));
                        } else {
                            // texture was found first
                            waitingForMeta.put(read.key(), read);
                        }
                        break;
                    }
                    case TEXTURE_METADATA: {
                        Map.Entry<Key, Metadata> entry = reader.textureMetadata();
                        Key key = entry.getKey();
                        Metadata meta = entry.getValue();
                        Texture texture = waitingForMeta.remove(key);

                        if (texture != null) {
                            // texture was found first
                            writer.texture(texture.meta(meta));
                        } else {
                            // meta was found first, wait
                            waitingForMeta.put(key, Texture.of(key, Writable.EMPTY, meta));
                        }
                        break;
                    }
                    case UNKNOWN: {
                        Map.Entry<String, Writable> file = reader.unknown();
                        writer.file(file.getKey(), file.getValue());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage() + " for " + reader.type());
            }
        }

        // write textures that do not have metadata (they waited and didn't found a meta :c)
        for (Texture texture : waitingForMeta.values()) {
            Metadata metadata = texture.meta();
            if (metadata.parts().isEmpty()) {
                // expected
                writer.texture(texture);
            } else {
                // in this metadata is not empty and texture is, weird!
                System.err.println("[ERROR] Found an unpaired texture metadata file for: " + texture.key());
            }
        }
    }

    @Override
    public void pipe(ResourcePackBuilder builder, ResourcePackWriter<?> writer) {
        Writable icon = builder.icon();
        if (icon != null) {
            writer.icon(icon);
        }
        Metadata metadata = builder.metadata();
        if (metadata.parts().size() > 0) {
            writer.metadata(metadata);
        }

        for (Language language : builder.languages()) {
            writer.language(language);
        }

        for (BlockState blockState : builder.blockStates()) {
            writer.blockState(blockState);
        }

        for (Font font : builder.fonts()) {
            writer.font(font);
        }

        for (Model model : builder.models()) {
            writer.model(model);
        }

        for (Texture texture : builder.textures()) {
            writer.texture(texture);
        }

        // write unknown files
        for (Map.Entry<String, Writable> entry : builder.unknownFiles().entrySet()) {
            writer.file(entry.getKey(), entry.getValue());
        }
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
