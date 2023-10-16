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
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@ApiStatus.NonExtendable
public interface ResourceContainer {

    //#region Atlases (Keyed)
    void atlas(final @NotNull Atlas atlas);

    @Nullable Atlas atlas(final @NotNull Key key);

    @NotNull Collection<Atlas> atlases();
    //#endregion

    //#region Block States (Keyed)
    void blockState(final @NotNull BlockState state);

    @Nullable BlockState blockState(final @NotNull Key key);

    @NotNull Collection<BlockState> blockStates();
    //#endregion

    //#region Fonts (Keyed)
    void font(final @NotNull Font font);

    @Nullable Font font(final @NotNull Key key);

    @NotNull Collection<Font> fonts();

    //#region Font helpers
    default void font(final @NotNull Key key, final @NotNull FontProvider @NotNull ... providers) {
        font(Font.of(key, providers));
    }

    default void font(final @NotNull Key key, final @NotNull List<FontProvider> providers) {
        font(Font.of(key, providers));
    }
    //#endregion
    //#endregion

    //#region Languages (Keyed)
    void language(final @NotNull Language language);

    @Nullable Language language(final @NotNull Key key);

    @NotNull Collection<Language> languages();
    //#endregion

    //#region Models (Keyed)
    void model(final @NotNull Model model);

    @Nullable Model model(final @NotNull Key key);

    @NotNull Collection<Model> models();
    //#endregion

    //#region Sound Registries (Namespaced)
    void soundRegistry(final @NotNull SoundRegistry soundRegistry);

    @Nullable SoundRegistry soundRegistry(final @NotNull String namespace);

    @NotNull Collection<SoundRegistry> soundRegistries();

    //#region Sound Events (Keyed, inside Sound Registries)
    default void soundEvent(final @NotNull SoundEvent soundEvent) {
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

    default @Nullable SoundEvent soundEvent(final @NotNull Key key) {
        requireNonNull(key, "key");
        SoundRegistry registry = soundRegistry(key.namespace());
        if (registry == null) {
            return null;
        }
        return registry.sound(key);
    }

    default @NotNull Collection<SoundEvent> soundEvents() {
        Collection<SoundEvent> soundEvents = new HashSet<>();
        for (SoundRegistry soundRegistry : soundRegistries()) {
            soundEvents.addAll(soundRegistry.sounds());
        }
        return soundEvents;
    }
    //#endregion
    //#endregion

    //#region Sounds (Keyed)
    void sound(final @NotNull Sound sound);

    @Nullable Sound sound(final @NotNull Key key);

    @NotNull Collection<Sound> sounds();

    //#region Sound helpers
    default void sound(final @NotNull Key key, final @NotNull Writable data) {
        sound(Sound.of(key, data));
    }
    //#endregion
    //#endregion

    //#region Textures (Keyed)
    void texture(final @NotNull Texture texture);

    @Nullable Texture texture(final @NotNull Key key);

    @NotNull Collection<Texture> textures();

    //#region Texture helpers
    default void texture(final @NotNull Key key, final @NotNull Writable data) {
        texture(Texture.of(key, data));
    }

    default void texture(final @NotNull Key key, final @NotNull Writable data, final @NotNull Metadata meta) {
        texture(Texture.of(key, data, meta));
    }
    //#endregion
    //#endregion

    /**
     * Adds the given resource pack part to this resource container.
     *
     * @param part The resource pack part
     * @since 1.1.0
     */
    default void part(final @NotNull ResourcePackPart part) {
        requireNonNull(part, "part");
        part.addTo(this);
    }

    //#region Unknown Files (By absolute path)
    void unknownFile(final @NotNull String path, final @NotNull Writable data);

    @Nullable Writable unknownFile(final @NotNull String path);

    @NotNull Map<String, Writable> unknownFiles();
    //#endregion

}
