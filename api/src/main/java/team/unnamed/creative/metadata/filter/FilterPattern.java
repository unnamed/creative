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
package team.unnamed.creative.metadata.filter;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.util.Validate;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Represents a {@link Key} pattern or predicate, uses
 * regular expressions to match {@link Key#namespace()}'s
 * and/or {@link Key#value()}'s
 *
 * @since 1.0.0
 * @sincePackFormat 9
 */
public class FilterPattern implements Examinable {

    private static final FilterPattern ANY = new FilterPattern(null, null);

    private final @Nullable Pattern namespace;
    private final @Nullable Pattern value;

    private FilterPattern(
            @Nullable Pattern namespace,
            @Nullable Pattern value
    ) {
        this.namespace = namespace;
        this.value = value;
    }

    /**
     * Returns the {@link Key#namespace() key's namespace}
     * pattern to match.
     *
     * <p>If null, it will apply to every namespace</p>
     *
     * @return The namespace pattern
     */
    public @Nullable Pattern namespace() {
        return namespace;
    }

    /**
     * Returns the {@link Key#value() key's value}
     * pattern to match.
     *
     * <p>If null, it will apply to every value</p>
     *
     * @return The value pattern
     */
    public @Nullable Pattern value() {
        return value;
    }

    /**
     * Tests if the given {@link Key} matches with this
     * pattern. Results match with Minecraft Vanilla
     * Client tests
     *
     * @param key The tested key
     * @return True if it matches and should be filtered
     */
    public boolean test(Key key) {
        Validate.isNotNull(key, "key");
        return (namespace == null || namespace.matcher(key.namespace()).matches())
                && (value == null || value.matcher(key.value()).matches());
    }

    public void serialize(ResourceWriter writer) {
        writer.startObject();
        if (namespace != null) {
            writer.key("namespace").value(namespace.pattern());
        }
        if (value != null) {
            writer.key("path").value(value.pattern());
        }
        writer.endObject();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterPattern that = (FilterPattern) o;
        return Objects.equals(namespace, that.namespace)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * namespace pattern and value pattern
     *
     * @param namespace The namespace pattern
     * @param value The value pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern of(
            @Nullable Pattern namespace,
            @Nullable Pattern value
    ) {
        return new FilterPattern(namespace, value);
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * namespace pattern and value pattern
     *
     * @param namespace The namespace pattern
     * @param value The value pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern of(
            @RegExp @Nullable String namespace,
            @RegExp @Nullable String value
    ) {
        return of(
                namespace == null ? null : Pattern.compile(namespace),
                value == null ? null : Pattern.compile(value)
        );
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * namespace pattern and will filter any value
     *
     * @param namespace The namespace pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern ofNamespace(@Nullable Pattern namespace) {
        return of(namespace, null);
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * namespace pattern and will filter any value
     *
     * @param namespace The namespace pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern ofNamespace(@RegExp @Nullable String namespace) {
        return of(namespace == null ? null : Pattern.compile(namespace), null);
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * value pattern and will apply to every namespace
     *
     * @param value The value pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern ofValue(@Nullable Pattern value) {
        return of(null, value);
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter keys using the specified
     * value pattern and will apply to every namespace
     *
     * @param value The value pattern
     * @return A new {@link FilterPattern} instance
     * @since 1.0.0
     */
    public static FilterPattern ofValue(@RegExp @Nullable String value) {
        return of(null, value == null ? null : Pattern.compile(value));
    }

    /**
     * Creates a new {@link FilterPattern} instance
     * that will filter any file
     *
     * @return The filtering pattern
     * @since 1.0.0
     */
    public static FilterPattern any() {
        return ANY;
    }

}
