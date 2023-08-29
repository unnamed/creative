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
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.util.Keys;
import team.unnamed.creative.util.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a registry of {@link SoundEvent}, or
 * "sounds.json" in the resource-pack
 *
 * @since 1.0.0
 */
public class SoundRegistry implements Namespaced, Examinable {

    @Subst(Key.MINECRAFT_NAMESPACE)
    private final String namespace;
    private final Map<Key, SoundEvent> sounds;

    private SoundRegistry(
            String namespace,
            Map<Key, SoundEvent> sounds
    ) {
        this.namespace = requireNonNull(namespace, "namespace");
        this.sounds = requireNonNull(sounds, "sounds");
        validate();
    }

    private void validate() {
        Keys.validateNamespace(namespace);
        for (Key key : sounds.keySet()) {
            Validate.isTrue(
                    key.namespace().equals(this.namespace),
                    "Sound events can't have a namespace different from the sound registry namespace!"
            );
        }
    }

    @Override
    @Pattern("[a-z0-9_\\-.]+")
    public @NotNull String namespace() {
        return namespace;
    }

    public Collection<SoundEvent> sounds() {
        return sounds.values();
    }

    public @Nullable SoundEvent sound(Key key) {
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
        SoundRegistry that = (SoundRegistry) o;
        return namespace.equals(that.namespace)
                && sounds.equals(that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, sounds);
    }

    /**
     * Creates a new registry from the given
     * sounds
     *
     * @param sounds The registered sounds
     * @return A new sound registry instance
     */
    public static SoundRegistry of(
            String namespace,
            Set<SoundEvent> sounds
    ) {
        Map<Key, SoundEvent> internalMap = new HashMap<>();
        for (SoundEvent sound : sounds) {
            internalMap.put(sound.key(), sound);
        }
        return new SoundRegistry(namespace, internalMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String namespace;
        private Set<SoundEvent> sounds;

        private Builder() {
        }

        public Builder namespace(String namespace) {
            this.namespace = requireNonNull(namespace, "namespace");
            return this;
        }

        public Builder sound(SoundEvent event) {
            requireNonNull(event, "event");
            if (sounds == null) {
                sounds = new HashSet<>();
            }
            sounds.add(event);
            return this;
        }

        public Builder sounds(Collection<? extends SoundEvent> sounds) {
            requireNonNull(sounds, "sounds");
            if (this.sounds == null) {
                this.sounds = new HashSet<>();
            }
            this.sounds.addAll(sounds);
            return this;
        }

        public SoundRegistry build() {
            requireNonNull(namespace, "namespace");
            requireNonNull(sounds, "sounds");
            return SoundRegistry.of(namespace, sounds);
        }

    }

}
