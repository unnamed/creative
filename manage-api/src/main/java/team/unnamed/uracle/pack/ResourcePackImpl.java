/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.pack;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class ResourcePackImpl
        implements ResourcePack {

    private final String url;
    private final String hash;
    private final boolean required;
    @Nullable private final String prompt;

    ResourcePackImpl(
            String url,
            String hash,
            boolean required,
            @Nullable String prompt
    ) {
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    @Override
    public ResourcePack withLocation(ResourcePackLocation location) {
        return new ResourcePackImpl(
                location.url(),
                location.hash(),
                required,
                prompt
        );
    }

    @Override
    public ResourcePack withProperties(ResourcePackApplication application) {
        return new ResourcePackImpl(
                url,
                hash,
                application.required(),
                application.prompt()
        );
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String hash() {
        return hash;
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public @Nullable String prompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "ResourcePackImpl{" +
                "url='" + url + '\'' +
                ", hash='" + hash + '\'' +
                ", required=" + required +
                ", prompt='" + prompt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourcePackImpl that = (ResourcePackImpl) o;
        return required == that.required
                && url.equals(that.url)
                && hash.equals(that.hash)
                && Objects.equals(prompt, that.prompt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, hash, required, prompt);
    }

}
