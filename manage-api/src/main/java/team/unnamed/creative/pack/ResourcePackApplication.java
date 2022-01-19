/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.pack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Abstraction for objects that contain resource
 * pack application settings. In other words, it
 * contains the fields of the Resource Pack Send
 * packet that do not require the resource-pack
 * to be uploaded (no url, no hash)
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ResourcePackApplication {

    /**
     * Determines whether the resource-pack is required to
     * join the server
     *
     * <p>If a player does not accept the server resource-pack,
     * they get kicked from the server</p>
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return True if the resource-pack is required
     * @since 1.0.0
     */
    boolean required();

    /**
     * Returns the JSON representation of the resource
     * pack prompt
     *
     * <p>Shown when they are asked to download the
     * server resource-pack</p>
     *
     * <p>This setting is available since Minecraft 1.17</p>
     *
     * @return The JSON representation of the resource
     * pack prompt
     * @since 1.0.0
     */
    @Nullable String prompt();

    /**
     * Creates a new {@link ResourcePackApplication}
     * instance from the simplest implementation,
     * with the specified values
     *
     * @param required Determines if the pack is required
     * @param prompt The resource-pack prompt
     * @return A new {@link ResourcePackApplication} instance
     * @since 1.0.0
     */
    static ResourcePackApplication of(
            boolean required,
            @Nullable String prompt
    ) {
        return new ResourcePackApplicationImpl(required, prompt);
    }

}
