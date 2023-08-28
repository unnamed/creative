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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static team.unnamed.creative.util.MoreCollections.immutableListOf;
import static team.unnamed.creative.util.Validate.isNotNull;
import static team.unnamed.creative.util.Validate.isTrue;

/**
 * Class representing the "filter" section of the
 * pack.mcmeta file for Minecraft Resource Packs
 *
 * <p>Makes the client ignore the specified files
 * from resource-packs below this one by using
 * {@link KeyPattern filtering patterns}</p>
 *
 * @since 1.0.0
 * @sincePackFormat 9
 */
public class FilterMeta implements MetadataPart {

    private final List<KeyPattern> patterns;

    private FilterMeta(List<KeyPattern> patterns) {
        isNotNull(patterns, "patterns");
        this.patterns = immutableListOf(patterns);
        validate();
    }

    private void validate() {
        isTrue(patterns.size() > 0, "Patterns list is empty!");
        for (KeyPattern pattern : patterns) {
            if (pattern == null) {
                throw new NullPointerException("An element in the patterns list is null");
            }
        }
    }

    /**
     * Returns the list of key patterns to block from
     * resource packs that are below this one.
     *
     * <p>The client will ignore files that match any
     * of these patterns on resource packs with less
     * priority than this one</p>
     *
     * @return The key patterns to filter
     * @since 1.0.0
     */
    public List<KeyPattern> patterns() {
        return patterns;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("patterns", patterns)
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
        FilterMeta that = (FilterMeta) o;
        return patterns.equals(that.patterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patterns);
    }

    /**
     * Creates a new {@link FilterMeta} from the given
     * pattern list
     *
     * @param patterns The key patterns to use
     * @return A new {@link FilterMeta} instance
     * @since 1.0.0
     */
    public static FilterMeta of(List<KeyPattern> patterns) {
        return new FilterMeta(patterns);
    }

    /**
     * Creates a new {@link FilterMeta} from the given
     * pattern array
     *
     * @param patterns The key patterns to use
     * @return A new {@link FilterMeta} instance
     * @since 1.0.0
     */
    public static FilterMeta of(KeyPattern... patterns) {
        return new FilterMeta(Arrays.asList(patterns));
    }

    @Deprecated
    public static FilterMeta of() {
        throw new UnsupportedOperationException("Cannot create an empty filter meta");
    }

}
