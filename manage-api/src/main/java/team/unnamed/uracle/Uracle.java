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
package team.unnamed.uracle;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.pack.ResourcePack;
import team.unnamed.uracle.player.PlayerManager;
import team.unnamed.uracle.pack.ResourcePackApplication;
import team.unnamed.uracle.pack.ResourcePackLocation;

/**
 * Represents the entry point object for the Uracle
 * API
 *
 * <p>Everything is accessed via this interface, plugins
 * can read and update state and call actions</p>
 */
public interface Uracle {

    /**
     * Returns the {@link PlayerManager} that this
     * service can provide, it is responsible for
     * managing server online players
     *
     * @return The player manager
     */
    PlayerManager players();

    /**
     * Returns the stored server resource-pack
     *
     * @return The server resource pack
     */
    @Nullable ResourcePack getPack();

    /**
     * Updates the resource pack application settings, will
     * re-ask to players who don't have the server resource-pack
     * if the new {@link ResourcePackApplication#required} value
     * is {@code true}
     *
     * @param application The new resource-pack settings
     */
    void setPackApplication(ResourcePackApplication application);

    void setPackLocation(ResourcePackLocation location);

    void fireGenerate();

}
