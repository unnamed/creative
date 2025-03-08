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

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an item condition that checks the item's custom model data component,
 * it has the following behavior:
 * <ul>
 *     <li>Returns a value from the flags list in the minecraft:custom_model_data component, if present</li>
 *     <li>Returns false if the component is not present</li>
 * </ul>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 * @see ItemBooleanProperty
 */
public interface CustomModelDataItemBooleanProperty extends ItemBooleanProperty {
    @ApiStatus.Internal
    int DEFAULT_INDEX = 0;

    /**
     * Returns the index for the entry to use in {@code flags} (minecraft:custom_model_data
     * item component), defaults to 0.
     *
     * @return The index
     */
    int index();
}
