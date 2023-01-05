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
package team.unnamed.creative;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.serialize.FileTree;
import team.unnamed.creative.base.KeyedMap;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageEntry;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class ResourcePackBuilderImpl implements ResourcePackBuilder {

    private static final String BLOCK_STATES = "blockStates";
    private static final String FONTS = "fonts";
    private static final String LANGUAGES = "languages";
    private static final String MODELS = "models";
    // no key required for sound registries since they are a special case
    // private static final String SOUND_REGISTRIES = "soundRegistries";
    private static final String SOUND_FILES = "soundFiles";
    private static final String TEXTURES = "textures";

    // Metadata
    private @Nullable PackMeta packMeta;
    private @Nullable LanguageMeta languageMeta;
    private @Nullable FilterMeta filterMeta;
    private @Nullable Map<String, MetadataPart> customMetadataParts;

    private final Map<String, KeyedMap<? extends FileResource>> files = new HashMap<>();
    private @Nullable Map<String, SoundRegistry> soundRegistries;

    private @Nullable Map<String, Writable> extraFiles;


    //#region Metadata methods
    // |-----------------------------------|
    // |------- METADATA OPERATIONS -------|
    // |-----------------------------------|
    @Override
    public ResourcePackBuilder meta(PackMeta meta) {
        this.packMeta = requireNonNull(meta, "meta");
        return this;
    }

    @Override
    public ResourcePackBuilder languageEntry(Key key, LanguageEntry languageEntry) {

        Map<Key, LanguageEntry> languages = new HashMap<>();

        if (languageMeta == null) {
            languages.put(key, languageEntry);
        } else {
            languages.putAll(languageMeta.languages());
            languages.put(key, languageEntry);
        }

        languageMeta = LanguageMeta.of(languages);
        return this;
    }

    @Override
    public @Nullable LanguageEntry languageEntry(Key key) {
        if (languageMeta == null) {
            return null;
        }
        return languageMeta.languages().get(key);
    }

    @Override
    public Collection<LanguageEntry> languageEntries() {
        if (languageMeta == null) {
            return Collections.emptySet();
        }
        return languageMeta.languages().values();
    }

    @Override
    public ResourcePackBuilder filter(FilterMeta filter) {
        this.filterMeta = requireNonNull(filter, "filter");
        return this;
    }

    @Override
    public ResourcePackBuilder customMetaPart(MetadataPart part) {
        if (customMetadataParts == null) {
            customMetadataParts = new HashMap<>();
        }
        customMetadataParts.put(part.name(), part);
        return this;
    }
    //#endregion

    @Override
    public ResourcePackBuilder blockState(BlockState state) {
        getOrCreateFor(BLOCK_STATES).put(state);
        return this;
    }

    @Override
    public @Nullable BlockState blockState(Key key) {
        return get(BLOCK_STATES, key);
    }

    @Override
    public Collection<BlockState> blockStates() {
        return getAll(BLOCK_STATES);
    }

    @Override
    public ResourcePackBuilder font(Font font) {
        getOrCreateFor(FONTS).put(font);
        return this;
    }

    @Override
    public ResourcePackBuilder language(Language language) {
        getOrCreateFor(LANGUAGES).put(language);
        return this;
    }

    @Override
    public ResourcePackBuilder model(Model model) {
        getOrCreateFor(MODELS).put(model);
        return this;
    }

    @Override
    public ResourcePackBuilder sounds(SoundRegistry soundRegistry) {
        if (soundRegistries == null) {
            soundRegistries = new HashMap<>();
        }
        soundRegistries.put(soundRegistry.namespace(), soundRegistry);
        return this;
    }

    @Override
    public ResourcePackBuilder sound(Sound.File soundFile) {
        getOrCreateFor(SOUND_FILES).put(soundFile);
        return this;
    }

    @Override
    public ResourcePackBuilder texture(Texture texture) {
        getOrCreateFor(TEXTURES).put(texture);
        return this;
    }

    @Override
    public ResourcePackBuilder file(String path, Writable data) {
        requireNonNull(path, "path");
        requireNonNull(data, "data");
        if (extraFiles == null) {
            extraFiles = new HashMap<>();
        }
        extraFiles.put(path, data);
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writeTo(FileTree tree) {
        if (packMeta == null) {
            throw new IllegalStateException("You must set the resource pack meta (ResourcePackBuilder#meta)");
        }

        // write our pack.mcmeta file
        {
            Metadata.Builder metaBuilder = Metadata.builder()
                    .add(packMeta);

            if (filterMeta != null) {
                metaBuilder.add(filterMeta);
            }

            if (customMetadataParts != null) {
                for (MetadataPart part : customMetadataParts.values()) {
                    metaBuilder.add((Class) part.getClass(), part);
                }
            }

            tree.write(metaBuilder.build());
        }

        for (KeyedMap<?> map : files.values()) {
            tree.write(map.values());
        }

        // write sound registries
        if (soundRegistries != null) {
            tree.write(soundRegistries.values());
        }

        // write extra files
        if (extraFiles != null) {
            tree.write(extraFiles);
        }
    }

    // helper methods
    @SuppressWarnings("unchecked")
    private <T extends Keyed & FileResource> KeyedMap<T> getOrCreateFor(String key) {
        return (KeyedMap<T>) files.computeIfAbsent(key, k -> new KeyedMap<>());
    }

    private <T extends Keyed & FileResource> @Nullable T get(String key, Key objectKey) {
        @SuppressWarnings("unchecked")
        KeyedMap<T> map = (KeyedMap<T>) files.get(key);
        if (map == null) {
            return null;
        } else {
            return map.get(objectKey);
        }
    }

    private <T extends Keyed & FileResource> Collection<T> getAll(String key) {
        @SuppressWarnings("unchecked")
        KeyedMap<T> map = (KeyedMap<T>) files.get(key);
        if (map == null) {
            return Collections.emptySet();
        } else {
            return map.values();
        }
    }

}
