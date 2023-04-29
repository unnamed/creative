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
package team.unnamed.creative.central.request;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Represents a resource-pack download request, sent to the
 * Minecraft players by the server
 *
 * @since 1.0.0
 */
public final class ResourcePackRequest {

    private final String url;
    private final String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    private ResourcePackRequest(
            String url,
            String hash,
            boolean required,
            @Nullable Component prompt
    ) {
        requireNonNull(url, "url");
        requireNonNull(hash, "hash");
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    /**
     * Returns the URL for the resource-pack being
     * sent
     *
     * @return The resource-pack URL
     */
    public String url() {
        return url;
    }

    /**
     * Returns the SHA-1 hash of the resource-pack,
     * may be empty
     *
     * @return The SHA-1 hash of the resource-pack
     */
    public String hash() {
        return hash;
    }

    /**
     * Returns true if the use of the resource-pack
     * is required on the server, false otherwise
     *
     * @return If the resource-pack is required
     */
    public boolean required() {
        return required;
    }

    /**
     * Returns the prompt message, shown to the end-user
     * in the confirmation screen when they are asked to
     * download the resource-pack
     *
     * @return The prompt message
     */
    public @Nullable Component prompt() {
        return prompt;
    }

    public static ResourcePackRequest of(
            String url,
            String hash,
            boolean required,
            @Nullable Component prompt
    ) {
        return new ResourcePackRequest(url, hash, required, prompt);
    }

    public static ResourcePackRequest of(String url, String hash, boolean required) {
        return of(url, hash, required, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String url;
        private String hash;
        private boolean required;
        private @Nullable Component prompt;

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder prompt(@Nullable Component prompt) {
            this.prompt = prompt;
            return this;
        }

        public ResourcePackRequest build() {
            return new ResourcePackRequest(url, hash, required, prompt);
        }


    }

}
