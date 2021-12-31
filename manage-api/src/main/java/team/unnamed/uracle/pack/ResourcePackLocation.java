/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.pack;

import org.jetbrains.annotations.ApiStatus;

/**
 * Abstraction for objects that contain a
 * resource-pack location (URL and hash)
 *
 * <p>This interface properties complement
 * {@link ResourcePackApplication} to build
 * a complete Resource Pack Send packet</p>
 *
 * See https://wiki.vg/Protocol#Resource_Pack_Send
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ResourcePackLocation {

    /**
     * Returns the resource pack universal
     * resource location
     *
     * @return The resource-pack URL
     * @since 1.0.0
     */
    String url();

    /**
     * Returns the SHA-1 hash of the resource
     * pack.
     *
     * <p>If the downloaded resource-pack hash
     * and this hash doesn't match, the client
     * rejects the resource-pack</p>
     *
     * <p>Must be lower-case in order to work</p>
     *
     * @return The resource-pack SHA-1 hash
     * @since 1.0.0
     */
    String hash();

    /**
     * Creates a new {@link ResourcePackLocation}
     * instance from the simplest implementation,
     * with the specified values
     *
     * @param url The resource-pack URL
     * @param hash The resource-pack SHA-1 hash
     * @return A new {@link ResourcePackLocation} instance
     * @since 1.0.0
     */
    static ResourcePackLocation of(String url, String hash) {
        return new ResourcePackLocationImpl(url, hash);
    }

}
