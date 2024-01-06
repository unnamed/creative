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
package team.unnamed.creative.part;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a resource-pack part.
 *
 * <p>The resource-pack part can contain one or multiple
 * resource-pack elements that can be added to an existing
 * resource-pack.</p>
 *
 * <p>This interface allows developers to extract parts
 * of the resource-pack in different objects</p>
 *
 * <p>See the following example on making a "serializer" that
 * converts custom blocks to something that can be written to
 * a resource-pack.</p>
 * <pre>{@code
 *  interface CustomBlockSerializer<T> {
 *      T serialize(CustomBlock block);
 *  }
 *
 *  class ResourcePackCustomBlockSerializer implements CustomBlockSerializer<ResourcePackPart> {
 *      @Override
 *      public ResourcePackPart serialize(CustomBlock block) {
 *          return ResourcePackPart.compound(
 *              BlockState.blockState(...),
 *              Texture.texture(...),
 *              Model.model(...)
 *          );
 *      }
 *  }
 * }</pre>
 *
 * @since 1.1.0
 */
public interface ResourcePackPart {

    /**
     * Adds the resource-pack part to the given
     * {@code resourceContainer}.
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    void addTo(final @NotNull ResourceContainer resourceContainer);

    /**
     * Creates a new resource-pack part that is compound
     * of the given {@code parts}.
     *
     * @param parts The resource pack parts
     * @return The new resource-pack part
     * @since 1.1.0
     */
    static @NotNull ResourcePackPart compound(final @NotNull Collection<? extends ResourcePackPart> parts) {
        return new ResourcePackPartCompound(new ArrayList<>(parts));
    }

    /**
     * Creates a new resource-pack part that is compound
     * of the given {@code parts}.
     *
     * @param parts The resource pack parts
     * @return The new resource-pack part
     * @since 1.1.0
     */
    static @NotNull ResourcePackPart compound(final @NotNull ResourcePackPart @NotNull ... parts) {
        return compound(Arrays.asList(parts));
    }

}
