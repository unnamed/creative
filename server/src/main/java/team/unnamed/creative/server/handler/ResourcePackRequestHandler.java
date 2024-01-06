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
package team.unnamed.creative.server.handler;

import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.server.request.ResourcePackDownloadRequest;

import java.io.IOException;

/**
 * Responsible for handling resource-pack download requests.
 *
 * <p>Download requests may or may not be executed by Minecraft
 * clients, this is determined by the nullness of the received
 * {@link ResourcePackDownloadRequest} instance.</p>
 *
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResourcePackRequestHandler {
    /**
     * Creates a new {@link ResourcePackRequestHandler} instance
     * that will always return the given resource-pack.
     *
     * @param pack      The resource-pack to return
     * @param validOnly Whether to only return the resource-pack
     *                  if the request is valid (has all the values
     *                  that a Minecraft vanilla client would send)
     * @return The new handler instance
     * @since 1.2.0
     */
    @Contract("_, _ -> new")
    static @NotNull ResourcePackRequestHandler fixed(final @NotNull BuiltResourcePack pack, final boolean validOnly) {
        return new FixedResourcePackRequestHandler(pack, validOnly);
    }

    /**
     * Creates a new {@link ResourcePackRequestHandler} instance
     * that will always return the given resource-pack.
     *
     * @param pack The resource-pack to return
     * @return The new handler instance
     * @since 1.2.0
     */
    @Contract("_ -> new")
    static @NotNull ResourcePackRequestHandler fixed(final @NotNull BuiltResourcePack pack) {
        return fixed(pack, false);
    }

    /**
     * Handles a resource pack request, the resulting resource
     * pack is written to {@link HttpExchange#getResponseBody()}
     *
     * <p>An "application/zip" Content-Type header should be set
     * when returning a resource-pack, {@link HttpExchange#getResponseHeaders()}</p>
     *
     * @param request  The resource pack request, null means that the
     *                 request couldn't be parsed and the requester is
     *                 not a Minecraft client
     * @param exchange The HTTP exchange
     * @throws IOException If writing the response fails
     * @see HttpExchange
     * @since 1.2.0
     */
    void onRequest(final @Nullable ResourcePackDownloadRequest request, final @NotNull HttpExchange exchange) throws IOException;
}
