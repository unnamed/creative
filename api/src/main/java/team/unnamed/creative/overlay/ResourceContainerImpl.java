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
package team.unnamed.creative.overlay;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@ApiStatus.Internal
public class ResourceContainerImpl implements ResourceContainer {

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

    //#region Atlases (Keyed)
    @Override
    public void atlas(final @NotNull Atlas atlas) {
        requireNonNull(atlas, "atlas");
        atlases.put(atlas.key(), atlas);
    }

    @Override
    public @Nullable Atlas atlas(final @NotNull Key key) {
        requireNonNull(key, "key");
        return atlases.get(key);
    }

    @Override
    public boolean removeAtlas(final @NotNull Key key) {
        requireNonNull(key, "key");
        return atlases.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Atlas> atlases() {
        return atlases.values();
    }
    //#endregion

    //#region Block States (Keyed)
    @Override
    public void blockState(final @NotNull BlockState state) {
        requireNonNull(state, "state");
        blockStates.put(state.key(), state);
    }

    @Override
    public @Nullable BlockState blockState(final @NotNull Key key) {
        requireNonNull(key, "key");
        return blockStates.get(key);
    }

    @Override
    public boolean removeBlockState(final @NotNull Key key) {
        requireNonNull(key, "key");
        return blockStates.remove(key) != null;
    }

    @Override
    public @NotNull Collection<BlockState> blockStates() {
        return blockStates.values();
    }
    //#endregion

    //#region Fonts (Keyed)
    @Override
    public void font(final @NotNull Font font) {
        requireNonNull(font, "font");
        fonts.put(font.key(), font);
    }

    @Override
    public @Nullable Font font(final @NotNull Key key) {
        requireNonNull(key, "key");
        return fonts.get(key);
    }

    @Override
    public boolean removeFont(final @NotNull Key key) {
        requireNonNull(key, "key");
        return fonts.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Font> fonts() {
        return fonts.values();
    }
    //#endregion

    //#region Languages (Keyed)
    @Override
    public void language(final @NotNull Language language) {
        requireNonNull(language, "language");
        languages.put(language.key(), language);
    }

    @Override
    public @Nullable Language language(final @NotNull Key key) {
        requireNonNull(key, "key");
        return languages.get(key);
    }

    @Override
    public boolean removeLanguage(final @NotNull Key key) {
        requireNonNull(key, "key");
        return languages.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Language> languages() {
        return languages.values();
    }
    //#endregion

    //#region Models (Keyed)
    @Override
    public void model(final @NotNull Model model) {
        requireNonNull(model, "model");
        models.put(model.key(), model);
    }

    @Override
    public @Nullable Model model(final @NotNull Key key) {
        requireNonNull(key, "key");
        return models.get(key);
    }

    @Override
    public boolean removeModel(final @NotNull Key key) {
        requireNonNull(key, "key");
        return models.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Model> models() {
        return models.values();
    }
    //#endregion

    //#region Sound Registries (Namespaced)
    @Override
    public void soundRegistry(final @NotNull SoundRegistry soundRegistry) {
        requireNonNull(soundRegistry, "soundRegistry");
        soundRegistries.put(soundRegistry.namespace(), soundRegistry);
    }

    @Override
    public @Nullable SoundRegistry soundRegistry(final @NotNull String namespace) {
        requireNonNull(namespace, "namespace");
        return soundRegistries.get(namespace);
    }

    @Override
    public boolean removeSoundRegistry(final @NotNull String namespace) {
        requireNonNull(namespace, "namespace");
        return soundRegistries.remove(namespace) != null;
    }

    @Override
    public @NotNull Collection<SoundRegistry> soundRegistries() {
        return soundRegistries.values();
    }
    //#endregion

    //#region Sounds (Keyed)
    @Override
    public void sound(final @NotNull Sound sound) {
        requireNonNull(sound, "sound");
        sounds.put(sound.key(), sound);
    }

    @Override
    public @Nullable Sound sound(final @NotNull Key key) {
        requireNonNull(key, "key");
        return sounds.get(key);
    }

    @Override
    public boolean removeSound(final @NotNull Key key) {
        requireNonNull(key, "key");
        return sounds.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Sound> sounds() {
        return sounds.values();
    }
    //#endregion

    //#region Textures (Keyed)
    @Override
    public void texture(final @NotNull Texture texture) {
        requireNonNull(texture, "textures");
        textures.put(texture.key(), texture);
    }

    @Override
    public @Nullable Texture texture(final @NotNull Key key) {
        requireNonNull(key, "key");
        return textures.get(key);
    }

    @Override
    public boolean removeTexture(final @NotNull Key key) {
        requireNonNull(key, "key");
        return textures.remove(key) != null;
    }

    @Override
    public @NotNull Collection<Texture> textures() {
        return textures.values();
    }
    //#endregion

    //#region Unknown Files (By absolute path)
    @Override
    public void unknownFile(final @NotNull String path, final @NotNull Writable data) {
        requireNonNull(path, "path");
        requireNonNull(data, "data");
        files.put(path, data);
    }

    @Override
    public @Nullable Writable unknownFile(final @NotNull String path) {
        requireNonNull(path, "path");
        return files.get(path);
    }

    @Override
    public boolean removeUnknownFile(final @NotNull String path) {
        requireNonNull(path, "path");
        return files.remove(path) != null;
    }

    @Override
    public @NotNull Map<String, Writable> unknownFiles() {
        return files;
    }
    //#endregion

    @Override
    public void merge(final @NotNull ResourceContainer other, final @NotNull MergeMode mode) {
        // merge atlases
        for (final Atlas atlas : other.atlases()) {
            final Atlas oldAtlas = atlases.get(atlas.key());
            if (oldAtlas == null || mode == MergeMode.OVERRIDE) {
                atlases.put(atlas.key(), atlas);
                continue;
            }

            final Atlas.Builder oldAtlasBuilder = oldAtlas.toBuilder();
            for (final AtlasSource source : atlas.sources()) {
                oldAtlasBuilder.addSource(source);
            }
            atlases.put(atlas.key(), oldAtlasBuilder.build());
        }

        // merge block states
        for (final BlockState blockState : other.blockStates()) {
            if (blockStates.containsKey(blockState.key())) {
                switch (mode) {
                    case OVERRIDE:
                        blockStates.put(blockState.key(), blockState);
                        break;
                    case MERGE_AND_FAIL_ON_ERROR:
                        throw new IllegalStateException("Block state " + blockState.key() + " already exists");
                    case MERGE_AND_KEEP_FIRST_ON_ERROR:
                        break;
                }
            } else {
                blockStates.put(blockState.key(), blockState);
            }
        }

        // merge fonts
        for (final Font font : other.fonts()) {
            final Font oldFont = fonts.get(font.key());
            if (oldFont == null || mode == MergeMode.OVERRIDE) {
                fonts.put(font.key(), font);
                continue;
            }

            // todo: check for duplicated characters
            final List<FontProvider> providers = new ArrayList<>(oldFont.providers());
            providers.addAll(font.providers());
            fonts.put(font.key(), oldFont.providers(providers));
        }

        // merge languages
        for (final Language language : other.languages()) {
            final Language oldLanguage = languages.get(language.key());
            if (oldLanguage == null || mode == MergeMode.OVERRIDE) {
                languages.put(language.key(), language);
                continue;
            }

            final Map<String, String> translations = new HashMap<>(oldLanguage.translations());
            for (final Map.Entry<String, String> translation : language.translations().entrySet()) {
                final String replaced = translations.put(translation.getKey(), translation.getValue());
                if (replaced != null && mode == MergeMode.MERGE_AND_FAIL_ON_ERROR) {
                    throw new IllegalStateException(
                            "Duplicated translation keys in language " + language.key()
                                    + ". Translation key: " + translation.getKey()
                    );
                }
            }
            languages.put(language.key(), Language.language(language.key(), translations));
        }

        // merge models
        for (final Model model : other.models()) {
            final Model oldModel = models.get(model.key());
            if (oldModel == null || mode == MergeMode.OVERRIDE) {
                models.put(model.key(), model);
                continue;
            }

            final Model.Builder oldModelBuilder = oldModel.toBuilder();
            for (final ItemOverride override : model.overrides()) {
                // todo: detect duplicated override keys
                oldModelBuilder.addOverride(override);
            }
            models.put(model.key(), oldModelBuilder.build());
        }

        // merge sound registries
        for (final SoundRegistry soundRegistry : other.soundRegistries()) {
            final SoundRegistry oldSoundRegistry = soundRegistries.get(soundRegistry.namespace());
            if (oldSoundRegistry == null || mode == MergeMode.OVERRIDE) {
                soundRegistries.put(soundRegistry.namespace(), soundRegistry);
                continue;
            }

            final Map<Key, SoundEvent> soundEvents = new HashMap<>();
            for (final SoundEvent soundEvent : oldSoundRegistry.sounds()) {
                soundEvents.put(soundEvent.key(), soundEvent);
            }

            for (final SoundEvent soundEvent : soundRegistry.sounds()) {
                final SoundEvent replacedSoundEvent = soundEvents.put(soundEvent.key(), soundEvent);
                if (replacedSoundEvent != null) {
                    switch (mode) {
                        case MERGE_AND_FAIL_ON_ERROR:
                            throw new IllegalStateException("Sound event " + soundEvent + " already exists");
                        case MERGE_AND_KEEP_FIRST_ON_ERROR:
                            break;
                    }
                }
            }

            soundRegistries.put(
                    soundRegistry.namespace(),
                    SoundRegistry.soundRegistry()
                            .namespace(soundRegistry.namespace())
                            .sounds(soundEvents.values())
                            .build()
            );
        }

        // merge sounds
        for (final Sound sound : other.sounds()) {
            if (sounds.containsKey(sound.key())) {
                switch (mode) {
                    case OVERRIDE:
                        sounds.put(sound.key(), sound);
                        break;
                    case MERGE_AND_FAIL_ON_ERROR:
                        throw new IllegalStateException("Sound " + sound.key() + " already exists");
                    case MERGE_AND_KEEP_FIRST_ON_ERROR:
                        break;
                }
            } else {
                sounds.put(sound.key(), sound);
            }
        }

        // merge textures
        // todo: should we merge metadata?
        for (final Texture texture : other.textures()) {
            if (textures.containsKey(texture.key())) {
                switch (mode) {
                    case OVERRIDE:
                        textures.put(texture.key(), texture);
                        break;
                    case MERGE_AND_FAIL_ON_ERROR:
                        throw new IllegalStateException("Texture " + texture.key() + " already exists");
                    case MERGE_AND_KEEP_FIRST_ON_ERROR:
                        break;
                }
            } else {
                textures.put(texture.key(), texture);
            }
        }

        // merge unknown files
        for (final Map.Entry<String, Writable> entry : other.unknownFiles().entrySet()) {
            if (files.containsKey(entry.getKey())) {
                switch (mode) {
                    case OVERRIDE:
                        files.put(entry.getKey(), entry.getValue());
                        break;
                    case MERGE_AND_FAIL_ON_ERROR:
                        throw new IllegalStateException("Unknown file " + entry.getKey() + " already exists");
                    case MERGE_AND_KEEP_FIRST_ON_ERROR:
                        break;
                }
            } else {
                files.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
