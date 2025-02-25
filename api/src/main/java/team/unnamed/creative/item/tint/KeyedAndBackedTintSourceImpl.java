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
package team.unnamed.creative.item.tint;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class KeyedAndBackedTintSourceImpl implements KeyedAndBackedTintSource {
    static final Key DYE = Key.key("dye");
    static final Key FIREWORK = Key.key("firework");
    static final Key MAP_COLOR = Key.key("map_color");
    static final Key POTION = Key.key("potion");
    static final Key TEAM = Key.key("team");

    private final Key key;
    private final int defaultTint;

    KeyedAndBackedTintSourceImpl(final @NotNull Key key, final int defaultTint) {
        this.key = requireNonNull(key, "key");
        this.defaultTint = defaultTint;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public int defaultTint() {
        return defaultTint;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
            ExaminableProperty.of("key", key),
            ExaminableProperty.of("defaultTint", defaultTint)
        );
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final KeyedAndBackedTintSourceImpl that = (KeyedAndBackedTintSourceImpl) o;
        return key.equals(that.key) && defaultTint == that.defaultTint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, defaultTint);
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }
}
