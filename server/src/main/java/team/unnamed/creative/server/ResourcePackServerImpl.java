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
package team.unnamed.creative.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.server.handler.ResourcePackRequestHandler;
import team.unnamed.creative.server.request.ResourcePackDownloadRequest;
import team.unnamed.creative.server.util.ResourcePackDownloadRequestParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

final class ResourcePackServerImpl implements ResourcePackServer {

    private final HttpServer server;
    private final ResourcePackRequestHandler handler;

    ResourcePackServerImpl(
            final @NotNull HttpServer server,
            final @NotNull String path,
            final @NotNull ResourcePackRequestHandler handler
    ) {
        this.server = requireNonNull(server, "server");
        this.handler = requireNonNull(handler, "handler");
        this.server.createContext(requireNonNull(path, "path"), this::handleRequest);
    }

    @Override
    public @NotNull InetSocketAddress address() {
        return server.getAddress();
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop(final int delay) {
        server.stop(delay);
    }

    private void handleRequest(final @NotNull HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.close();
            return;
        }

        final Headers headers = exchange.getRequestHeaders();
        final ResourcePackDownloadRequest request = ResourcePackDownloadRequestParser.parse(headers);

        try {
            handler.onRequest(request, exchange);
        } finally {
            exchange.close();
        }
    }

    static final class BuilderImpl implements Builder {
        private InetSocketAddress address;
        private Executor executor;
        private int backlog;
        private ResourcePackRequestHandler handler;
        private String path = "/";
        private HttpServerFactory serverFactory = HttpServer::create;

        @Override
        public @NotNull Builder address(final @NotNull InetSocketAddress address) {
            this.address = requireNonNull(address, "address");
            return this;
        }

        @Override
        public @NotNull Builder executor(final @Nullable Executor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public @NotNull Builder secure(final @NotNull HttpsConfigurator httpsConfigurator) {
            requireNonNull(httpsConfigurator, "httpsConfigurator");
            this.serverFactory = (address, backlog) -> {
                HttpsServer server = HttpsServer.create(address, backlog);
                server.setHttpsConfigurator(httpsConfigurator);
                return server;
            };
            return this;
        }

        @Override
        public @NotNull Builder backlog(final int backlog) {
            this.backlog = backlog;
            return this;
        }

        @Override
        public @NotNull Builder handler(final @NotNull ResourcePackRequestHandler handler) {
            this.handler = requireNonNull(handler, "handler");
            return this;
        }

        @Override
        public @NotNull Builder path(final @NotNull String path) {
            this.path = requireNonNull(path, "path");
            return this;
        }

        @Override
        public @NotNull ResourcePackServer build() throws IOException {
            final HttpServer server = serverFactory.create(address, backlog);
            server.setExecutor(executor);
            return new ResourcePackServerImpl(server, path, handler);
        }
    }

    interface HttpServerFactory {

        HttpServer create(InetSocketAddress address, int backlog) throws IOException;

    }
}
