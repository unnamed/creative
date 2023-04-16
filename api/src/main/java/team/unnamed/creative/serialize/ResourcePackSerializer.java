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
package team.unnamed.creative.serialize;

import team.unnamed.creative.BuiltResourcePack;

import java.util.function.Consumer;

/**
 * @since 1.0.0
 */
public interface ResourcePackSerializer<I, O> {

    ResourcePackWriter<?> writer(O output);

    ResourcePackBuilder builder();

    void pipe(ResourcePackBuilder builder, ResourcePackWriter<?> writer);

    void deserialize(I dataSource, ResourcePackWriter<?> into);

    default ResourcePackBuilder deserialize(I dataSource) {
        ResourcePackBuilder builder = builder();
        deserialize(dataSource, builder);
        return builder;
    }

    default void serialize(ResourcePackBuilder resourcePack, O output) {
        pipe(resourcePack, writer(output));
    }

    BuiltResourcePack build(Consumer<ResourcePackBuilder> consumer);

}
