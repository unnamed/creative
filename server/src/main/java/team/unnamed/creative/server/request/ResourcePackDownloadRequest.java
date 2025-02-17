/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
package team.unnamed.creative.server.request;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a request to download a resource pack, sent by
 * vanilla Minecraft clients.
 *
 * <p>This is a type containing all the information that the vanilla
 * Minecraft client sends to the server when it wants to download
 * a resource pack.</p>
 *
 * @since 1.2.0
 */
@ApiStatus.NonExtendable
public interface ResourcePackDownloadRequest extends Examinable {
    /**
     * Creates a new {@link ResourcePackDownloadRequest} instance
     * with the given parameters.
     *
     * @param uuid            The requester player UUID
     * @param username        The requester player username
     * @param clientVersion   The requester client version name
     * @param clientVersionId The requester client version id
     * @param packFormat      The client pack format
     * @return The created request
     * @since 1.2.0
     */
    static @NotNull ResourcePackDownloadRequest request(final @NotNull UUID uuid, final @NotNull String username, final @NotNull String clientVersion, final @NotNull String clientVersionId, final int packFormat) {
        return new ResourcePackDownloadRequestImpl(uuid, username, clientVersion, clientVersionId, packFormat);
    }

    /**
     * Returns the uuid of the requester player.
     *
     * @return The requester player UUID
     * @since 1.2.0
     */
    @NotNull UUID uuid();

    /**
     * Returns the requester player's username.
     *
     * @return The requester player username
     * @since 1.2.0
     */
    @NotNull String username();

    /**
     * Returns the requester player's client version
     * name, e.g. "1.18.1".
     *
     * @return The requester client version name
     * @since 1.2.0
     */
    @NotNull String clientVersion();

    /**
     * Returns the requester player's client version
     * id, e.g. "1.18.1".
     *
     * @return The requester client version id
     * @since 1.2.0
     */
    @NotNull String clientVersionId();

    /**
     * Returns the pack format that the client supports.
     *
     * @return The client pack format
     * @since 1.2.0
     */
    int packFormat();
}
