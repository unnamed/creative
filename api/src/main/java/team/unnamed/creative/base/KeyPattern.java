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
import net.kyori.examination.Examinable;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Represents a {@link Key} pattern or predicate, uses
 * regular expressions to match {@link Key#namespace()}'s
 * and/or {@link Key#value()}'s
 *
 * @since 1.0.0
 */
public interface KeyPattern extends Examinable {

    /**
     * Returns the {@link Key#namespace() key's namespace}
     * pattern to match.
     *
     * <p>If null, it will apply to every namespace</p>
     *
     * @return The namespace pattern
     * @since 1.0.0
     */
    @Nullable Pattern namespace();

    /**
     * Returns the {@link Key#value() key's value}
     * pattern to match.
     *
     * <p>If null, it will apply to every value</p>
     *
     * @return The value pattern
     * @since 1.0.0
     */
    @Nullable Pattern value();

    /**
     * Tests if the given {@link Key} matches with this
     * pattern. Results match with Minecraft Vanilla
     * Client tests
     *
     * @param key The tested key
     * @return True if it matches and should be filtered
     */
    boolean test(final @NotNull Key key);

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * namespace pattern and value pattern
     *
     * @param namespace The namespace pattern
     * @param value     The value pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern of(
            final @Nullable Pattern namespace,
            final @Nullable Pattern value
    ) {
        return new KeyPatternImpl(namespace, value);
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * namespace pattern and value pattern
     *
     * @param namespace The namespace pattern
     * @param value     The value pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern of(
            final @RegExp @Nullable String namespace,
            final @RegExp @Nullable String value
    ) {
        return of(
                namespace == null ? null : Pattern.compile(namespace),
                value == null ? null : Pattern.compile(value)
        );
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * namespace pattern and will filter any value
     *
     * @param namespace The namespace pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern ofNamespace(final @Nullable Pattern namespace) {
        return of(namespace, null);
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * namespace pattern and will filter any value
     *
     * @param namespace The namespace pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern ofNamespace(final @RegExp @Nullable String namespace) {
        return of(namespace == null ? null : Pattern.compile(namespace), null);
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * value pattern and will apply to every namespace
     *
     * @param value The value pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern ofValue(final @Nullable Pattern value) {
        return of(null, value);
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter keys using the specified
     * value pattern and will apply to every namespace
     *
     * @param value The value pattern
     * @return A new {@link KeyPattern} instance
     * @since 1.0.0
     */
    static @NotNull KeyPattern ofValue(final @RegExp @Nullable String value) {
        return of(null, value == null ? null : Pattern.compile(value));
    }

    /**
     * Creates a new {@link KeyPattern} instance
     * that will filter any file
     *
     * @return The filtering pattern
     * @since 1.0.0
     */
    static @NotNull KeyPattern any() {
        return KeyPatternImpl.ANY;
    }

}
