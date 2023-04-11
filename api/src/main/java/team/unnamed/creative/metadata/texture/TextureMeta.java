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
package team.unnamed.creative.metadata.texture;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents meta-data applicable to textures
 *
 * @since 1.0.0
 */
public class TextureMeta implements MetadataPart {

    public static final boolean DEFAULT_BLUR = false;
    public static final boolean DEFAULT_CLAMP = false;

    private final boolean blur;
    private final boolean clamp;

    private TextureMeta(
            boolean blur,
            boolean clamp
    ) {
        this.blur = blur;
        this.clamp = clamp;
    }

    /**
     * Determines whether the texture will be
     * blur-ed when viewed from close up
     *
     * @return True to blur texture
     */
    public boolean blur() {
        return blur;
    }

    /**
     * Determines whether the texture should be stretched
     * instead of tiled in cases where it otherwise would,
     * such as on the shadow
     *
     * @return True to clamp
     */
    public boolean clamp() {
        return clamp;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("blur", blur),
                ExaminableProperty.of("clamp", clamp)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureMeta that = (TextureMeta) o;
        return blur == that.blur
                && clamp == that.clamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blur, clamp);
    }

    /**
     * Creates a new {@link TextureMeta} instance to
     * be applied to a texture
     *
     * @param blur To make the texture blur
     * @param clamp To stretch the texture
     * @return A new texture metadata instance
     */
    public static TextureMeta of(
            boolean blur,
            boolean clamp
    ) {
        return new TextureMeta(blur, clamp);
    }

}
