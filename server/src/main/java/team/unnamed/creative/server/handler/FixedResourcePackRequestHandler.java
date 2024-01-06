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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.server.request.ResourcePackDownloadRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

final class FixedResourcePackRequestHandler implements ResourcePackRequestHandler {
    private final BuiltResourcePack pack;
    private final boolean validOnly;

    FixedResourcePackRequestHandler(final @NotNull BuiltResourcePack pack, final boolean validOnly) {
        this.pack = Objects.requireNonNull(pack, "pack");
        this.validOnly = validOnly;
    }

    @Override
    public void onRequest(final @Nullable ResourcePackDownloadRequest request, final @NotNull HttpExchange exchange) throws IOException {
        if (request == null && validOnly) {
            final byte[] data = "Please use a Minecraft client\n".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(400, data.length);
            try (final OutputStream responseStream = exchange.getResponseBody()) {
                responseStream.write(data);
            }
            return;
        }

        final byte[] data = pack.data().toByteArray();
        exchange.getResponseHeaders().set("Content-Type", "application/zip");
        exchange.sendResponseHeaders(200, data.length);
        try (final OutputStream responseStream = exchange.getResponseBody()) {
            responseStream.write(data);
        }
    }

    @Override
    public @NotNull String toString() {
        return "FixedResourcePackRequestHandler{" +
                "pack=" + pack +
                '}';
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FixedResourcePackRequestHandler that = (FixedResourcePackRequestHandler) o;
        return pack.equals(that.pack);
    }

    @Override
    public int hashCode() {
        return pack.hashCode();
    }
}
