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
import team.unnamed.creative.ResourcePackBuilder;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.model.Model;
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
import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.TEXTURE_EXTENSION;

final class MinecraftResourcePackReader {

    private static final JsonParser PARSER = new JsonParser();

    private MinecraftResourcePackReader() {
    }

    static void read(FileTreeReader treeReader, ResourcePackBuilder output) {
        try {
            _read(treeReader, output);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void _read(
            FileTreeReader treeReader,
            ResourcePackBuilder output
    ) throws IOException {

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

                        Metadata metadata = SerializerMetadata.INSTANCE.readFromTree(parse(inputStream));

                        // pack meta read
                        PackMeta packMeta = metadata.meta(PackMeta.class);
                        if (packMeta == null) {
                            System.err.println("[ERROR] The pack.mcmeta file didn't contain the 'pack' section (Which is required)");
                        } else {
                            output.meta(packMeta);
                            System.out.println(" [INFO] Loaded pack metadata (pack.mcmeta file). Format: "
                                    + packMeta.format() + ". Description: '" + packMeta.description() + "'");
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
                        output.icon(Writable.copyInputStream(inputStream));
                        System.out.println(" [INFO] Loaded resource pack icon (pack.png file)");
                        break;
                    }
                    default: {
                        System.err.println(" [WARN] Loaded an unknown top level file: " + path);
                        // unknown top-level file, maybe some credits.txt file?
                        output.file(path, Writable.copyInputStream(inputStream));
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
                output.file(path, Writable.copyInputStream(inputStream));
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
                    output.sounds(soundRegistry);
                    System.out.println(" [INFO] Found sound registry for '" + namespace
                            + "' with " + soundRegistry.sounds().size() + " sound entries");
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
                output.file(path, Writable.copyInputStream(inputStream));
                continue;
            }

            String value = MinecraftResourcePackStructure.path(tokens);

            switch (category) {
                case MinecraftResourcePackStructure.MODELS_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Unknown file in models folder: " + path);
                        break;
                    }

                    Model model = SerializerModel.INSTANCE.readModel(
                            PARSER.parse(reader(inputStream)),
                            Key.key(namespace, stripExtension(value))
                    );
                    output.model(model);
                    break;
                }

                case MinecraftResourcePackStructure.TEXTURES_FOLDER: {
                    if (value.endsWith(TEXTURE_EXTENSION + METADATA_EXTENSION)) {
                        // a metadata file
                        Key key = Key.key(namespace, value.substring(0, value.length() - TEXTURE_EXTENSION.length() - METADATA_EXTENSION.length()));
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
                    } else if (value.endsWith(TEXTURE_EXTENSION)) {
                        // a texture file
                        Key key = Key.key(namespace, value.substring(0, value.length() - TEXTURE_EXTENSION.length()));
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
                    } else {
                        System.err.println(" [WARN] Found an unknown file in textures folder: " + path);
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
                            Writable.copyInputStream(inputStream)
                    );
                    output.sound(sound);
                    System.out.println(" [INFO] Loaded sound: " + sound.key());
                    break;
                }

                case MinecraftResourcePackStructure.FONTS_FOLDER: {

                    if (!value.endsWith(OBJECT_EXTENSION)) {
                        System.err.println(" [WARN] Found an unknown file in fonts folder: " + path);
                        break;
                    }

                    Font font = SerializerFont.INSTANCE.readFont(
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
                    System.err.println(" [WARN] Unknown category: " + category);
                    output.file(path, Writable.copyInputStream(inputStream));
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

        System.out.println(" [INFO] Loaded " + output.fonts().size() + " fonts");
        System.out.println(" [INFO] Loaded " + output.blockStates().size() + " blockstates");
        System.out.println(" [INFO] Loaded " + output.textures().size() + " textures");
        System.out.println(" [INFO] Loaded " + output.models().size() + " models");
        System.out.println(" [INFO] Loaded " + output.languages().size() + " languages");
        System.out.println(" [INFO] Loaded " + output.extraFiles().size() + " extra files");
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
