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
package team.unnamed.creative.central.common.export;

import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.export.ResourcePackLocation;
import team.unnamed.creative.central.server.CentralResourcePackServer;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.util.logging.Logger;

public class LocalHostExporter implements ResourcePackExporter {

    private final CentralResourcePackServer server;
    private final Logger logger;

    public LocalHostExporter(CentralResourcePackServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public @Nullable ResourcePackLocation export(ResourcePack resourcePack) {

        if (!server.isOpen()) {
            logger.severe(
                    "The resource-pack server is closed, can't host resource-pack there,"
                    + " you must enable it in the configuration (export.localhost.enabled)"
            );
            return null;
        }

        // build resource pack in memory
        BuiltResourcePack pack = MinecraftResourcePackWriter.minecraft().build(resourcePack);

        server.resourcePack(pack);

        // Example:
        // http://127.0.0.1:7270/f69deb4e77d2c6820b39652f63e6deceb87ba13d.zip
        String url = "http://" + server.address() + ':' + server.port() + '/' + pack.hash() + ".zip";

        logger.info("Resource-pack hosted, available in: " + url);

        return ResourcePackLocation.of(url, pack.hash());
    }

}