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
package team.unnamed.creative.item.property;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item condition that does not have any field.
 *
 * <p>Might be "minecraft:broken", "minecraft:bundle/has_selected_item",
 * "minecraft:carried", "minecraft:damaged", "minecraft:extended_view",
 * "minecraft:fishing_rod/cast", "minecraft:selected", "minecraft:using_item",
 * "minecraft:view_entity", and others added in the future. See more in the
 * {@link ItemBooleanProperty} static factory methods</p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 * @see ItemBooleanProperty
 */
@ApiStatus.Experimental
public interface NoFieldItemBooleanProperty extends ItemBooleanProperty, Keyed {
    /**
     * Returns the identifier for this item condition.
     *
     * @return The identifier
     */
    @Override
    @NotNull Key key();
}
