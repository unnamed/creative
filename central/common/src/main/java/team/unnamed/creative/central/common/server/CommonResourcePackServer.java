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
package team.unnamed.creative.central.common.server;

import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.central.server.CentralResourcePackServer;
import team.unnamed.creative.server.ResourcePackRequest;
import team.unnamed.creative.server.ResourcePackRequestHandler;
import team.unnamed.creative.server.ResourcePackServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public final class CommonResourcePackServer implements CentralResourcePackServer, ResourcePackRequestHandler {

    private @Nullable ResourcePackServer server;
    private @Nullable String address;
    private int port = -1;
    private @Nullable BuiltResourcePack resourcePack;
    private boolean open;

    @Override
    public @Nullable BuiltResourcePack resourcePack() {
        return resourcePack;
    }

    @Override
    public void resourcePack(@Nullable BuiltResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void open(String address, int port) throws IOException {
        if (open) {
            throw new IllegalStateException("The resource pack server is already open!");
        }

        this.address = address;
        this.port = port;
        this.open = true;

        server = ResourcePackServer.builder()
                .address(address, port)
                .handler(this)
                .build();
        server.start();
    }

    @Override
    public @Nullable String address() {
        return address;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public void onRequest(@Nullable ResourcePackRequest request, HttpExchange exchange) throws IOException {

        if (resourcePack == null) {
            byte[] response = "The resource-pack is not loaded yet, please wait...".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(400, response.length);
            exchange.getResponseBody().write(response);
            return;
        }

        byte[] data = resourcePack.bytes();
        exchange.getResponseHeaders().set("Content-Type", "application/zip");
        exchange.sendResponseHeaders(200, data.length);
        exchange.getResponseBody().write(data);
    }

    @Override
    public void onInvalidRequest(HttpExchange exchange) throws IOException {
        onRequest(null, exchange);
    }

    @Override
    public void close() {
        if (!open) return;
        if (server != null) {
            server.stop(0);
        }
        address = null;
        port = -1;
        open = false;
        server = null;
    }

}
