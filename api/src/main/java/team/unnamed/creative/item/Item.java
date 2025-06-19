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
package team.unnamed.creative.item;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

/**
 * Represents an item model, information needed to render an item
 *
 * <p>Added in <a href="https://feedback.minecraft.net/hc/en-us/articles/32385811139085-Minecraft-Java-Edition-1-21-4-The-Garden-Awakens">Minecraft: Java Edition 1.21.4 - The Garden Awakens</a></p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface Item extends Keyed, ResourcePackPart, Examinable {
    @ApiStatus.Internal
    boolean DEFAULT_HAND_ANIMATION_ON_SWAP = true;

    @ApiStatus.Internal
    boolean DEFAULT_OVERSIZED_IN_GUI = true;

    /**
     * Describes if down-and-up animation should be played in first-person view
     * when item stack is changed (either type, count or components).
     *
     * <p>Only the value from the new item is taken into account</p>
     *
     * <p>Does not control "pop" animation in GUI when item is picked up or changes
     * count</p>
     *
     * @return If hand animation should be played
     */
    boolean handAnimationOnSwap();

    /**
     * Determines whether the item should be rendered oversized or clipped in the inventory.
     *
     * @return If clipping should not be applied
     */
    boolean oversizedInGui();

    @NotNull ItemModel model();

    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.item(this);
    }

    /**
     * Creates a new {@link Item} instance with the given key, model and hand animation on swap.
     *
     * @param key The item key
     * @param model The item model
     * @param handAnimationOnSwap If hand animation should be played
     * @param oversizedInGui If item clipping should not be applied
     * @return The item
     * @since 1.8.4
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull Item item(final @NotNull Key key, final @NotNull ItemModel model, final boolean handAnimationOnSwap, final boolean oversizedInGui) {
        return new ItemImpl(key, model, handAnimationOnSwap, oversizedInGui);
    }

    /**
     * Creates a new {@link Item} instance with the given key, model and hand animation on swap.
     *
     * @param key The item key
     * @param model The item model
     * @param handAnimationOnSwap If hand animation should be played
     * @return The item
     * @since 1.8.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull Item item(final @NotNull Key key, final @NotNull ItemModel model, final boolean handAnimationOnSwap) {
        return new ItemImpl(key, model, handAnimationOnSwap, DEFAULT_OVERSIZED_IN_GUI);
    }

    /**
     * Creates a new {@link Item} instance with the given key and model.
     *
     * @param key The item key
     * @param model The item model
     * @return The item
     * @since 1.8.0
     */
    static @NotNull Item item(final @NotNull Key key, final @NotNull ItemModel model) {
        return item(key, model, DEFAULT_HAND_ANIMATION_ON_SWAP);
    }
}
