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
package team.unnamed.creative.server.request;

import com.sun.net.httpserver.Headers;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@ApiStatus.Internal
public final class ResourcePackDownloadRequestParser {

    private ResourcePackDownloadRequestParser() {
        throw new UnsupportedOperationException("Can't instantiate utility class");
    }

    public static @Nullable ResourcePackDownloadRequest parse(final @NotNull Headers headers) {
        final String username = headers.getFirst("X-Minecraft-Username");
        final String rawUuid = headers.getFirst("X-Minecraft-UUID");
        final String clientVersion = headers.getFirst("X-Minecraft-Version");
        final String clientVersionId = headers.getFirst("X-Minecraft-Version-ID");
        final String rawPackFormat = headers.getFirst("X-Minecraft-Pack-Format");

        // any null = invalid!
        if (username == null || rawUuid == null || clientVersion == null || clientVersionId == null || rawPackFormat == null) {
            return null;
        }

        // parse input data
        final UUID uuid;
        final int packFormat;

        try {
            uuid = UUIDUtil.fromUndashedString(rawUuid);
            packFormat = Integer.parseInt(rawPackFormat);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }

        return ResourcePackDownloadRequest.request(uuid, username, clientVersion, clientVersionId, packFormat);
    }

}
