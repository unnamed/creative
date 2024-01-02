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
package team.unnamed.creative.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Represents a sound event, a compound of {@link SoundEntry}
 * instances
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface SoundEvent extends ResourcePackPart, Sound.Type, Examinable {
    /**
     * Creates a new {@link SoundEvent} from the
     * given values
     *
     * @param key      The sound event's key
     * @param replace  True to replace default sounds
     * @param subtitle The sound event subtitle
     * @param sounds   The sound event sounds
     * @return A new {@link SoundEvent} instance
     * @since 1.1.0
     */
    static @NotNull SoundEvent soundEvent(final @NotNull Key key, final boolean replace, final @Nullable String subtitle, final @NotNull List<SoundEntry> sounds) {
        return new SoundEventImpl(key, replace, subtitle, sounds);
    }

    /**
     * Creates a new {@link Builder} instance,
     * it eases the creation of {@link SoundEvent}
     * objects
     *
     * @return A new {@link Builder} instance
     * @since 1.1.0
     */
    static @NotNull Builder soundEvent() {
        return new SoundEventImpl.BuilderImpl();
    }

    boolean DEFAULT_REPLACE = false;

    /**
     * Returns this sound event's key.
     *
     * @return The sound event's key
     * @since 1.0.0
     */
    @Override
    @NotNull Key key();

    /**
     * Returns true if the sounds listed in {@link SoundEvent#sounds}
     * should replace the sounds listed in the default
     * sounds.json for this sound event. False if the
     * sounds listed should be added to the list of
     * default sounds
     *
     * @return True to replace default sounds.json sounds
     * @since 1.0.0
     */
    boolean replace();

    /**
     * Returns the sound event subtitle, which is translated as the
     * subtitle of the sound if "Show Subtitles" is enabled in-game
     *
     * @return The sound subtitle
     * @since 1.0.0
     */
    @Nullable String subtitle();

    /**
     * The sounds this sound event uses, one of the listed sounds
     * is randomly selected to play when this sound event is triggered
     *
     * @return An unmodifiable list of the sound that this event uses
     * @since 1.0.0
     */
    @Unmodifiable @NotNull List<SoundEntry> sounds();

    /**
     * Adds this sound event to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.soundEvent(this);
    }

    /**
     * Converts this sound event instance to its builder type,
     * with all the properties already set
     *
     * @return The created builder
     * @since 1.6.0
     */
    @Contract("-> new")
    @NotNull Builder toBuilder();

    /**
     * Builder implementation for ease the
     * construction of {@link SoundEvent}
     * instances
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the sound event's key
         *
         * @param key The sound event's key
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder key(final @NotNull Key key);

        /**
         * Sets whether the sounds listed in {@link SoundEvent#sounds}
         * should replace the sounds listed in the default sound registry
         * for this sound event. False if the sounds listed should be added
         * to the list of default sounds
         *
         * @param replace True to replace default sounds
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder replace(final boolean replace);

        /**
         * Sets the sound event subtitle, which is translated as the
         * subtitle of the sound if "Show Subtitles" is enabled in-game
         *
         * @param subtitle The sound subtitle
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder subtitle(final @Nullable String subtitle);

        /**
         * Sets the sounds this sound event uses, one of the listed sounds
         * is randomly selected to play when this sound event is triggered
         *
         * @param sounds The sounds this sound event uses
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder sounds(final @NotNull List<SoundEntry> sounds);

        /**
         * Adds a sound to this sound event.
         *
         * @param soundEntry The sound entry
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        @NotNull Builder addSound(final @NotNull SoundEntry soundEntry);

        /**
         * Adds a sound to this sound event.
         *
         * @param sound The sound
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        default @NotNull Builder addSound(final @NotNull team.unnamed.creative.sound.Sound sound) {
            return addSound(SoundEntry.soundEntry(sound));
        }

        /**
         * Adds a sound to this sound event.
         *
         * @param soundEvent The sound event
         * @return This builder
         * @since 1.1.0
         */
        @Contract("_ -> this")
        default @NotNull Builder addSound(final @NotNull SoundEvent soundEvent) {
            return addSound(SoundEntry.soundEntry(soundEvent));
        }

        /**
         * Sets the sounds this sound event uses, one of the listed sounds
         * is randomly selected to play when this sound event is triggered
         *
         * @param sounds The sounds this sound event uses
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder sounds(final @NotNull SoundEntry @NotNull ... sounds) {
            requireNonNull(sounds, "sounds");
            return sounds(Arrays.asList(sounds));
        }

        /**
         * Finishes building the {@link SoundEvent} instance
         * using the previously set values
         *
         * @return A new {@link SoundEvent instance}
         * @since 1.0.0
         */
        @NotNull SoundEvent build();
    }
}
