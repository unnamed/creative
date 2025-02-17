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
package team.unnamed.creative.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

/**
 * Represents a Minecraft sound data in the resource-pack,
 * similar to {@link Texture}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface Sound extends ResourcePackPart, Keyed, Examinable {
    /**
     * Creates a new sound instance with the given key and data.
     *
     * @param key  The sound key
     * @param data The sound data
     * @return The sound instance
     * @since 1.1.0
     */
    static @NotNull Sound sound(final @NotNull Key key, final @NotNull Writable data) {
        return new SoundImpl(key, data);
    }

    /**
     * Creates a new sound instance with the given key and data.
     *
     * @param key  The sound key
     * @param data The sound data
     * @return The sound instance
     * @since 1.0.0
     * @deprecated Use {@link #sound(Key, Writable)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull Sound of(final @NotNull Key key, final @NotNull Writable data) {
        return new SoundImpl(key, data);
    }

    /**
     * Returns this sound's key (location), the key contains
     * the sound namespace and path, so that the full sound
     * path is formatted like {@code assets/<namespace>/sounds/<path>}
     *
     * <p>Example key for a sound: <pre>{@code
     *
     *   Key key = Key.key("minecraft", "ambient/cave");
     * }</pre></p>
     *
     * <p>Note that the key value must not include the sound extension</p>
     *
     * @return The sound key
     */
    @Override
    @NotNull Key key();

    /**
     * Returns the sound's OGG sound data, as a
     * {@link Writable} object, never null
     *
     * @return The OGG sound data
     * @since 1.0.0
     */
    @NotNull Writable data();

    /**
     * Adds this sound to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.sound(this);
    }
}
