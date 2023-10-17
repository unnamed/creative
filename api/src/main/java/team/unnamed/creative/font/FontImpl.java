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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class FontImpl implements Font {
    private final Key key;
    private final List<FontProvider> providers;

    FontImpl(
            final @NotNull Key key,
            final @NotNull List<FontProvider> providers
    ) {
        this.key = requireNonNull(key, "key");
        this.providers = requireNonNull(providers, "providers");
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Font key(final @NotNull Key key) {
        requireNonNull(key, "key");
        return new FontImpl(key, this.providers);
    }

    @Override
    public @NotNull List<FontProvider> providers() {
        return providers;
    }

    @Override
    public @NotNull Font providers(final @NotNull List<FontProvider> providers) {
        requireNonNull(providers, "providers");
        return new FontImpl(this.key, providers);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("providers", providers)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontImpl that = (FontImpl) o;
        return key.equals(that.key)
                && providers.equals(that.providers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, providers);
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    static final class BuilderImpl implements Builder {

        private Key key;
        private List<FontProvider> providers;

        BuilderImpl() {
        }

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder providers(final @NotNull List<FontProvider> providers) {
            this.providers = new ArrayList<>(requireNonNull(providers, "providers"));
            return this;
        }

        @Override
        public @NotNull Builder providers(final @NotNull FontProvider @NotNull ... providers) {
            return providers(Arrays.asList(providers));
        }

        @Override
        public @NotNull Builder addProvider(final @NotNull FontProvider provider) {
            requireNonNull(provider, "provider");
            if (providers == null) {
                providers = new ArrayList<>();
            }
            providers.add(provider);
            return this;
        }

        @Override
        public @NotNull Font build() {
            return new FontImpl(key, providers);
        }
    }
}
