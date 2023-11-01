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

    /**
     * Adds/updates an atlas to this resource container.
     *
     * <p>Note that there can't be two atlases with the
     * same key, so if there was an atlas with the same
     * key as the new given atlas, it will be replaced
     * by the given one.</p>
     *
     * @param atlas The atlas to add/update
     * @since 1.0.0
     */
    void atlas(final @NotNull Atlas atlas);

    /**
     * Gets the atlas with the given key.
     *
     * @param key The atlas key
     * @return The atlas, null if not found
     * @since 1.0.0
     */
    @Nullable Atlas atlas(final @NotNull Key key);

    /**
     * Removes the atlas with the given key.
     *
     * @param key The atlas key
     * @return True if the atlas existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeAtlas(final @NotNull Key key);

    /**
     * Gets all the atlases in this resource container.
     *
     * @return The atlases
     * @since 1.0.0
     */
    @NotNull Collection<Atlas> atlases();
    //#endregion

    //#region Block States (Keyed)

    /**
     * Adds/updates a block state to this resource container.
     *
     * <p>Note that there can't be two block states with the
     * same key, so if there was a block state with the same
     * key as the new given block state, it will be replaced
     * by the given one.</p>
     *
     * @param state The block state to add/update
     * @since 1.0.0
     */
    void blockState(final @NotNull BlockState state);

    /**
     * Gets the block state with the given key.
     *
     * @param key The block state key
     * @return The block state, null if not found
     * @since 1.0.0
     */
    @Nullable BlockState blockState(final @NotNull Key key);

    /**
     * Removes the block state with the given key.
     *
     * @param key The block state key
     * @return True if the block state existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeBlockState(final @NotNull Key key);

    /**
     * Gets all the block states in this resource container.
     *
     * @return The block states
     * @since 1.0.0
     */
    @NotNull Collection<BlockState> blockStates();
    //#endregion

    //#region Fonts (Keyed)

    /**
     * Adds/updates a font to this resource container.
     *
     * <p>Note that there can't be two fonts with the
     * same key, so if there was a font with the same
     * key as the new given font, it will be replaced
     * by the given one.</p>
     *
     * @param font The font to add/update
     * @since 1.0.0
     */
    void font(final @NotNull Font font);

    /**
     * Gets the font with the given key.
     *
     * @param key The font key
     * @return The font, null if not found
     * @since 1.0.0
     */
    @Nullable Font font(final @NotNull Key key);

    /**
     * Removes the font with the given key.
     *
     * @param key The font key
     * @return True if the font existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeFont(final @NotNull Key key);

    /**
     * Gets all the fonts in this resource container.
     *
     * @return The fonts
     * @since 1.0.0
     */
    @NotNull Collection<Font> fonts();

    //#region Font helpers

    /**
     * Adds/updates a font to this resource container.
     *
     * <p>Note that there can't be two fonts with the
     * same key, so if there was a font with the same
     * key as the new given font, it will be replaced
     * by the given one.</p>
     *
     * @param key       The font key
     * @param providers The font providers
     * @since 1.0.0
     */
    default void font(final @NotNull Key key, final @NotNull FontProvider @NotNull ... providers) {
        font(Font.font(key, providers));
    }

    /**
     * Adds/updates a font to this resource container.
     *
     * <p>Note that there can't be two fonts with the
     * same key, so if there was a font with the same
     * key as the new given font, it will be replaced
     * by the given one.</p>
     *
     * @param key       The font key
     * @param providers The font providers
     * @since 1.0.0
     */
    default void font(final @NotNull Key key, final @NotNull List<FontProvider> providers) {
        font(Font.font(key, providers));
    }
    //#endregion
    //#endregion

    //#region Languages (Keyed)

    /**
     * Adds/updates a language to this resource container.
     *
     * <p>Note that there can't be two languages with the
     * same key, so if there was a language with the same
     * key as the new given language, it will be replaced
     * by the given one.</p>
     *
     * @param language The language to add/update
     * @since 1.0.0
     */
    void language(final @NotNull Language language);

    /**
     * Gets the language with the given key.
     *
     * @param key The language key
     * @return The language, null if not found
     * @since 1.0.0
     */
    @Nullable Language language(final @NotNull Key key);

    /**
     * Removes the language with the given key.
     *
     * @param key The language key
     * @return True if the language existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeLanguage(final @NotNull Key key);

    /**
     * Gets all the languages in this resource container.
     *
     * @return The languages
     * @since 1.0.0
     */
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
        soundRegistry(SoundRegistry.soundRegistry(namespace, soundEvents));
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

    /**
     * Adds/updates a texture to this resource container.
     *
     * <p>Note that there can't be two textures with the
     * same key, so if there was a texture with the same
     * key as the new given texture, it will be replaced
     * by the given one.</p>
     *
     * @param texture The texture to add/update
     * @since 1.0.0
     */
    void texture(final @NotNull Texture texture);

    /**
     * Gets the texture with the given key.
     *
     * @param key The texture key
     * @return The texture, null if not found
     * @since 1.0.0
     */
    @Nullable Texture texture(final @NotNull Key key);

    /**
     * Removes the texture with the given key.
     *
     * @param key The texture key
     * @return True if the texture existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeTexture(final @NotNull Key key);

    /**
     * Gets all the textures in this resource container.
     *
     * @return The textures
     * @since 1.0.0
     */
    @NotNull Collection<Texture> textures();

    //#region Texture helpers

    /**
     * Adds/updates a texture to this resource container.
     *
     * <p>Note that there can't be two textures with the
     * same key, so if there was a texture with the same
     * key as the new given texture, it will be replaced
     * by the given one.</p>
     *
     * @param key  The texture key
     * @param data The texture data
     * @since 1.0.0
     */
    default void texture(final @NotNull Key key, final @NotNull Writable data) {
        texture(Texture.texture(key, data));
    }

    /**
     * Adds/updates a texture to this resource container.
     *
     * <p>Note that there can't be two textures with the
     * same key, so if there was a texture with the same
     * key as the new given texture, it will be replaced
     * by the given one.</p>
     *
     * @param key  The texture key
     * @param data The texture data
     * @param meta The texture metadata
     * @since 1.0.0
     */
    default void texture(final @NotNull Key key, final @NotNull Writable data, final @NotNull Metadata meta) {
        texture(Texture.texture(key, data, meta));
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
