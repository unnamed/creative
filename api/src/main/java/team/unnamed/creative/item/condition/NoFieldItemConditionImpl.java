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
package team.unnamed.creative.item.condition;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class NoFieldItemConditionImpl implements NoFieldItemCondition {
    static final NoFieldItemCondition BROKEN = new NoFieldItemConditionImpl("broken");
    static final NoFieldItemCondition BUNDLE_HAS_SELECTED_ITEM = new NoFieldItemConditionImpl("bundle/has_selected_item");
    static final NoFieldItemCondition CARRIED = new NoFieldItemConditionImpl("carried");
    static final NoFieldItemCondition DAMAGED = new NoFieldItemConditionImpl("damaged");
    static final NoFieldItemCondition EXTENDED_VIEW = new NoFieldItemConditionImpl("extended_view");
    static final NoFieldItemCondition FISHING_ROD_CAST = new NoFieldItemConditionImpl("fishing_rod/cast");
    static final NoFieldItemCondition SELECTED = new NoFieldItemConditionImpl("selected");
    static final NoFieldItemCondition USING_ITEM = new NoFieldItemConditionImpl("using_item");
    static final NoFieldItemCondition VIEW_ENTITY = new NoFieldItemConditionImpl("view_entity");

    private final Key key;

    NoFieldItemConditionImpl(final @NotNull Key key) {
        this.key = requireNonNull(key, "key");
    }

    NoFieldItemConditionImpl(@Subst("minecraft:broken") final @NotNull String key) {
        this.key = Key.key(key);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("key", key));
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final NoFieldItemConditionImpl that = (NoFieldItemConditionImpl) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }
}
