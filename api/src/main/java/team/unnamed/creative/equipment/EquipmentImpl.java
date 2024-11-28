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
package team.unnamed.creative.equipment;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EquipmentImpl implements Equipment {
    private final Key key;
    private final Key humanoid;
    private final Key humanoidLeggings;
    private final Key horseBody;
    private final Key wings;

    EquipmentImpl(
            final Key key,
            final @Nullable Key humanoid,
            final @Nullable Key humanoidLeggings,
            final @Nullable Key horseBody,
            final @Nullable Key wings
    ) {
        this.key = key;
        this.humanoid = humanoid;
        this.humanoidLeggings = humanoidLeggings;
        this.horseBody = horseBody;
        this.wings = wings;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @Nullable Key humanoid() {
        return humanoid;
    }

    @Override
    public @Nullable Key humanoidLeggings() {
        return humanoidLeggings;
    }

    @Override
    public @Nullable Key horseBody() {
        return horseBody;
    }

    @Override
    public @Nullable Key wings() {
        return wings;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("humanoid", humanoid),
                ExaminableProperty.of("humanoidLeggings", humanoidLeggings),
                ExaminableProperty.of("horseBody", horseBody),
                ExaminableProperty.of("wings", wings)
        );
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EquipmentImpl that = (EquipmentImpl) o;
        return key.equals(that.key)
                && Objects.equals(humanoid, that.humanoid)
                && Objects.equals(humanoidLeggings, that.humanoidLeggings)
                && Objects.equals(horseBody, that.horseBody)
                && Objects.equals(wings, that.wings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, humanoid, humanoidLeggings, horseBody, wings);
    }



    static final class BuilderImpl implements Equipment.Builder {
        private Key key;
        private Key humanoid;
        private Key humanoidLeggings;
        private Key horseBody;
        private Key wings;

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder humanoid(final @Nullable Key humanoid) {
            this.humanoid = humanoid;
            return this;
        }

        @Override
        public @NotNull Builder humanoidLeggings(final @Nullable Key humanoidLeggings) {
            this.humanoidLeggings = humanoidLeggings;
            return this;
        }

        @Override
        public @NotNull Builder horseBody(final @Nullable Key horseBody) {
            this.horseBody = horseBody;
            return this;
        }

        @Override
        public @NotNull Builder wings(final @Nullable Key wings) {
            this.wings = wings;
            return this;
        }

        @Override
        public @NotNull Equipment build() {
            return new EquipmentImpl(key, humanoid, humanoidLeggings, horseBody, wings);
        }

    }
}
