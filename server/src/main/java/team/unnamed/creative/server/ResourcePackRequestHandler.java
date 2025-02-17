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
package team.unnamed.creative.server;

import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.server.request.ResourcePackDownloadRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Responsible for handling resource-pack requests made by
 * Minecraft clients, using this we are able to detect some
 * information such as the expected pack format, the requester
 * player uuid, username and client version
 *
 * @since 1.0.0
 * @deprecated Use new {@link team.unnamed.creative.server.handler.ResourcePackRequestHandler} instead
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
@FunctionalInterface
public interface ResourcePackRequestHandler extends team.unnamed.creative.server.handler.ResourcePackRequestHandler {

    /**
     * Handles a resource pack request, the resulting resource
     * pack is written to {@link HttpExchange#getResponseBody()}
     *
     * <p>An "application/zip" Content-Type header should be set
     * when returning a resource-pack, {@link HttpExchange#getResponseHeaders()}</p>
     *
     * @param request  The resource pack request
     * @param exchange The HTTP exchange
     * @throws IOException If writing the response fails
     * @see HttpExchange
     */
    void onRequest(ResourcePackRequest request, HttpExchange exchange) throws IOException;

    @Override
    default void onRequest(final @Nullable ResourcePackDownloadRequest request, final @NotNull HttpExchange exchange) throws IOException {
        try {
            if (request == null) {
                onInvalidRequest(exchange);
            } else {
                onRequest(new ResourcePackRequest(
                        request.uuid(),
                        request.username(),
                        request.clientVersion(),
                        request.clientVersionId(),
                        request.packFormat()
                ), exchange);
            }
        } catch (final Exception e) {
            onException(e);
        } finally {
            exchange.close();
        }
    }

    /**
     * Handles an unhandled exception when invoking
     * {@link ResourcePackRequestHandler#onRequest}
     *
     * @param e The caught exception
     */
    default void onException(Exception e) {
        System.err.println("Exception caught when serving a resource-pack");
        e.printStackTrace();
    }

    /**
     * Called when an invalid request is made, by default,
     * a simple message is sent
     *
     * @param exchange The http exchange
     * @throws IOException If writing the response fails
     * @see HttpExchange
     */
    default void onInvalidRequest(HttpExchange exchange) throws IOException {
        byte[] response = "Please use a Minecraft client\n".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(400, response.length);
        try (OutputStream responseStream = exchange.getResponseBody()) {
            responseStream.write(response);
        }
    }

    static ResourcePackRequestHandler of(BuiltResourcePack pack, boolean validOnly) {
        return new ResourcePackRequestHandler() {

            @Override
            public void onRequest(@Nullable ResourcePackRequest request, HttpExchange exchange) throws IOException {
                if (request != null || !validOnly) {
                    byte[] data = pack.data().toByteArray();
                    exchange.getResponseHeaders().set("Content-Type", "application/zip");
                    exchange.sendResponseHeaders(200, data.length);
                    try (OutputStream responseStream = exchange.getResponseBody()) {
                        responseStream.write(data);
                    }
                } else {
                    ResourcePackRequestHandler.super.onInvalidRequest(exchange);
                }
            }

            @Override
            public void onInvalidRequest(HttpExchange exchange) throws IOException {
                onRequest(null, exchange);
            }

            @Override
            public String toString() {
                return "BuiltResourcePackRequestHandler{"
                        + "pack=" + pack
                        + ", validOnly=" + validOnly
                        + '}';
            }

        };
    }

    static ResourcePackRequestHandler of(BuiltResourcePack pack) {
        return of(pack, false);
    }

}
