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
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.serialize.ResourcePackBuilder;
import team.unnamed.creative.base.KeyedMap;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
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

final class MinecraftResourcePackBuilder implements ResourcePackBuilder {

    private @Nullable Writable icon;

    // Metadata
    private @Nullable PackMeta packMeta;
    private @Nullable LanguageMeta languageMeta;
    private @Nullable FilterMeta filterMeta;
    private @Nullable Map<String, MetadataPart> customMetadataParts;

    private @Nullable Map<String, SoundRegistry> soundRegistries;

    private @Nullable Map<String, Writable> extraFiles;

    // Objects
    private @Nullable KeyedMap<BlockState> blockStates;
    private @Nullable KeyedMap<Font> fonts;
    private @Nullable KeyedMap<Language> languages;
    private @Nullable KeyedMap<Model> models;
    private @Nullable KeyedMap<Texture> textures;

    //#region Icon methods
    // |-----------------------------------|
    // |--------- ICON OPERATIONS ---------|
    // |-----------------------------------|
    @Override
    public ResourcePackBuilder icon(@Nullable Writable icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public @Nullable Writable icon() {
        return icon;
    }
    //#endregion


    @Override
    public ResourcePackBuilder metadata(@Nullable Metadata metadata) {
        // TODO: !
        return this;
    }

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
    public @Nullable PackMeta meta() {
        return packMeta;
    }

    @Override
    public ResourcePackBuilder languageRegistry(LanguageMeta meta) {
        this.languageMeta = meta;
        return this;
    }

    @Override
    public @Nullable LanguageMeta languageRegistry() {
        return languageMeta;
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
        this.filterMeta = filter;
        return this;
    }

    @Override
    public @Nullable FilterMeta filter() {
        return filterMeta;
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
        if (blockStates == null) blockStates = new KeyedMap<>();
        blockStates.put(state);
        return this;
    }

    @Override
    public @Nullable BlockState blockState(Key key) {
        return blockStates == null ? null : blockStates.get(key);
    }

    @Override
    public Collection<BlockState> blockStates() {
        return blockStates == null ? Collections.emptySet() : blockStates.values();
    }

    @Override
    public ResourcePackBuilder font(Font font) {
        if (fonts == null) fonts = new KeyedMap<>();
        fonts.put(font);
        return this;
    }

    @Override
    public @Nullable Font font(Key key) {
        return fonts == null ? null : fonts.get(key);
    }

    @Override
    public Collection<Font> fonts() {
        return fonts == null ? Collections.emptySet() : fonts.values();
    }

    @Override
    public ResourcePackBuilder language(Language language) {
        if (languages == null) languages = new KeyedMap<>();
        languages.put(language);
        return this;
    }

    @Override
    public Collection<Language> languages() {
        return languages == null ? Collections.emptySet() : languages.values();
    }

    @Override
    public ResourcePackBuilder model(Model model) {
        if (models == null) models = new KeyedMap<>();
        models.put(model);
        return this;
    }

    @Override
    public Collection<Model> models() {
        return models == null ? Collections.emptySet() : models.values();
    }

    @Override
    public ResourcePackBuilder soundRegistry(SoundRegistry soundRegistry) {
        if (soundRegistries == null) {
            soundRegistries = new HashMap<>();
        }
        soundRegistries.put(soundRegistry.namespace(), soundRegistry);
        return this;
    }

    @Override
    public ResourcePackBuilder sound(Sound.File soundFile) {
        return this;
    }

    @Override
    public ResourcePackBuilder texture(Texture texture) {
        if (textures == null) textures = new KeyedMap<>();
        textures.put(texture);
        return this;
    }

    @Override
    public Collection<Texture> textures() {
        return textures == null ? Collections.emptySet() : textures.values();
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
    public Map<String, Writable> unknownFiles() {
        return extraFiles == null ? Collections.emptyMap() : extraFiles;
    }

    public static ResourcePackBuilder minecraft() {
        return new MinecraftResourcePackBuilder();
    }

}
