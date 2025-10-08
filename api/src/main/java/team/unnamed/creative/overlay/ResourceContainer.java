/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.resources.MergeException;
import team.unnamed.creative.resources.MergeStrategy;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.sound.SoundRegistry;
import team.unnamed.creative.texture.Texture;
import team.unnamed.creative.waypoint.WaypointStyle;
import team.unnamed.creative.waypoint.WaypointStyleImpl;

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

    //#region Equipment (Keyed)
    /**
     * Adds/updates equipment to this resource container.
     *
     * <p>Note that there can't be two equipment with the
     * same key, so if there was equipment with the same
     * key as the new given equipment, it will be replaced
     * by the given one.</p>
     *
     * @param equipment The equipment to add/update
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    void equipment(final @NotNull Equipment equipment);

    /**
     * Gets the equipment with the given key.
     *
     * @param key The equipment key
     * @return The equipment, null if not found
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Nullable Equipment equipment(final @NotNull Key key);

    /**
     * Removes the equipment with the given key.
     *
     * @param key The equipment key
     * @return True if the equipment existed and was removed,
     * false otherwise
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    boolean removeEquipment(final @NotNull Key key);

    /**
     * Gets all the equipment in this resource container.
     *
     * @return The equipment
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @NotNull Collection<Equipment> equipment();
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

    //#region Items (Keyed)
    /**
     * Adds/updates an item to this resource container.
     *
     * <p>Note that there can't be two items with the
     * same key, so if there was an item with the same
     * key as the new given item, it will be replaced
     * by the given one.</p>
     *
     * @param item The item to add/update
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    void item(final @NotNull Item item);

    /**
     * Gets the item with the given key.
     *
     * @param key The item key
     * @return The item, null if not found
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    @Nullable Item item(final @NotNull Key key);

    /**
     * Removes the item with the given key.
     *
     * @param key The item key
     * @return True if the item existed and was removed,
     * false otherwise
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    boolean removeItem(final @NotNull Key key);

    /**
     * Gets all the items in this resource container.
     *
     * @return The items
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    @NotNull Collection<Item> items();
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

    /**
     * Adds/updates a model to this resource container.
     *
     * <p>Note that there can't be two models with the
     * same key, so if there was a model with the same
     * key as the new given model, it will be replaced
     * by the given one.</p>
     *
     * @param model The model to add/update
     * @since 1.0.0
     */
    void model(final @NotNull Model model);

    /**
     * Gets the model with the given key.
     *
     * @param key The model key
     * @return The model, null if not found
     * @since 1.0.0
     */
    @Nullable Model model(final @NotNull Key key);

    /**
     * Removes the model with the given key.
     *
     * @param key The model key
     * @return True if the model existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeModel(final @NotNull Key key);

    /**
     * Gets all the models in this resource container.
     *
     * @return The models
     * @since 1.0.0
     */
    @NotNull Collection<Model> models();
    //#endregion

    //#region Sound Registries (Namespaced)

    /**
     * Adds/updates a sound registry to this resource container.
     *
     * <p>Note that there can't be two sound registries with the
     * same namespace, so if there was a sound registry with the same
     * namespace as the new given sound registry, it will be replaced
     * by the given one.</p>
     *
     * @param soundRegistry The sound registry to add/update
     * @since 1.0.0
     */
    void soundRegistry(final @NotNull SoundRegistry soundRegistry);

    /**
     * Gets the sound registry with the given namespace.
     *
     * @param namespace The sound registry namespace
     * @return The sound registry, null if not found
     * @since 1.0.0
     */
    @Nullable SoundRegistry soundRegistry(final @NotNull String namespace);

    /**
     * Removes the sound registry with the given namespace.
     *
     * @param namespace The sound registry namespace
     * @return True if the sound registry existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeSoundRegistry(final @NotNull String namespace);

    /**
     * Gets all the sound registries in this resource container.
     *
     * @return The sound registries
     * @since 1.0.0
     */
    @NotNull Collection<SoundRegistry> soundRegistries();

    //#region Sound Events (Keyed, inside Sound Registries)

    /**
     * Adds/updates a sound event to this resource container.
     *
     * <p>Note that there can't be two sound events with the
     * same key, so if there was a sound event with the same
     * key as the new given sound event, it will be replaced
     * by the given one.</p>
     *
     * @param soundEvent The sound event to add/update
     * @since 1.0.0
     */
    default void soundEvent(final @NotNull SoundEvent soundEvent) {
        requireNonNull(soundEvent, "soundEvent");
        final String namespace = soundEvent.key().namespace();

        final Set<SoundEvent> soundEvents;
        final SoundRegistry soundRegistry = soundRegistry(namespace);
        if (soundRegistry == null) {
            soundEvents = new HashSet<>();
        } else {
            soundEvents = new HashSet<>(soundRegistry.sounds());
        }

        soundEvents.add(soundEvent);
        soundRegistry(SoundRegistry.soundRegistry(namespace, soundEvents));
    }

    /**
     * Gets the sound event with the given key.
     *
     * @param key The sound event key
     * @return The sound event, null if not found
     * @since 1.0.0
     */
    default @Nullable SoundEvent soundEvent(final @NotNull Key key) {
        requireNonNull(key, "key");
        final SoundRegistry registry = soundRegistry(key.namespace());
        if (registry == null) {
            return null;
        }
        return registry.sound(key);
    }

    /**
     * Removes the sound event with the given key.
     *
     * @param key The sound event key
     * @return True if the sound event existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    default boolean removeSoundEvent(final @NotNull Key key) {
        requireNonNull(key, "key");
        final SoundRegistry registry = soundRegistry(key.namespace());
        if (registry == null) {
            return false;
        }
        final Set<SoundEvent> sounds = new HashSet<>(registry.sounds());
        final boolean removed = sounds.removeIf(sound -> sound.key().equals(key));
        soundRegistry(SoundRegistry.soundRegistry(key.namespace(), sounds));
        return removed;
    }

    /**
     * Gets all the sound events in this resource container.
     *
     * @return The sound events
     * @since 1.0.0
     */
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

    /**
     * Adds/updates a sound to this resource container.
     *
     * <p>Note that there can't be two sounds with the
     * same key, so if there was a sound with the same
     * key as the new given sound, it will be replaced
     * by the given one.</p>
     *
     * @param sound The sound to add/update
     * @since 1.0.0
     */
    void sound(final @NotNull Sound sound);

    /**
     * Gets the sound with the given key.
     *
     * @param key The sound key
     * @return The sound, null if not found
     * @since 1.0.0
     */
    @Nullable Sound sound(final @NotNull Key key);

    /**
     * Removes the sound with the given key.
     *
     * @param key The sound key
     * @return True if the sound existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeSound(final @NotNull Key key);

    /**
     * Gets all the sounds in this resource container.
     *
     * @return The sounds
     * @since 1.0.0
     */
    @NotNull Collection<Sound> sounds();

    //#region Sound helpers

    /**
     * Adds/updates a sound to this resource container.
     *
     * <p>Note that there can't be two sounds with the
     * same key, so if there was a sound with the same
     * key as the new given sound, it will be replaced
     * by the given one.</p>
     *
     * @param key  The sound key
     * @param data The sound data
     * @since 1.0.0
     */
    default void sound(final @NotNull Key key, final @NotNull Writable data) {
        sound(Sound.sound(key, data));
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

    //#region Waypoint Styles (Keyed)

    /**
     * Sets the style of the waypoint to the specified {@code WaypointStyle}.
     *
     * @param style the desired waypoint style to be applied; must not be null
     * @since 1.8.4
     * @sinceMinecraft 1.21.6
     */
    void waypointStyle(final @NotNull WaypointStyle style);

    /**
     * Retrieves the waypoint style associated with the specified key.
     *
     * @param key the non-null key used to fetch the corresponding waypoint style
     * @return the waypoint style associated with the given key, or null if no style is found
     * @since 1.8.4
     * @sinceMinecraft 1.21.6
     */
    @Nullable WaypointStyle waypointStyle(final @NotNull Key key);

    /**
     * Removes the waypoint style associated with the specified key.
     *
     * @param key the key representing the waypoint style to be removed, must not be null
     * @return true if the waypoint style was successfully removed, false otherwise
     * @since 1.8.4
     * @sinceMinecraft 1.21.6
     */
    boolean removeWaypointStyle(final @NotNull Key key);

    /**
     * Retrieves a collection of waypoint styles.
     *
     * @return A non-null collection of {@link WaypointStyle} objects representing the styles of waypoints.
     * @since 1.8.4
     * @sinceMinecraft 1.21.6
     */
    @NotNull Collection<WaypointStyle> waypointStyles();
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

    //#region Unknown Files (By path, relative to current's resource container)

    /**
     * Adds/updates an unknown file to this resource container.
     *
     * <p>Note that there can't be two unknown files with the
     * same path, so if there was an unknown file with the same
     * path as the new given unknown file, it will be replaced
     * by the given one.</p>
     *
     * @param path The unknown file path
     * @param data The unknown file data
     * @since 1.0.0
     */
    void unknownFile(final @NotNull String path, final @NotNull Writable data);

    /**
     * Gets the unknown file with the given path.
     *
     * @param path The unknown file path
     * @return The unknown file, null if not found
     * @since 1.0.0
     */
    @Nullable Writable unknownFile(final @NotNull String path);

    /**
     * Removes the unknown file with the given path.
     *
     * @param path The unknown file path
     * @return True if the unknown file existed and was removed,
     * false otherwise
     * @since 1.3.0
     */
    boolean removeUnknownFile(final @NotNull String path);

    /**
     * Gets all the unknown files in this resource container.
     *
     * @return The unknown files
     * @since 1.0.0
     */
    @NotNull Map<String, Writable> unknownFiles();
    //#endregion

    /**
     * Merges the given {@code other} resource container
     * with this resource container.
     *
     * @param other    The other resource container
     * @param strategy The merge strategy
     * @throws MergeException If the merge fails
     * @since 1.4.0
     */
    void merge(final @NotNull ResourceContainer other, final @NotNull MergeStrategy strategy);
}
