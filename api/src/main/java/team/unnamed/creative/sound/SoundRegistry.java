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
import net.kyori.adventure.key.Namespaced;
import net.kyori.examination.Examinable;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@link SoundEvent} registry.
 *
 * @since 1.0.0
 */
public interface SoundRegistry extends ResourcePackPart, Namespaced, Examinable {
    /**
     * Creates a new registry from the given
     * sounds.
     *
     * @param namespace The sound registry namespace
     * @param sounds    The registered sounds
     * @return A new sound registry instance
     * @since 1.1.0
     */
    static @NotNull SoundRegistry soundRegistry(final @NotNull String namespace, final @NotNull Set<SoundEvent> sounds) {
        return new SoundRegistryImpl(namespace, sounds);
    }

    /**
     * Creates a new registry from the given
     * sounds.
     *
     * @param namespace The sound registry namespace
     * @param sounds    The registered sounds
     * @return A new sound registry instance
     * @since 1.6.0
     */
    static @NotNull SoundRegistry soundRegistry(final @NotNull String namespace, final @NotNull Collection<SoundEvent> sounds) {
        return new SoundRegistryImpl(namespace, sounds);
    }

    /**
     * Creates a new registry builder.
     *
     * @return A new registry builder
     * @since 1.1.0
     */
    static @NotNull Builder soundRegistry() {
        return new SoundRegistryImpl.BuilderImpl();
    }

    /**
     * Gets the sound registry namespace.
     *
     * <p>The returned namespace is a valid argument
     * for the first parameter of {@link Key#key(String, String)}.
     * </p>
     *
     * @return The sound registry namespace
     * @since 1.0.0
     */
    @Override
    @Pattern("[a-z0-9_\\-.]+")
    @Subst("minecraft")
    @NotNull String namespace();

    /**
     * Returns an unmodifiable collection of
     * the registered sound events.
     *
     * <p>Note that all the sound events in this
     * collection will have a namespace equal to
     * the registry {@link #namespace() namespace}.</p>
     *
     * @return The registered sound events
     * @since 1.0.0
     */
    @Unmodifiable @NotNull Collection<SoundEvent> sounds();

    /**
     * Returns the sound event with the given
     * key, or null if not found.
     *
     * @param key The sound event key
     * @return The sound event with the given key
     * @since 1.0.0
     */
    @Nullable SoundEvent sound(final @NotNull Key key);

    /**
     * Adds this sound registry to the given resource container.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.soundRegistry(this);
    }

    /**
     * A sound registry builder.
     *
     * @since 1.0.0
     */
    interface Builder {

        /**
         * Sets the sound registry namespace.
         *
         * @param namespace The sound registry namespace
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder namespace(final @NotNull String namespace);

        /**
         * Adds the given sound event to the registry.
         *
         * @param event The sound event
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder sound(final @NotNull SoundEvent event);

        /**
         * Sets the given sound events to the registry.
         *
         * @param sounds The sound events
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder sounds(final @NotNull Collection<? extends SoundEvent> sounds);

        /**
         * Builds the sound registry.
         *
         * @return The built sound registry
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull SoundRegistry build();
    }
}
