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
package team.unnamed.creative;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Writable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

/**
 * Represents a built server-side resource-pack ready
 * to be downloaded by a player via HTTP.
 *
 * <p>This class contains the data and SHA-1 hash of
 * the resource-pack ZIP file.</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface BuiltResourcePack extends Examinable {
    /**
     * Creates a new {@link BuiltResourcePack} instance
     * from the given data and hash.
     *
     * @param data The resource-pack zip archive data
     * @param hash The SHA-1 hash of the resource-pack
     * @return The built resource-pack instance
     * @since 1.1.0
     */
    static @NotNull BuiltResourcePack of(final @NotNull Writable data, final @NotNull String hash) {
        return new BuiltResourcePackImpl(data, hash);
    }

    /**
     * Creates a new {@link BuiltResourcePack} instance
     * from the given bytes and hash.
     *
     * @param bytes The resource-pack zip archive bytes
     * @param hash  The SHA-1 hash of the resource-pack
     * @return The built resource-pack instance
     * @since 1.0.0
     * @deprecated Use {@link #of(Writable, String)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull BuiltResourcePack of(final byte @NotNull [] bytes, final @NotNull String hash) {
        Objects.requireNonNull(bytes, "bytes");
        return new BuiltResourcePackImpl(Writable.bytes(bytes), hash);
    }

    /**
     * Returns the resource-pack zip archive
     * data.
     *
     * @return The resource-pack zip archive data
     * @since 1.1.0
     */
    @NotNull Writable data();

    /**
     * Returns the resource-pack zip archive
     * bytes.
     *
     * @return The resource-pack file data
     * @since 1.0.0
     * @deprecated Use {@link #data()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    default byte @NotNull [] bytes() {
        try {
            return data().toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to convert data to byte array", e);
        }
    }

    /**
     * Returns the SHA-1 hash of the resource-pack.
     *
     * @return The SHA-1 hash of the resource-pack
     * @since 1.0.0
     */
    @NotNull String hash();
}
