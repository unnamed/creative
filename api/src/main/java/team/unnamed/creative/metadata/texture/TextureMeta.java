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
package team.unnamed.creative.metadata.texture;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

/**
 * Represents meta-data applicable to textures
 *
 * @sinceMinecraft 1.6.1
 * @sincePackFormat 1
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface TextureMeta extends MetadataPart {
    /**
     * Creates a new {@link TextureMeta} instance to
     * be applied to a texture
     *
     * @param blur  To make the texture blur
     * @param clamp To stretch the texture
     * @return A new texture metadata instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.3.0
     */
    @Contract("_, _ -> new")
    static @NotNull TextureMeta texture(final boolean blur, final boolean clamp) {
        return new TextureMetaImpl(blur, clamp);
    }

    /**
     * Creates a new {@link TextureMeta} instance to
     * be applied to a texture
     *
     * @param blur  To make the texture blur
     * @param clamp To stretch the texture
     * @return A new texture metadata instance
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     * @deprecated Use {@link #texture(boolean, boolean)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("_, _ -> new")
    static @NotNull TextureMeta of(final boolean blur, final boolean clamp) {
        return new TextureMetaImpl(blur, clamp);
    }

    boolean DEFAULT_BLUR = false;
    boolean DEFAULT_CLAMP = false;

    /**
     * Determines whether the texture will be
     * blur-ed when viewed from close up
     *
     * @return True to blur texture
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    boolean blur();

    /**
     * Determines whether the texture should be stretched
     * instead of tiled in cases where it otherwise would,
     * such as on the shadow
     *
     * @return True to clamp
     * @sincePackFormat 1
     * @sinceMinecraft 1.6.1
     * @since 1.0.0
     */
    boolean clamp();
}
