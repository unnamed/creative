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

import com.google.gson.stream.JsonReader;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePackBuilder;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.serialize.minecraft.io.FileTreeReader;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.METADATA_EXTENSION;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.TEXTURE_EXTENSION;

public final class MinecraftResourcePackReader {

    private MinecraftResourcePackReader() {
    }

    public void read(
            FileTreeReader treeReader,
            ResourcePackBuilder output
    ) throws IOException {

        Map<Key, Texture> waitingForMeta = new HashMap<>();

        while (treeReader.hasNext()) {
            String path = treeReader.next();
            InputStream inputStream = treeReader.input();

            Queue<String> tokens = new LinkedList<>(Arrays.asList(path.split("/")));

            if (tokens.isEmpty()) {
                // INVALID!
                continue;
            }

            // top level files
            if (tokens.size() == 1) {
                switch (tokens.poll()) {
                    case MinecraftResourcePackStructure.PACK_METADATA_FILE: {

                        Metadata metadata = SerializerMetadata.INSTANCE.read(new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));

                        // pack meta read
                        PackMeta packMeta = metadata.meta(PackMeta.class);
                        if (packMeta == null) {
                            System.err.println("Pack meta is null!");
                        } else {
                            output.meta(packMeta);
                            System.out.println("Pack format: " + packMeta.format() + ", Description: " + packMeta.description());
                        }

                        // filter meta read
                        FilterMeta filterMeta = metadata.meta(FilterMeta.class);
                        if (filterMeta != null) {
                            output.filter(filterMeta);
                        }

                        // language meta read
                        LanguageMeta languageMeta = metadata.meta(LanguageMeta.class);
                        if (languageMeta != null) {
                            output.languageRegistry(languageMeta);
                        }

                        // todo: check for extra (and invalid) metadata parts
                        break;
                    }
                    case MinecraftResourcePackStructure.PACK_ICON_FILE: {
                        System.out.println("Found pack icon");
                        output.icon(Writable.copyInputStream(inputStream));
                        break;
                    }
                    default: {
                        System.out.println("Unknown top level file: " + path);
                        // unknown top-level file, maybe some
                        // credits.txt file?
                        output.file(path, Writable.copyInputStream(inputStream));
                        break;
                    }
                }
                continue;
            }

            // has folders
            String folder = tokens.poll();
            if (folder.equals(MinecraftResourcePackStructure.ASSETS_FOLDER)) {
                if (tokens.isEmpty()) {
                    // assets was a file?
                    System.err.println("Is 'assets' a file?");
                    continue;
                }

                // then the next token is a namespace
                String namespace = tokens.poll();
                if (tokens.isEmpty()) {
                    // found a file like assets/<file>
                    // when it should be assets/<namespace>/...
                    System.err.println("Found a file in the namespaces folder: " + path);
                    continue;
                }

                // TODO: validate namespace

                String category = tokens.poll();
                if (tokens.isEmpty()) {
                    if (category.equals("sounds.json")) {
                        SoundRegistry soundRegistry = SerializerSoundRegistry.INSTANCE.readFromTree(
                                LazyTypeAdapter.PARSER.parse(new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))),
                                namespace
                        );
                        output.sounds(soundRegistry);
                        System.out.println("Found sounds.json file for namespace "
                                + namespace + " with " + soundRegistry.sounds().size() + " sound entries");
                    } else {
                        // TODO: gpu_warnlist.json?
                        System.err.println("Found a file in the namespace root folder");
                    }
                    continue;
                }

                switch (category) {
                    case MinecraftResourcePackStructure.MODELS_FOLDER: {
                        String keyValue = String.join("/", tokens);
                        Key key = Key.key(namespace, keyValue);
                        System.out.println("Found a model with key: " + key);
                        break;
                    }
                    case MinecraftResourcePackStructure.TEXTURES_FOLDER: {
                        String keyValue = String.join("/", tokens);
                        if (keyValue.endsWith(METADATA_EXTENSION)) {
                            // a metadata file
                            Key key = Key.key(keyValue.substring(0, keyValue.length() - METADATA_EXTENSION.length()));
                            Metadata meta = SerializerMetadata.INSTANCE.read(new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
                            Texture texture = waitingForMeta.remove(key);

                            if (texture != null) {
                                // texture was found first
                                texture = Texture.of(key, texture.data(), meta);
                                output.texture(texture);
                            } else {
                                // meta was found first, wait
                                waitingForMeta.put(key, Texture.of(
                                        key,
                                        Writable.EMPTY,
                                        meta
                                ));
                            }

                            System.out.println("Found metadata for texture " + key);
                        } else if (keyValue.endsWith(TEXTURE_EXTENSION)) {
                            // a texture file
                            Key key = Key.key(keyValue.substring(0, keyValue.length() - TEXTURE_EXTENSION.length()));
                            Writable data = Writable.copyInputStream(inputStream);

                            Texture texture = waitingForMeta.remove(key);

                            if (texture != null) {
                                // meta was found first
                                texture = Texture.of(key, data, texture.meta());
                                output.texture(texture);
                            } else {
                                // texture was found first
                                waitingForMeta.put(key, Texture.of(
                                        key,
                                        data
                                ));
                            }

                            System.out.println("Found a texture " + key);
                        } else {
                            // unknown!
                            System.err.println("Unknown file found in textures folder: " + path);
                        }
                        break;
                    }
                    case MinecraftResourcePackStructure.SOUNDS_FOLDER: {
                        break;
                    }
                    case MinecraftResourcePackStructure.FONTS_FOLDER: {
                        break;
                    }
                    case MinecraftResourcePackStructure.LANGUAGES_FOLDER: {
                        break;
                    }
                    case MinecraftResourcePackStructure.BLOCKSTATES_FOLDER: {
                        break;
                    }
                    default: {
                        System.out.println("Unknown category: " + category);
                    }
                }
            } else {
                // unknown folder, just add it as an unknown file
                System.out.println("Unknown complex file: " + path);
                output.file(path, Writable.copyInputStream(inputStream));
            }
        }

        // write textures that do not have metadata (they waited and didn't found a meta :c)
        for (Texture texture : waitingForMeta.values()) {
            Metadata metadata = texture.meta();
            if (metadata.parts().isEmpty()) {
                // expected
                output.texture(texture);
            } else {
                // in this metadata is not empty and texture is, weird!
                System.err.println("Found an unpaired texture metadata file for: " + texture.key());
            }
        }
    }

    public static MinecraftResourcePackReader minecraft() {
        return new MinecraftResourcePackReader();
    }

}
