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
package team.unnamed.creative.item.special;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class NoFieldSpecialRenderImpl implements NoFieldSpecialRender {
    static final NoFieldSpecialRender CONDUIT = new NoFieldSpecialRenderImpl("conduit"); // renders a conduit
    static final NoFieldSpecialRender DECORATED_POT = new NoFieldSpecialRenderImpl("decorated_pot"); // renders a decorated pot, uses values from minecraft:pot_decorations component
    static final NoFieldSpecialRender SHIELD = new NoFieldSpecialRenderImpl("shield"); // renders a shield, uses patterns from the minecraft:banner_patterns component and color from the minecraft:base_color component
    static final NoFieldSpecialRender TRIDENT = new NoFieldSpecialRenderImpl("trident"); // renders a trident
    static final NoFieldSpecialRender PLAYER_HEAD = new NoFieldSpecialRenderImpl("player_head"); // renders a player head

    private final Key key;

    NoFieldSpecialRenderImpl(final @NotNull Key key) {
        this.key = requireNonNull(key, "key");
    }

    private NoFieldSpecialRenderImpl(final @Subst("conduit") @NotNull String keyValue) {
        this.key = Key.key(Key.MINECRAFT_NAMESPACE, keyValue);
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
        final NoFieldSpecialRenderImpl that = (NoFieldSpecialRenderImpl) o;
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
