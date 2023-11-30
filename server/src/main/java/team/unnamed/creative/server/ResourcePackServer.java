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

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.server.handler.ResourcePackRequestHandler;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * An HTTP(s) server wrapper specialized in serving
 * creative resource packs.
 *
 * @since 1.0.0
 */
public interface ResourcePackServer {
    /**
     * Creates a new builder instance for {@link ResourcePackServer}.
     *
     * @return A new builder instance
     * @since 1.2.0
     */
    static @NotNull Builder server() {
        return new ResourcePackServerImpl.BuilderImpl();
    }

    /**
     * Creates a new builder instance for {@link ResourcePackServer}.
     *
     * @return A new builder instance
     * @since 1.0.0
     * @deprecated Use {@link #server()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("-> new")
    static @NotNull Builder builder() {
        return server();
    }

    /**
     * Gets the internal {@link HttpServer} instance.
     *
     * @return The internal {@link HttpServer} instance
     * @since 1.0.0
     * @deprecated Should not be exposed by this interface
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @NotNull HttpServer httpServer();

    /**
     * Gets the server's bound {@link InetSocketAddress address}.
     *
     * @return The server's bound {@link InetSocketAddress address}
     * @since 1.2.0
     */
    @NotNull InetSocketAddress address();

    /**
     * Starts the internal {@link HttpServer}, it is started in a new
     * background thread, so this operation is not blocking
     *
     * @since 1.0.0
     */
    void start();

    /**
     * Stops the internal {@link HttpServer}
     *
     * @param delay the maximum time in seconds to wait until requests have finished
     * @see HttpServer#stop(int)
     * @since 1.0.0
     */
    void stop(final int delay);

    /**
     * A builder for {@link ResourcePackServer} instances
     *
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets the server's bound address, required.
         *
         * @param address The server's bound address
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder address(final @NotNull InetSocketAddress address);

        /**
         * Sets the server's bound address, required.
         *
         * @param hostname The server's bound hostname
         * @param port     The server's bound port
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_, _ -> this")
        default @NotNull Builder address(final @NotNull String hostname, final int port) {
            requireNonNull(hostname, "hostname");
            return address(new InetSocketAddress(hostname, port));
        }

        /**
         * Sets the server's bound address, required.
         *
         * @param address The server's bound address
         * @param port    The server's bound port
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_, _ -> this")
        default @NotNull Builder address(final @NotNull InetAddress address, final int port) {
            requireNonNull(address, "address");
            return address(new InetSocketAddress(address, port));
        }

        /**
         * Sets the server's bound address, required.
         *
         * @param port The server's bound port
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder address(final int port) {
            return address(new InetSocketAddress(port));
        }

        /**
         * Sets the server's executor, optional.
         *
         * <p>All requests are handled in tasks given to the executor.
         * if the executor is not specified or if it's set to null, then
         * a default implementation is used, which uses the thread
         * which was created by the {@link ResourcePackServer#start()}
         * method.</p>
         *
         * @param executor The server's executor
         * @return This builder
         * @since 1.5.0
         */
        @Contract("_ -> this")
        @NotNull Builder executor(final @Nullable Executor executor);

        /**
         * Sets the server's HTTPS configurator, optional.
         * If not set, the server will default to a HTTP
         * implementation.
         *
         * @param httpsConfigurator The server's HTTPS configurator
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder secure(final @NotNull HttpsConfigurator httpsConfigurator);

        /**
         * Sets the server's HTTPS configurator, optional.
         * If not set, the server will default to a HTTP
         * implementation.
         *
         * @param sslContext   The server's SSL context
         * @param configurator The server's HTTPS configurator
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_, _ -> this")
        default @NotNull Builder secure(final @NotNull SSLContext sslContext, final @NotNull Consumer<@NotNull HttpsParameters> configurator) {
            requireNonNull(sslContext, "sslContext");
            requireNonNull(configurator, "configurator");
            return this.secure(new HttpsConfigurator(sslContext) {
                @Override
                public void configure(final @NotNull HttpsParameters params) {
                    configurator.accept(params);
                }

                @Override
                public @NotNull String toString() {
                    return "ResourcePackServer/HttpsConfigurator for " + configurator;
                }
            });
        }

        /**
         * Sets the server's HTTPS SSL context, optional.
         * If not set, the server will default to a HTTP
         * implementation.
         *
         * @param sslContext The server's SSL context
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder secure(final @NotNull SSLContext sslContext) {
            requireNonNull(sslContext, "sslContext");
            return secure(new HttpsConfigurator(sslContext));
        }

        /**
         * Sets the socket backlog, specifies the number
         * of pending connections the connection queue will
         * hold
         *
         * <p>If the given value is less than or equal to
         * zero, then a system default value is used</p>
         *
         * @param backlog The socket backlog
         * @return This builder, for chaining
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder backlog(final int backlog);

        /**
         * Sets the server's request handler, required,
         * may also be set by using {@link #pack}.
         *
         * @param handler The server's request handler
         * @return This builder
         * @since 1.2.0
         */
        @NotNull Builder handler(final @NotNull ResourcePackRequestHandler handler);

        /**
         * Sets the server's request handler, required,
         * may also be set by using {@link #pack}.
         *
         * @param handler The server's request handler
         * @return This builder
         * @since 1.0.0
         * @deprecated Use {@link #handler(ResourcePackRequestHandler)} instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        @Contract("_ -> this")
        default @NotNull Builder handler(final @NotNull team.unnamed.creative.server.ResourcePackRequestHandler handler) {
            // cast to new ResourcePackRequestHandler
            return handler((ResourcePackRequestHandler) requireNonNull(handler, "handler"));
        }

        /**
         * Sets a request handler that will always return the given
         * {@link BuiltResourcePack pack}.
         *
         * @param pack      The served resource pack
         * @param validOnly Whether to only serve the pack if the
         *                  request was made from a Minecraft client
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_, _ -> this")
        default @NotNull Builder pack(final @NotNull BuiltResourcePack pack, final boolean validOnly) {
            requireNonNull(pack, "pack");
            return handler(ResourcePackRequestHandler.fixed(pack, validOnly));
        }

        /**
         * Sets a request handler that will always return the given
         * {@link BuiltResourcePack pack}.
         *
         * @param pack The served resource pack
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder pack(final @NotNull BuiltResourcePack pack) {
            requireNonNull(pack, "pack");
            return handler(ResourcePackRequestHandler.fixed(pack));
        }

        /**
         * Sets the base path for the set handler, optional,
         * defaults to "/".
         *
         * @param path The base path for the set handler
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder path(final @NotNull String path);

        /**
         * Builds the {@link ResourcePackServer} instance.
         *
         * <p>Note that this method will NOT automatically start
         * the server, you must use the {@link ResourcePackServer#start()}
         * method to do that.</p>
         *
         * @return The built {@link ResourcePackServer} instance
         * @throws IOException If bind fails
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull ResourcePackServer build() throws IOException;
    }
}
