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
package team.unnamed.creative.texture;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.Vector2Float;

/**
 * Represents a texture UV mapping.
 *
 * <p>The UV maps a full texture or texture region
 * to a certain 3D surface.</p>
 *
 * <p>Note that the coordinates specified in this texture
 * are all relative and (ideally) in the range of zero to
 * one.</p>
 *
 * <p>If any of the from and to components are swapped,
 * the texture flips.</p>
 *
 * @since 1.1.0
 */
public interface TextureUV extends Examinable {
    /**
     * Creates a new {@link TextureUV} instance with the
     * given start and end points.
     *
     * @param from The UV region start point
     * @param to   The UV region end point
     * @return The created {@link TextureUV} instance
     * @since 1.1.0
     */
    @Contract("_, _ -> new")
    static @NotNull TextureUV uv(final @NotNull Vector2Float from, final @NotNull Vector2Float to) {
        return new TextureUVImpl(from, to);
    }

    /**
     * Creates a new {@link TextureUV} instance with the
     * given start and end points.
     *
     * @param fromX The UV region start point X
     * @param fromY The UV region start point Y
     * @param toX   The UV region end point X
     * @param toY   The UV region end point Y
     * @return The created {@link TextureUV} instance
     * @since 1.1.0
     */
    @Contract("_, _, _, _ -> new")
    static @NotNull TextureUV uv(final float fromX, final float fromY, final float toX, final float toY) {
        return uv(new Vector2Float(fromX, fromY), new Vector2Float(toX, toY));
    }

    /**
     * Returns the UV region start point.
     *
     * @return The UV region start point
     * @since 1.1.0
     */
    @NotNull Vector2Float from();

    /**
     * Returns the UV region end point.
     *
     * @return The UV region end point
     * @since 1.1.0
     */
    @NotNull Vector2Float to();
}
