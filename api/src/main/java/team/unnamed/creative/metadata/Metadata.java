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
package team.unnamed.creative.metadata;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * Represents a metadata container for a specific
 * metadata-able resource or for the resource-pack
 * information.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface Metadata extends Examinable {
    /**
     * Creates a new {@link Metadata} builder.
     *
     * @return The builder
     * @since 1.2.0
     */
    @Contract("-> new")
    static @NotNull Builder metadata() {
        return new MetadataImpl.BuilderImpl();
    }

    /**
     * Creates an empty {@link Metadata} instance.
     *
     * @return The empty metadata
     * @since 1.2.0
     */
    static @NotNull Metadata empty() {
        return MetadataImpl.EMPTY;
    }

    /**
     * Gets all the metadata parts in this metadata object.
     *
     * @return The metadata parts
     * @since 1.0.0
     */
    @Unmodifiable @NotNull Collection<MetadataPart> parts();

    /**
     * Gets the metadata part of the given type.
     *
     * @param type The metadata part type
     * @param <T>  The metadata part type
     * @return The metadata part, or null if not present
     * @since 1.0.0
     */
    <T extends MetadataPart> @Nullable T meta(final @NotNull Class<T> type);

    /**
     * Converts this metadata object to a {@link Builder}
     * with all the metadata parts in this object.
     *
     * @return The builder
     * @since 1.0.0
     */
    @Contract("-> new")
    @NotNull Builder toBuilder();

    interface Builder {
        /**
         * Sets the metadata parts of this builder.
         *
         * @param parts The metadata parts
         * @return This builder
         * @since 1.4.0
         */
        @NotNull Builder parts(final @NotNull Collection<MetadataPart> parts);

        /**
         * Adds a metadata part to this builder.
         *
         * @param part The metadata part
         * @return This builder
         * @since 1.4.0
         */
        @NotNull Builder addPart(final @NotNull MetadataPart part);

        /**
         * Builds a new {@link Metadata} instance.
         *
         * @return The metadata
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull Metadata build();
    }

}
