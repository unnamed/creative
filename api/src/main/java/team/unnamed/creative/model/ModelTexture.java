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
package team.unnamed.creative.model;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ModelTexture {

    // only one is null
    private final @Nullable Key key;
    private final @Nullable String reference;

    private ModelTexture(
            @Nullable Key key,
            @Nullable String reference
    ) {
        this.key = key;
        this.reference = reference;
    }

    public @Nullable Key key() {
        return key;
    }

    public @Nullable String reference() {
        return reference;
    }

    public Object get() {
        return key == null ? reference : key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelTexture that = (ModelTexture) o;

        if (!Objects.equals(key, that.key)) return false;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        return result;
    }

    public static ModelTexture ofKey(Key key) {
        requireNonNull(key, "key");
        return new ModelTexture(key, null);
    }

    public static ModelTexture ofReference(String reference) {
        requireNonNull(reference, "reference");
        return new ModelTexture(null, reference);
    }

}
