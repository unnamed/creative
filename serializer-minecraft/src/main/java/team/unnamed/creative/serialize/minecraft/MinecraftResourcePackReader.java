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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.ResourcePackWriter;
import team.unnamed.creative.serialize.minecraft.io.FileTreeReader;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.METADATA_EXTENSION;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.OBJECT_EXTENSION;
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.SOUND_EXTENSION;

final class MinecraftResourcePackReader {

    private static final JsonParser PARSER = new JsonParser();

    private MinecraftResourcePackReader() {
    }

    static void read(FileTreeReader treeReader, ResourcePackWriter<?> output) {

        Map<Key, Texture> waitingForMeta = new HashMap<>();
        boolean alertOptiFineNotSupported = true;

        while (treeReader.hasNext()) {
            String path = treeReader.next();
            InputStream inputStream = treeReader.input();

            Queue<String> tokens = MinecraftResourcePackStructure.tokenize(path);

            if (tokens.isEmpty()) {
                // Invalid case, should never happen
                continue;
            }

            // top level files
            if (tokens.size() == 1) {
                switch (tokens.poll()) {
                    // Check for the pack.mcmeta file
                    case MinecraftResourcePackStructure.PACK_METADATA_FILE: {
                        output.metadata(SerializerMetadata.INSTANCE.readFromTree(parse(inputStream)));
                        break;
                    }
                    case MinecraftResourcePackStructure.PACK_ICON_FILE: {
                        output.icon(writableFromInputStreamCopy(inputStream));
                        break;
                    }
                    default: {
                        // unknown top-level file, maybe some credits.txt file?
                        output.file(path, writableFromInputStreamCopy(inputStream));
                        break;
                    }
                }
                continue;
            }

            // has folders
            String folder = tokens.poll();

            // not assets?
            if (!folder.equals(MinecraftResourcePackStructure.ASSETS_FOLDER)) {
                System.err.println(" [WARN] Loaded an unknown file: " + path);
                output.file(path, writableFromInputStreamCopy(inputStream));
                continue;
            }

            // assets was a file?
            if (tokens.isEmpty()) {
                System.err.println("[ERROR] The 'assets' folder is a file, it must be a folder");
                continue;
            }

            // then the next token is a namespace
            String namespace = tokens.poll();
            if (tokens.isEmpty()) {
                // found a file like assets/<file>
                // when it should be assets/<namespace>/...
                System.err.println(" [WARN] Found a file in the namespaces folder: " + path);
                continue;
            }

            // validate namespace
            if (!Keys.isValidNamespace(namespace)) {
                System.err.println("[ERROR] Found an invalid namespace name: " + namespace);
                continue;
            }

            String category = tokens.poll();

            // found a file instead of a category folder
            if (tokens.isEmpty()) {
                if (category.equals(MinecraftResourcePackStructure.SOUNDS_FILE)) {
                    SoundRegistry soundRegistry = SerializerSoundRegistry.INSTANCE.readFromTree(parse(inputStream), namespace);
                    output.soundRegistry(soundRegistry);
                } else {
                    // TODO: gpu_warnlist.json?
                    System.err.println(" [WARN] Found a file in the namespace root folder: " + path);
                }
                continue;
            }

            // alert about OptiFine support
            if (category.equals(MinecraftResourcePackStructure.OPTIFINE_FOLDER)) {
                if (alertOptiFineNotSupported) {
                    System.err.println(" [WARN] Found OptiFine files, remember that creative doesn't currently support them");
                    alertOptiFineNotSupported = false;
                }
                output.file(path, writableFromInputStreamCopy(inputStream));
                continue;
            }

            String value = MinecraftResourcePackStructure.path(tokens);

            switch (category) {
                case MinecraftResourcePackStructure.MODELS_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Unknown file in models folder: " + path);
                        break;
                    }

                    Model model = SerializerModel.INSTANCE.readFromTree(
                            PARSER.parse(reader(inputStream)),
                            Key.key(namespace, stripExtension(value))
                    );
                    output.model(model);
                    break;
                }

                case MinecraftResourcePackStructure.TEXTURES_FOLDER: {
                    if (value.endsWith(METADATA_EXTENSION)) {
                        // a metadata file
                        Key key = Key.key(namespace, value.substring(0, value.length() - METADATA_EXTENSION.length()));
                        Metadata meta = SerializerMetadata.INSTANCE.readFromTree(PARSER.parse(reader(inputStream)));
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
                    } else {
                        // a texture file
                        Key key = Key.key(namespace, value.substring(0, value.length()));
                        Writable data = writableFromInputStreamCopy(inputStream);

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
                    }
                    break;
                }

                case MinecraftResourcePackStructure.SOUNDS_FOLDER: {

                    if (!value.endsWith(SOUND_EXTENSION)) {
                        System.err.println(" [WARN] Found an unknown file in sounds folder: " + path);
                        break;
                    }

                    Sound.File sound = Sound.File.of(
                            Key.key(namespace, value.substring(0, value.length() - SOUND_EXTENSION.length())),
                            writableFromInputStreamCopy(inputStream)
                    );
                    output.sound(sound);
                    break;
                }

                case MinecraftResourcePackStructure.FONTS_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Found an unknown file in fonts folder: " + path);
                        break;
                    }

                    Font font = SerializerFont.INSTANCE.readFromTree(
                            PARSER.parse(reader(inputStream)),
                            Key.key(namespace, value.substring(0, value.length() - OBJECT_EXTENSION.length()))
                    );
                    output.font(font);
                    break;
                }

                case MinecraftResourcePackStructure.LANGUAGES_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Found an unknown file in languages folder: " + path);
                        break;
                    }

                    Language language = SerializerLanguage.INSTANCE.readFromTree(
                            PARSER.parse(reader(inputStream)),
                            Key.key(namespace, value.substring(0, value.length() - OBJECT_EXTENSION.length()))
                    );
                    output.language(language);
                    break;
                }

                case MinecraftResourcePackStructure.BLOCKSTATES_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Found an unknown file in blockstates folder: " + path);
                        break;
                    }

                    Key key = Key.key(namespace, value.substring(0, value.length() - OBJECT_EXTENSION.length()));
                    BlockState blockState = SerializerBlockState.INSTANCE.readFromTree(
                            PARSER.parse(reader(inputStream)),
                            key
                    );
                    output.blockState(blockState);
                    break;
                }
                default: {
                    output.file(path, writableFromInputStreamCopy(inputStream));
                    break;
                }
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
                System.err.println("[ERROR] Found an unpaired texture metadata file for: " + texture.key());
            }
        }
    }

    private static Writable writableFromInputStreamCopy(InputStream inputStream) {
        try {
            return Writable.copyInputStream(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create a Writable instance from an InputStream copy", e);
        }
    }

    private static String stripExtension(String path) {
        String[] parts = path.split(Pattern.quote("."));
        StringJoiner joiner = new StringJoiner(".");
        for (int i = 0; i < parts.length - 1; i++) {
            joiner.add(parts[i]);
        }
        return joiner.toString();
    }

    private static JsonReader reader(InputStream input) {
        return new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    private static JsonElement parse(InputStream input) {
        return PARSER.parse(reader(input));
    }

}
