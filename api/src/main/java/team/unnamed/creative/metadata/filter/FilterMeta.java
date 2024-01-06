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
package team.unnamed.creative.metadata.filter;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Arrays;
import java.util.List;

/**
 * Class representing the "filter" section of the
 * pack.mcmeta file for Minecraft Resource Packs
 *
 * <p>Makes the client ignore the specified files
 * from resource-packs below this one by using
 * {@link KeyPattern filtering patterns}</p>
 *
 * @sincePackFormat 9
 * @sinceMinecraft 1.19
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface FilterMeta extends MetadataPart {
    /**
     * Creates a new {@link FilterMeta} from the given
     * pattern list
     *
     * @param patterns The key patterns to use
     * @return A new {@link FilterMeta} instance
     * @sincePackFormat 9
     * @sinceMinecraft 1.19
     * @since 1.3.0
     */
    @Contract("_ -> new")
    static @NotNull FilterMeta filter(final @NotNull List<KeyPattern> patterns) {
        return new FilterMetaImpl(patterns);
    }

    /**
     * Creates a new {@link FilterMeta} from the given
     * pattern array
     *
     * @param patterns The key patterns to use
     * @return A new {@link FilterMeta} instance
     * @sincePackFormat 9
     * @sinceMinecraft 1.19
     * @since 1.3.0
     */
    @Contract("_ -> new")
    static @NotNull FilterMeta filter(final @NotNull KeyPattern @NotNull ... patterns) {
        return filter(Arrays.asList(patterns));
    }

    /**
     * @return Nothing.
     * @throws UnsupportedOperationException Always.
     * @since 1.3.0
     * @deprecated Use {@link #filter(KeyPattern...)} instead
     */
    @Deprecated
    @Contract("-> fail")
    static @NotNull FilterMeta filter() {
        throw new UnsupportedOperationException("Cannot create an empty filter meta");
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
     * @sincePackFormat 9
     * @sinceMinecraft 1.19
     * @since 1.0.0
     */
    @NotNull @Unmodifiable List<KeyPattern> patterns();
}
