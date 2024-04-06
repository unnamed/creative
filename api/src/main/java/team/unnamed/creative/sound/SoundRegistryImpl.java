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
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.Keys;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class SoundRegistryImpl implements SoundRegistry {

    @Subst(Key.MINECRAFT_NAMESPACE)
    private final String namespace;
    private final Map<Key, SoundEvent> sounds;

    SoundRegistryImpl(
            final @NotNull String namespace,
            final @NotNull Collection<SoundEvent> sounds
    ) {
        this.namespace = requireNonNull(namespace, "namespace");
        requireNonNull(sounds, "sounds");
        this.sounds = new LinkedHashMap<>();
        for (SoundEvent soundEvent : sounds) {
            this.sounds.put(soundEvent.key(), soundEvent);
        }
        validate();
    }

    private void validate() {
        Keys.validateNamespace(namespace);
        for (Key key : sounds.keySet()) {
            if (!key.namespace().equals(this.namespace))
                throw new IllegalArgumentException("Sound events can't have a namespace different from the sound registry namespace!");
        }
    }

    @Pattern("[a-z0-9_\\-.]+")
    @Override
    public @NotNull String namespace() {
        return namespace;
    }

    @Override
    public @Unmodifiable @NotNull Collection<SoundEvent> sounds() {
        return sounds.values();
    }

    @Override
    public @Nullable SoundEvent sound(final @NotNull Key key) {
        requireNonNull(key, "key");
        return sounds.get(key);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("namespace", namespace),
                ExaminableProperty.of("sounds", sounds)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundRegistryImpl that = (SoundRegistryImpl) o;
        return namespace.equals(that.namespace)
                && sounds.equals(that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, sounds);
    }

    static final class BuilderImpl implements Builder {

        private String namespace;
        private Set<SoundEvent> sounds;

        @Override
        public @NotNull Builder namespace(final @NotNull String namespace) {
            this.namespace = requireNonNull(namespace, "namespace");
            return this;
        }

        @Override
        public @NotNull Builder sound(final @NotNull SoundEvent event) {
            requireNonNull(event, "event");
            if (sounds == null) {
                sounds = new HashSet<>();
            }
            sounds.add(event);
            return this;
        }

        @Override
        public @NotNull Builder sounds(final @NotNull Collection<? extends SoundEvent> sounds) {
            requireNonNull(sounds, "sounds");
            this.sounds = new LinkedHashSet<>(sounds);
            return this;
        }

        @Override
        public @NotNull SoundRegistry build() {
            return new SoundRegistryImpl(namespace, sounds);
        }
    }
}
