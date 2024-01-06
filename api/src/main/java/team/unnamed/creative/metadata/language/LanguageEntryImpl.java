/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
package team.unnamed.creative.metadata.language;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class LanguageEntryImpl implements LanguageEntry {
    private final String name;
    private final String region;
    private final boolean bidirectional;

    LanguageEntryImpl(final @NotNull String name, final @NotNull String region, final boolean bidirectional) {
        this.name = requireNonNull(name, "name");
        this.region = requireNonNull(region, "region");
        this.bidirectional = bidirectional;
        validate();
    }

    private void validate() {
        if (name.isEmpty())
            throw new IllegalArgumentException("'name' is empty!");
        if (region.isEmpty())
            throw new IllegalArgumentException("'region' is empty!");
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String region() {
        return region;
    }

    @Override
    public boolean bidirectional() {
        return bidirectional;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("name", name),
                ExaminableProperty.of("region", region),
                ExaminableProperty.of("bidirectional", bidirectional)
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
        final LanguageEntryImpl entry = (LanguageEntryImpl) o;
        return bidirectional == entry.bidirectional
                && name.equals(entry.name)
                && region.equals(entry.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, region, bidirectional);
    }

    static final class BuilderImpl implements Builder {
        private String name;
        private String region;
        private boolean bidirectional = false;

        @Override
        public @NotNull Builder name(final @NotNull String name) {
            this.name = requireNonNull(name, "name");
            return this;
        }

        @Override
        public @NotNull Builder region(final @NotNull String region) {
            this.region = requireNonNull(region, "region");
            return this;
        }

        @Override
        public @NotNull Builder bidirectional(final boolean bidirectional) {
            this.bidirectional = bidirectional;
            return this;
        }

        @Override
        public @NotNull LanguageEntry build() {
            return new LanguageEntryImpl(name, region, bidirectional);
        }
    }
}
