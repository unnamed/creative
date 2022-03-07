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
package team.unnamed.creative.server;

import com.sun.net.httpserver.HttpExchange;
import team.unnamed.creative.ResourcePack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Responsible for handling resource-pack requests made by
 * Minecraft clients, using this we are able to detect some
 * information such as the expected pack format, the requester
 * player uuid, username and client version
 */
@FunctionalInterface
public interface ResourcePackRequestHandler {

    /**
     * Handles a resource pack request, the resulting resource
     * pack is written to {@link HttpExchange#getResponseBody()}
     *
     * <p>An "application/zip" Content-Type header should be set
     * when returning a resource-pack, {@link HttpExchange#getResponseHeaders()}</p>
     *
     * @param request The resource pack request
     * @param exchange The HTTP exchange
     * @see HttpExchange
     */
    void onRequest(ResourcePackRequest request, HttpExchange exchange) throws IOException;

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
     * @see HttpExchange
     */
    default void onInvalidRequest(HttpExchange exchange) throws IOException {
        byte[] response = "Please use a Minecraft client".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(400, response.length);
        exchange.getResponseBody().write(response);
    }

    static ResourcePackRequestHandler of(ResourcePack pack) {
        return (request, exchange) -> {
            byte[] data = pack.bytes();
            exchange.getResponseHeaders().set("Content-Type", "application/zip");
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
        };
    }

}
