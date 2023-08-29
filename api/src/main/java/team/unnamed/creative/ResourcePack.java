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
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Minecraft: Java Edition resource-pack,
 * it is only a memory representation and needs to be
 * serialized/compiled to a {@link BuiltResourcePack}
 * if you want to send it
 *
 * <p>This class is very useful during the stage of
 * resource collection, where you need to add all the
 * resources inside the resource-pack</p>
 *
 * <p>This class is mutable and not thread-safe</p>
 *
 * @since 1.0.0
 */
public final class ResourcePack {

    private @Nullable Writable icon;
    private Metadata metadata;

    private final Map<Key, Atlas> atlases = new HashMap<>();
    private final Map<Key, BlockState> blockStates = new HashMap<>();
    private final Map<Key, Font> fonts = new HashMap<>();
    private final Map<Key, Language> languages = new HashMap<>();
    private final Map<Key, Model> models = new HashMap<>();
    private final Map<String, SoundRegistry> soundRegistries = new HashMap<>();
    private final Map<Key, Sound> sounds = new HashMap<>();
    private final Map<Key, Texture> textures = new HashMap<>();

    // Unknown files we don't know how to parse
    private final Map<String, Writable> files = new HashMap<>();

    private ResourcePack() {
    }

    //#region (Top Level)
    /**
     * Returns the resource-pack icon
     * in <bold>PNG</bold> format
     *
     * @return The resource-pack icon
     */
    public @Nullable Writable icon() {
        return icon;
    }

    /**
     * Sets the resource-pack icon
     * in <bold>PNG</bold> format
     *
     * @param icon The resource-pack icon
     */
    public void icon(@Nullable Writable icon) {
        this.icon = icon;
    }

    /**
     * Returns the metadata of this resource
     * pack, may be empty, but it is never null
     *
     * @return The resource-pack metadata
     */
    public Metadata metadata() {
        return metadata == null ? Metadata.EMPTY : metadata;
    }

    /**
     * Sets the metadata for this resource pack,
     * may be empty, but not null
     *
     * @param metadata The resource-pack metadata
     */
    public void metadata(Metadata metadata) {
        requireNonNull(metadata, "metadata");
        this.metadata = metadata;
    }

    //#region Metadata helpers
    public void editMetadata(Consumer<Metadata.Builder> editFunction) {
        requireNonNull(editFunction, "editFunction");
        Metadata.Builder builder = metadata().toBuilder();
        editFunction.accept(builder);
        metadata(builder.build());
    }

    public @Nullable PackMeta packMeta() {
        return metadata == null ? null : metadata.meta(PackMeta.class);
    }

    public void packMeta(PackMeta packMeta) {
        requireNonNull(packMeta, "packMeta");
        editMetadata(metadata -> metadata.add(packMeta));
    }

    //#region Pack Meta helpers
    public void packMeta(int format, String description) {
        packMeta(PackMeta.of(format, description));
    }

    public int format() {
        PackMeta meta = packMeta();
        if (meta == null) {
            return -1;
        } else {
            return meta.format();
        }
    }

    public @Nullable String description() {
        PackMeta meta = packMeta();
        if (meta == null) {
            return null;
        } else {
            return meta.description();
        }
    }
    //#endregion (Pack meta helpers)

    public @Nullable LanguageMeta languageMeta() {
        return metadata == null ? null : metadata.meta(LanguageMeta.class);
    }

    public void languageMeta(LanguageMeta languageMeta) {
        requireNonNull(languageMeta, "languageMeta");
        editMetadata(metadata -> metadata.add(languageMeta));
    }

    public @Nullable FilterMeta filterMeta() {
        return metadata == null ? null : metadata.meta(FilterMeta.class);
    }

    public void filterMeta(FilterMeta filterMeta) {
        requireNonNull(filterMeta, "filterMeta");
        editMetadata(metadata -> metadata.add(filterMeta));
    }
    //#endregion (Metadata helpers)
    //#endregion (Top level)

    //#region Atlases (Keyed)
    public void atlas(Atlas atlas) {
        requireNonNull(atlas, "atlas");
        atlases.put(atlas.key(), atlas);
    }

    public @Nullable Atlas atlas(Key key) {
        requireNonNull(key, "key");
        return atlases.get(key);
    }

    public Collection<Atlas> atlases() {
        return atlases.values();
    }
    //#endregion

    //#region Block States (Keyed)
    public void blockState(BlockState state) {
        requireNonNull(state, "state");
        blockStates.put(state.key(), state);
    }


    public @Nullable BlockState blockState(Key key) {
        requireNonNull(key, "key");
        return blockStates.get(key);
    }

    public Collection<BlockState> blockStates() {
        return blockStates.values();
    }
    //#endregion

    //#region Fonts (Keyed)
    public void font(Font font) {
        requireNonNull(font, "font");
        fonts.put(font.key(), font);
    }

    public @Nullable Font font(Key key) {
        requireNonNull(key, "key");
        return fonts.get(key);
    }

    public Collection<Font> fonts() {
        return fonts.values();
    }

    //#region Font helpers
    public void font(Key key, FontProvider... providers) {
        font(Font.of(key, providers));
    }

    public void font(Key key, List<FontProvider> providers) {
        font(Font.of(key, providers));
    }
    //#endregion
    //#endregion

    //#region Languages (Keyed)
    public void language(Language language) {
        requireNonNull(language, "language");
        languages.put(language.key(), language);
    }

    public @Nullable Language language(Key key) {
        requireNonNull(key, "key");
        return languages.get(key);
    }

    public Collection<Language> languages() {
        return languages.values();
    }
    //#endregion

    //#region Models (Keyed)
    public void model(Model model) {
        requireNonNull(model, "model");
        models.put(model.key(), model);
    }

    public @Nullable Model model(Key key) {
        requireNonNull(key, "key");
        return models.get(key);
    }

    public Collection<Model> models() {
        return models.values();
    }
    //#endregion

    //#region Sound Registries (Namespaced)
    public void soundRegistry(SoundRegistry soundRegistry) {
        requireNonNull(soundRegistry, "soundRegistry");
        soundRegistries.put(soundRegistry.namespace(), soundRegistry);
    }

    public @Nullable SoundRegistry soundRegistry(String namespace) {
        requireNonNull(namespace, "namespace");
        return soundRegistries.get(namespace);
    }

    public Collection<SoundRegistry> soundRegistries() {
        return soundRegistries.values();
    }

    //#region Sound Events (Keyed, inside Sound Registries)
    public void soundEvent(SoundEvent soundEvent) {
        requireNonNull(soundEvent, "soundEvent");
        String namespace = soundEvent.key().namespace();

        Set<SoundEvent> soundEvents;
        SoundRegistry soundRegistry = soundRegistry(namespace);
        if (soundRegistry == null) {
            soundEvents = new HashSet<>();
        } else {
            soundEvents = new HashSet<>(soundRegistry.sounds());
        }

        soundEvents.add(soundEvent);
        soundRegistry(SoundRegistry.of(namespace, soundEvents));
    }

    public @Nullable SoundEvent soundEvent(Key key) {
        requireNonNull(key, "key");
        SoundRegistry registry = soundRegistry(key.namespace());
        if (registry == null) {
            return null;
        }
        return registry.sound(key);
    }

    public Collection<SoundEvent> soundEvents() {
        Collection<SoundEvent> soundEvents = new HashSet<>();
        for (SoundRegistry soundRegistry : soundRegistries()) {
            soundEvents.addAll(soundRegistry.sounds());
        }
        return soundEvents;
    }
    //#endregion
    //#endregion

    //#region Sounds (Keyed)
    public void sound(Sound sound) {
        requireNonNull(sound, "sound");
        sounds.put(sound.key(), sound);
    }

    public @Nullable Sound sound(Key key) {
        requireNonNull(key, "key");
        return sounds.get(key);
    }

    public Collection<Sound> sounds() {
        return sounds.values();
    }

    //#region Sound helpers
    public void sound(Key key, Writable data) {
        sound(Sound.of(key, data));
    }
    //#endregion
    //#endregion

    //#region Textures (Keyed)
    public void texture(Texture texture) {
        requireNonNull(texture, "textures");
        textures.put(texture.key(), texture);
    }

    public @Nullable Texture texture(Key key) {
        requireNonNull(key, "key");
        return textures.get(key);
    }

    public Collection<Texture> textures() {
        return textures.values();
    }

    //#region Texture helpers
    public void texture(Key key, Writable data) {
        texture(Texture.of(key, data));
    }

    public void texture(Key key, Writable data, Metadata meta) {
        texture(Texture.of(key, data, meta));
    }
    //#endregion
    //#endregion

    //#region Unknown Files (By absolute path)
    public void unknownFile(String path, Writable data) {
        requireNonNull(path, "path");
        requireNonNull(data, "data");
        files.put(path, data);
    }

    public @Nullable Writable unknownFile(String path) {
        requireNonNull(path, "path");
        return files.get(path);
    }

    public Map<String, Writable> unknownFiles() {
        return files;
    }
    //#endregion

    public static ResourcePack create() {
        return new ResourcePack();
    }

}
