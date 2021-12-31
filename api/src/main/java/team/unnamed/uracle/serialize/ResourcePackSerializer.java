/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.serialize;

import team.unnamed.uracle.ResourcePack;

/**
 * Responsible for serializing {@link ResourcePack}
 * instances and writing them into a specific {@code T}
 * type
 *
 * @param <T> The output type
 * @since 1.0.0
 */
@FunctionalInterface
public interface ResourcePackSerializer<T> {

    /**
     * Serializes the given {@link ResourcePack} instance
     * into the specified {@code T} output
     *
     * @param resourcePack The serialized resource pack
     * @param output The target output
     */
    void serialize(ResourcePack resourcePack, T output);

}
