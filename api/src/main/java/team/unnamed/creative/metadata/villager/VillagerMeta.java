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
package team.unnamed.creative.metadata.villager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

/**
 * Metadata applicable to villager textures (from entity/villager
 * and entity/zombie_villager) containing additional effects to
 * apply to the villager hat layer.
 *
 * @sincePackFormat 4
 * @sinceMinecraft 1.14
 * @since 1.0.0
 */
public interface VillagerMeta extends MetadataPart {
    /**
     * Creates a new {@link VillagerMeta} instance
     * from the given values
     *
     * @param hat The hat render mode
     * @return A new instance of {@link VillagerMeta}
     * @sinceMinecraft 1.14
     * @sincePackFormat 4
     * @since 1.3.0
     */
    @Contract("_ -> new")
    static @NotNull VillagerMeta villager(final @NotNull Hat hat) {
        return new VillagerMetaImpl(hat);
    }

    /**
     * Creates a new {@link VillagerMeta} instance
     * from the given values
     *
     * @param hat The hat render mode
     * @return A new instance of {@link VillagerMeta}
     * @sinceMinecraft 1.14
     * @sincePackFormat 4
     * @since 1.0.0
     * @deprecated Use {@link #villager(Hat)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("_ -> new")
    static @NotNull VillagerMeta of(final @NotNull Hat hat) {
        return new VillagerMetaImpl(hat);
    }

    /**
     * Determines how the villager hat should render
     * ("none", "partial", "full")
     *
     * @return The hat render mode
     * @sincePackFormat 4
     * @sinceMinecraft 1.14
     * @since 1.0.0
     */
    @NotNull Hat hat();

    /**
     * Represents the hat render mode for villager textures.
     *
     * @sincePackFormat 4
     * @sinceMinecraft 1.14
     * @since 1.0.0
     */
    enum Hat {
        /**
         * No hat rendering.
         *
         * @sinceMinecraft 1.14
         * @sincePackFormat 4
         * @since 1.0.0
         */
        NONE,

        /**
         * Partial hat rendering.
         *
         * @sinceMinecraft 1.14
         * @sincePackFormat 4
         * @since 1.0.0
         */
        PARTIAL,

        /**
         * Full hat rendering.
         *
         * @sinceMinecraft 1.14
         * @sincePackFormat 4
         * @since 1.0.0
         */
        FULL
    }
}
