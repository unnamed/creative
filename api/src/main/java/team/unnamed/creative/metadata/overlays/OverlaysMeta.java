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
package team.unnamed.creative.metadata.overlays;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the "overlays" metadata part. It declares
 * all the available overlays for this resource-pack.
 *
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 * @since 1.1.0
 */
@ApiStatus.NonExtendable
public interface OverlaysMeta extends MetadataPart {

    /**
     * Gets the ordered list of declared overlays.
     *
     * @return The overlays.
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.1.0
     */
    @NotNull List<OverlayEntry> entries();

    /**
     * Creates a new {@link OverlaysMeta} instance with the
     * given overlays.
     *
     * @param overlays The overlays.
     * @return The overlays meta.
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.1.0
     */
    static @NotNull OverlaysMeta of(final @NotNull List<OverlayEntry> overlays) {
        return new OverlaysMetaImpl(overlays);
    }

    /**
     * Creates a new {@link OverlaysMeta} instance with the
     * given overlays.
     *
     * @param overlays The overlays.
     * @return The overlays meta.
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.1.0
     */
    static @NotNull OverlaysMeta of(final @NotNull OverlayEntry @NotNull ... overlays) {
        return of(Arrays.asList(overlays));
    }

}
