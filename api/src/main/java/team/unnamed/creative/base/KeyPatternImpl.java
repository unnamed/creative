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
package team.unnamed.creative.base;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

final class KeyPatternImpl implements KeyPattern {

    static final KeyPattern ANY = new KeyPatternImpl(null, null);

    private final @Nullable Pattern namespace;
    private final @Nullable Pattern value;

    KeyPatternImpl(
            final @Nullable Pattern namespace,
            final @Nullable Pattern value
    ) {
        this.namespace = namespace;
        this.value = value;
    }

    @Override
    public @Nullable Pattern namespace() {
        return namespace;
    }

    @Override
    public @Nullable Pattern value() {
        return value;
    }

    @Override
    public boolean test(final @NotNull Key key) {
        Objects.requireNonNull(key, "key");
        return (namespace == null || namespace.matcher(key.namespace()).matches())
                && (value == null || value.matcher(key.value()).matches());
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("namespace", namespace),
                ExaminableProperty.of("value", value)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        KeyPatternImpl that = (KeyPatternImpl) other;
        return patternEquals(namespace, that.namespace)
                && patternEquals(value, that.value);
    }

    private static boolean patternEquals(final @Nullable Pattern a, final @Nullable Pattern b) {
        if (a == b) return true;
        if (a == null) return false;
        if (b == null) return false;
        return a.pattern().equals(b.pattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }

}
