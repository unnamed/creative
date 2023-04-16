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
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeReader;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;
import team.unnamed.creative.util.Keys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import static team.unnamed.creative.serialize.minecraft.MinecraftResourcePackStructure.*;

final class MinecraftResourcePackReader implements ResourcePackReader  {

    private static final JsonParser PARSER = new JsonParser();

    private final FileTreeReader reader;
    private @Nullable String current;
    private final Map<Key, Texture> texturesWaitingMetadata = new HashMap<>();
    private @Nullable Texture currentTexture = null;

    MinecraftResourcePackReader(FileTreeReader reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasNext() {
        return reader.hasNext() || !texturesWaitingMetadata.isEmpty();
    }

    @Override
    public void next() {
        if (reader.hasNext()) {
            this.current = reader.next();
        } else if (texturesWaitingMetadata.isEmpty()) {
            throw new NoSuchElementException("No more elements");
        } else {
            Iterator<Texture> textures = texturesWaitingMetadata.values().iterator();
            this.currentTexture = textures.next();
            textures.remove();
        }
    }

    @Override
    public ElementType type() {

        if (current == null) {
            throw new IllegalStateException("Execute next() at least once!");
        }

        if (currentTexture != null) {
            return ElementType.TEXTURE;
        }

        Queue<String> tokens = MinecraftResourcePackStructure.tokenize(current);

        if (tokens.isEmpty()) {
            throw new IllegalStateException("Token collection is empty!");
        }

        if (tokens.size() == 1) {
            // top level file
            switch (tokens.poll()) {
                case MinecraftResourcePackStructure.PACK_METADATA_FILE:
                    return ElementType.PACK_METADATA;
                case MinecraftResourcePackStructure.PACK_ICON_FILE:
                    return ElementType.PACK_ICON;
                default:
                    return ElementType.UNKNOWN;
            }
        }

        String folder = tokens.poll();

        if (!folder.equals(MinecraftResourcePackStructure.ASSETS_FOLDER)) {
            // not assets?
            return ElementType.UNKNOWN;
        }

        if (tokens.isEmpty()) {
            // assets was a file?
            return ElementType.UNKNOWN;
        }

        // then the next token is a namespace
        String namespace = tokens.poll();
        if (tokens.isEmpty()) {
            // found a file like assets/<file>
            // when it should be assets/<namespace>/...
            return ElementType.UNKNOWN;
        }

        if (!Keys.isValidNamespace(namespace)) {
            return ElementType.UNKNOWN;
        }

        String category = tokens.poll();

        if (tokens.isEmpty()) {
            // found a file instead of a category folder
            if (category.equals(MinecraftResourcePackStructure.SOUNDS_FILE)) {
                return ElementType.SOUND_REGISTRY;
            } else {
                // TODO: gpu_warnlist.json?
                return ElementType.UNKNOWN;
            }
        }

        String value = MinecraftResourcePackStructure.path(tokens);

        switch (category) {
            case MinecraftResourcePackStructure.MODELS_FOLDER: {
                if (!value.endsWith(OBJECT_EXTENSION)) {
                    return ElementType.UNKNOWN;
                } else {
                    return ElementType.MODEL;
                }
            }
            case MinecraftResourcePackStructure.TEXTURES_FOLDER: {
                if (value.endsWith(METADATA_EXTENSION)) {
                    return ElementType.TEXTURE_METADATA;
                } else {
                    return ElementType.TEXTURE;
                }
            }
            case MinecraftResourcePackStructure.SOUNDS_FOLDER: {
                if (!value.endsWith(SOUND_EXTENSION)) {
                    return ElementType.UNKNOWN;
                } else {
                    return ElementType.SOUND;
                }
            }
            case MinecraftResourcePackStructure.FONTS_FOLDER: {
                if (!value.endsWith(OBJECT_EXTENSION)) {
                    return ElementType.UNKNOWN;
                } else {
                    return ElementType.FONT;
                }
            }
            case MinecraftResourcePackStructure.LANGUAGES_FOLDER: {
                if (!value.endsWith(OBJECT_EXTENSION)) {
                    return ElementType.UNKNOWN;
                } else {
                    return ElementType.LANGUAGE;
                }
            }

            case MinecraftResourcePackStructure.BLOCKSTATES_FOLDER: {
                if (!value.endsWith(OBJECT_EXTENSION)) {
                    return ElementType.UNKNOWN;
                } else {
                    return ElementType.BLOCK_STATE;
                }
            }
            default:
                return ElementType.UNKNOWN;
        }
    }

    @Override
    public Writable icon() {
        return copyCurrentData();
    }

    @Override
    public Metadata metadata() {
        // currentKey(TEXTURES_FOLDER, METADATA_EXTENSION)
        return SerializerMetadata.INSTANCE.readFromTree(
                parse(reader.input()),
                currentKey(null, METADATA_EXTENSION)
        );
    }

    @Override
    public BlockState blockState() {
        return SerializerBlockState.INSTANCE.readFromTree(
                parse(reader.input()),
                currentKey(MinecraftResourcePackStructure.BLOCKSTATES_FOLDER, OBJECT_EXTENSION)
        );
    }

    @Override
    public Font font() {
        return SerializerFont.INSTANCE.readFromTree(
                parse(reader.input()),
                currentKey(MinecraftResourcePackStructure.FONTS_FOLDER, OBJECT_EXTENSION)
        );
    }

    @Override
    public Language language() {
        return SerializerLanguage.INSTANCE.readFromTree(
                parse(reader.input()),
                currentKey(MinecraftResourcePackStructure.LANGUAGES_FOLDER, OBJECT_EXTENSION)
        );
    }

    @Override
    public Model model() {
        return SerializerModel.INSTANCE.readFromTree(
                parse(reader.input()),
                currentKey(MODELS_FOLDER, OBJECT_EXTENSION)
        );
    }

    @Override
    public SoundRegistry soundRegistry() {
        return SerializerSoundRegistry.INSTANCE.readFromTree(
                parse(reader.input()),
                currentNamespace()
        );
    }

    @Override
    public Sound.File sound() {
        return Sound.File.of(
                currentKey(SOUNDS_FOLDER, SOUND_EXTENSION),
                copyCurrentData()
        );
    }

    @Override
    public Texture texture() {
        return Texture.of(
                currentKey(TEXTURES_FOLDER, null),
                copyCurrentData()
        );
    }

    @Override
    public Map.Entry<String, Writable> unknown() {
        return new AbstractMap.SimpleEntry<>(current, copyCurrentData());
    }

    private Queue<String> tokens() {
        if (current == null) {
            throw new IllegalStateException("Invoke next() at least once");
        }
        return MinecraftResourcePackStructure.tokenize(current);
    }

    private String currentNamespace(Queue<String> tokens) {
        if (tokens.isEmpty()) {
            // should never happen
            throw new IllegalStateException("Tokens queue is empty");
        } else if (tokens.poll().equals(MinecraftResourcePackStructure.ASSETS_FOLDER) && !tokens.isEmpty()) {
            String namespace = tokens.poll();

            if (!Keys.isValidNamespace(namespace)) {
                throw new IllegalStateException("Path '" + current
                        + "' has an invalid namespace '" + namespace + "'");
            }

            return namespace;
        }

        throw new IllegalStateException("Cannot get namespace from path: " + current);
    }

    private String currentNamespace() {
        return currentNamespace(tokens());
    }

    private Key currentKey(@Nullable String category, @Nullable String extension) {
        Queue<String> tokens = tokens();
        @Subst("minecraft")
        String namespace = currentNamespace(tokens);
        String folder = tokens.poll();

        if (folder != null && folder.equals(category)) {
            @Subst("value")
            String value = MinecraftResourcePackStructure.path(tokens);

            if (extension != null) {
                // remove expected extension
                value = value.substring(0, value.length() - extension.length());
            }

            return Key.key(namespace, value);
        } else {
            throw new IllegalStateException("Unexpected category folder: '"
                    + folder + "', expected: '" + category + "', for file: '" + current + "'");
        }
    }

    private Writable copyCurrentData() {
        return writableFromInputStreamCopy(reader.input());
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
