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
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;
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

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.*;

final class MinecraftResourcePackReaderImpl implements MinecraftResourcePackReader {

    static final MinecraftResourcePackReaderImpl INSTANCE = new MinecraftResourcePackReaderImpl();

    private static final JsonParser PARSER = new JsonParser();

    private MinecraftResourcePackReaderImpl() {
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public ResourcePack read(FileTreeReader reader) {
        ResourcePack resourcePack = ResourcePack.create();

        // textures that are waiting for metadata, or metadata
        // waiting for textures (because we can't know the order
        // they come in)
        Map<Key, Texture> incompleteTextures = new HashMap<>();

        while (reader.hasNext()) {
            String path = reader.next();
            InputStream input = reader.input();

            // tokenize path in sections, e.g.: [ assets, minecraft, textures, ... ]
            Queue<String> tokens = tokenize(path);

            if (tokens.isEmpty()) {
                // this should never happen
                throw new IllegalStateException("Token collection is empty!");
            }

            // single token means the file is on the
            // root level (top level files) so it may be:
            // - pack.mcmeta
            // - pack.png
            if (tokens.size() == 1) {
                switch (tokens.poll()) {
                    case PACK_METADATA_FILE: {
                        // found pack.mcmeta file, deserialize and add
                        Metadata metadata = SerializerMetadata.INSTANCE.readFromTree(parse(input));
                        resourcePack.metadata(metadata);
                        continue;
                    }
                    case PACK_ICON_FILE: {
                        // found pack.png file, add
                        resourcePack.icon(writableFromInputStreamCopy(input));
                        continue;
                    }
                    default: {
                        // unknown top level file
                        resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
                        continue;
                    }
                }
            }

            // if there are two or more tokens, it means the
            // file is inside a folder, in a Minecraft resource
            // pack, the first folder is always "assets"
            String folder = tokens.poll();

            if (!folder.equals(ASSETS_FOLDER)) {
                // not assets! this is an unknown file
                resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
                continue;
            }

            if (tokens.isEmpty()) {
                // should never happen because we know there
                // are more tokens
                throw new IllegalStateException();
            }

            // inside "assets", we should always have a folder
            // with any name, which is a namespace, e.g. "minecraft"
            String namespace = tokens.poll();

            if (!Keys.isValidNamespace(namespace)) {
                // invalid namespace found
                resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
                continue;
            }

            if (tokens.isEmpty()) {
                // found a file directly inside "assets", like
                // assets/<file>, it is not allowed
                resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
                continue;
            }

            // so we already have "assets/<namespace>/", most files inside
            // the namespace folder always have a "category", e.g. textures,
            // lang, font, etc. But not always! There is sounds.json file and
            // gpu_warnlist.json file
            String categoryName = tokens.poll();

            if (tokens.isEmpty()) {
                // this means "category" is a file
                // (remember: last tokens are always files)
                if (categoryName.equals(SOUNDS_FILE)) {
                    // found a sound registry!
                    resourcePack.soundRegistry(SerializerSoundRegistry.INSTANCE.readFromTree(
                            parse(input),
                            namespace
                    ));
                    continue;
                } else {
                    // TODO: gpu_warnlist.json?
                    resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
                    continue;
                }
            }

            // so "category" is actually a category like "textures",
            // "lang", "font", etc. next we can compute the relative
            // path inside the category
            String categoryPath = path(tokens);

            switch (categoryName) {
                case TEXTURES_FOLDER: {
                    String keyOfMetadata = withoutExtension(categoryPath, METADATA_EXTENSION);
                    if (keyOfMetadata != null) {
                        // found metadata for texture
                        Key key = Key.key(namespace, keyOfMetadata);
                        Metadata metadata = SerializerMetadata.INSTANCE.readFromTree(parse(input));

                        Texture texture = incompleteTextures.remove(key);
                        if (texture == null) {
                            // metadata was found first, put
                            incompleteTextures.put(key, Texture.of(key, Writable.EMPTY, metadata));
                        } else {
                            // texture was found before the metadata, nice!
                            resourcePack.texture(texture.meta(metadata));
                        }
                    } else {
                        Key key = Key.key(namespace, categoryPath);
                        Writable data = writableFromInputStreamCopy(input);
                        Texture waiting = incompleteTextures.remove(key);

                        if (waiting == null) {
                            // found texture before metadata
                            incompleteTextures.put(key, Texture.of(key, data));
                        } else {
                            // metadata was found first
                            resourcePack.texture(Texture.of(
                                    key,
                                    data,
                                    waiting.meta()
                            ));
                        }
                    }
                    continue;
                }
                default: {
                    ResourceCategory category = ResourceCategory.getByFolder(categoryName);
                    if (category == null) {
                        // unknown category!
                        break;
                    }
                    String keyValue = withoutExtension(categoryPath, category.extension());
                    if (keyValue == null) {
                        break;
                    }
                    Key key = Key.key(namespace, keyValue);
                    try {
                        Object resource = category.deserializer().deserialize(input, key);
                        category.setter().accept(resourcePack, resource);
                    } catch (IOException e) {
                        throw new UncheckedIOException("Failed to deserialize resource at: '" + path + "'", e);
                    }
                    continue;
                }
            }

            // unknown category or
            // file inside category had a wrong extension
            resourcePack.unknownFile(path, writableFromInputStreamCopy(input));
        }

        for (Texture texture : incompleteTextures.values()) {
            if (texture.data() != Writable.EMPTY) {
                resourcePack.texture(texture);
            }
            // else {
            //     texture placeholder was not completed!
            //     TODO: Should we warn?
            // }
        }
        return resourcePack;
    }

    private static @Nullable String withoutExtension(String string, String extension) {
        if (string.endsWith(extension)) {
            return string.substring(0, string.length() - extension.length());
        } else {
            // string doesn't end with extension
            return null;
        }
    }

    private static Writable writableFromInputStreamCopy(InputStream inputStream) {
        try {
            return Writable.copyInputStream(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create a Writable instance from an InputStream copy", e);
        }
    }

    private static JsonReader reader(InputStream input) {
        return new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    private static JsonElement parse(InputStream input) {
        return PARSER.parse(reader(input));
    }

}
