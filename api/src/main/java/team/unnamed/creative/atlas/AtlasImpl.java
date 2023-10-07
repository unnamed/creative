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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.MoreCollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class AtlasImpl implements Atlas {

    private final Key key;
    private final List<AtlasSource> sources;

    AtlasImpl(
            final @NotNull Key key,
            final @NotNull List<AtlasSource> sources
    ) {
        this.key = requireNonNull(key, "key");
        this.sources = MoreCollections.immutableListOf(requireNonNull(sources, "sources"));
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @Unmodifiable @NotNull List<AtlasSource> sources() {
        return sources;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return Atlas.builder().key(key).sources(sources);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("sources", sources)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtlasImpl atlas = (AtlasImpl) o;
        if (!key.equals(atlas.key)) return false;
        return sources.equals(atlas.sources);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + sources.hashCode();
        return result;
    }

    static class BuilderImpl implements Builder {

        private Key key;
        private List<AtlasSource> sources;

        BuilderImpl() {
        }

        @Override
        public @Nullable Key key() {
            return key;
        }

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @Nullable List<AtlasSource> sources() {
            return sources;
        }

        @Override
        public @NotNull Builder sources(final @NotNull List<AtlasSource> sources) {
            requireNonNull(sources, "sources");
            this.sources = new ArrayList<>(sources);
            return this;
        }

        @Override
        public @NotNull Builder sources(final @NotNull AtlasSource @NotNull ... sources) {
            requireNonNull(sources, "sources");
            this.sources = new ArrayList<>(Arrays.asList(sources));
            return this;
        }

        @Override
        public @NotNull Builder addSource(final @NotNull AtlasSource source) {
            requireNonNull(source, "source");
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(source);
            return this;
        }

        @Override
        public @NotNull Atlas build() {
            return new AtlasImpl(key, sources);
        }
    }
}
