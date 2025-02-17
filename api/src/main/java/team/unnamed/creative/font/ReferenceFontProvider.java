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
package team.unnamed.creative.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Type of {@link FontProvider} that references another font.
 *
 * <p>It's used to include common font providers in another
 * font.</p>
 *
 * @sinceMinecraft 1.20
 * @sincePackFormat 15
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ReferenceFontProvider extends FontProvider {
    /**
     * Creates a new {@link ReferenceFontProvider} with the
     * given {@code id}.
     *
     * @param id The referenced font key
     * @return A new {@link ReferenceFontProvider}
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     * @deprecated Use {@link FontProvider#reference(Key)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull ReferenceFontProvider of(final @NotNull Key id) {
        return FontProvider.reference(id);
    }

    /**
     * Returns the referenced font key.
     *
     * @return The referenced font key
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.0.0
     */
    @NotNull Key id();
}
