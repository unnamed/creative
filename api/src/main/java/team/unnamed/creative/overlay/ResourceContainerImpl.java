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
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.blockstate.BlockState;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.HashMap;
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

}
