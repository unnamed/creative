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
package team.unnamed.creative.server.request;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ResourcePackDownloadRequestImpl implements ResourcePackDownloadRequest {
    private final UUID uuid;
    private final String username;
    private final String clientVersion;
    private final String clientVersionId;
    private final int packFormat;

    ResourcePackDownloadRequestImpl(
            final @NotNull UUID uuid,
            final @NotNull String username,
            final @NotNull String clientVersion,
            final @NotNull String clientVersionId,
            final int packFormat
    ) {
        this.uuid = requireNonNull(uuid, "uuid");
        this.username = requireNonNull(username, "username");
        this.clientVersion = requireNonNull(clientVersion, "clientVersion");
        this.clientVersionId = requireNonNull(clientVersionId, "clientVersionId");
        this.packFormat = packFormat;
    }

    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    @Override
    public @NotNull String username() {
        return username;
    }

    @Override
    public @NotNull String clientVersion() {
        return clientVersion;
    }

    @Override
    public @NotNull String clientVersionId() {
        return clientVersionId;
    }

    @Override
    public int packFormat() {
        return packFormat;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("uuid", uuid),
                ExaminableProperty.of("username", username),
                ExaminableProperty.of("clientVersion", clientVersion),
                ExaminableProperty.of("clientVersionId", clientVersionId),
                ExaminableProperty.of("packFormat", packFormat)
        );
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ResourcePackDownloadRequestImpl that = (ResourcePackDownloadRequestImpl) o;
        return packFormat == that.packFormat
                && uuid.equals(that.uuid)
                && username.equals(that.username)
                && clientVersion.equals(that.clientVersion)
                && clientVersionId.equals(that.clientVersionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, clientVersion, clientVersionId, packFormat);
    }
}
