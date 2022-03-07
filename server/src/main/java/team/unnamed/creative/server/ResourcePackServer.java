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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class ResourcePackServer {

    private final HttpServer server;
    private final ResourcePackRequestHandler handler;

    private ResourcePackServer(
            InetSocketAddress address,
            String path,
            ResourcePackRequestHandler handler
    ) throws IOException {
        this.server = HttpServer.create(address, 0);
        this.handler = handler;
        this.server.createContext(path, this::handleRequest);
    }

    public HttpServer httpServer() {
        return server;
    }

    /**
     * Starts the internal {@link HttpServer}, it is started in a new
     * background thread, so this operation is not blocking
     */
    public void start() {
        server.start();
    }

    /**
     * Stops the internal {@link HttpServer}
     * 
     * @param delay the maximum time in seconds to wait until requests have finished
     * @see HttpServer#stop(int) 
     */
    public void stop(int delay) {
        server.stop(delay);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        String username = headers.getFirst("X-Minecraft-Username");
        String rawUuid = headers.getFirst("X-Minecraft-UUID");
        String clientVersion = headers.getFirst("X-Minecraft-Version");
        String clientVersionId = headers.getFirst("X-Minecraft-Version-ID");
        String rawPackFormat = headers.getFirst("X-Minecraft-Pack-Format");

        if (username == null
                || rawUuid == null
                || clientVersion == null
                || clientVersionId == null
                || rawPackFormat == null) {
            handler.onInvalidRequest(exchange);
            exchange.close();
            return;
        }

        // parse input data
        UUID uuid;
        int packFormat;

        try {
            uuid = UUIDUtil.fromUndashedString(rawUuid);
            packFormat = Integer.parseInt(rawPackFormat);
        } catch (IllegalArgumentException e) {
            handler.onInvalidRequest(exchange);
            exchange.close();
            return;
        }

        ResourcePackRequest request
                = new ResourcePackRequest(uuid, username, clientVersion, clientVersionId, packFormat);

        try {
            handler.onRequest(request, exchange);
        } catch (Exception e) {
            handler.onException(e);
        } finally {
            exchange.close();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private InetSocketAddress address;
        private ResourcePackRequestHandler handler;
        private String path = "/";

        private Builder() {
        }

        public Builder address(InetSocketAddress address) {
            this.address = requireNonNull(address, "address");
            return this;
        }

        public Builder address(String hostname, int port) {
            this.address = new InetSocketAddress(hostname, port);
            return this;
        }

        public Builder handler(ResourcePackRequestHandler handler) {
            this.handler = requireNonNull(handler, "handler");
            return this;
        }

        public Builder path(String path) {
            this.path = requireNonNull(path, "path");
            return this;
        }

        public ResourcePackServer create() throws IOException {
            requireNonNull(address, "Address must be set!");
            requireNonNull(handler, "Handler must be set!");
            return new ResourcePackServer(address, path, handler);
        }

    }

}
