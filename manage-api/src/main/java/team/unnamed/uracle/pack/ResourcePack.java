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
package team.unnamed.uracle.pack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Representation of a Minecraft resource-pack, it should
 * always represent the newest resource-pack across the
 * Minecraft versions
 *
 * <p>It is a combination of {@link ResourcePackLocation}
 * and {@link ResourcePackApplication} interfaces</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ResourcePack
        extends ResourcePackLocation, ResourcePackApplication {

    /**
     * Creates a new {@link ResourcePack} with the
     * given {@link ResourcePackLocation}. All other
     * properties are kept
     *
     * @param location The new resource pack location
     * @return A new {@link ResourcePack} with the
     * given location
     * @since 1.0.0
     */
    ResourcePack withLocation(ResourcePackLocation location);

    /**
     * Creates a new {@link ResourcePack} with the
     * given {@link ResourcePackApplication} properties.
     *
     * <p>The location properties are kept</p>
     *
     * @param application The new resource pack properties
     * @return A new {@link ResourcePack} with the
     * given application properties
     */
    ResourcePack withProperties(ResourcePackApplication application);

    /**
     * Creates a new {@link ResourcePack} instance from
     * the given components (location and application)
     *
     * @param location The resource pack location
     * @param application The pack application settings
     * @return A new {@link ResourcePack} instance
     * @since 1.0.0
     */
    static ResourcePack of(
            ResourcePackLocation location,
            ResourcePackApplication application
    ) {
        return of(
                location.url(),
                location.hash(),
                application.required(),
                application.prompt()
        );
    }

    /**
     * Creates a new {@link ResourcePack} instance from
     * the given values
     *
     * @param url The pack URL
     * @param hash The pack SHA-1 hash
     * @param required True if resource pack is required
     * @param prompt The resource-pack JSON prompt
     * @return A new {@link ResourcePack} instance
     * @since 1.0.0
     */
    static ResourcePack of(
            String url,
            String hash,
            boolean required,
            @Nullable String prompt
    ) {
        return new ResourcePackImpl(
                url,
                hash,
                required,
                prompt
        );
    }

}