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
package team.unnamed.creative.equipment;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines how an equipment layer behaves when dyed.
 *
 * <p>If specified for a layer, the layer will be tinted by the
 * color contained in the {@code dyed_color} component in the
 * item.</p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.2
 * @see EquipmentLayer
 */
public interface EquipmentLayerDye extends Examinable {
    /**
     * Returns the (optional) RGB color used to tint the
     * item, if the item is not dyed.
     *
     * <p>If it's null and the item is not dyed, the layer
     * is hidden.</p>
     *
     * @return The RGB color used to tint the item
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Nullable Integer colorWhenUndyed();

    /**
     * Returns a {@link EquipmentLayerDye} instance with no
     * color when undyed, this means, the layer will be hidden
     * if the item is not dyed.
     *
     * @return An empty {@link EquipmentLayerDye} instance
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    static @NotNull EquipmentLayerDye hiddenIfNotDyed() {
        return EquipmentLayerDyeImpl.EMPTY;
    }

    /**
     * Returns a {@link EquipmentLayerDye} instance with the
     * specified color when undyed, this means the item will
     * be tinted with the specified color if it's not dyed.
     *
     * @param colorWhenUndyed The RGB color used to tint the item
     * @return A {@link EquipmentLayerDye} instance with the specified color
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull EquipmentLayerDye colorWhenUndyed(final int colorWhenUndyed) {
        // yeah this method is essentially the same as the next one, but we make
        // stuff safe, clear and self-explanatory :)
        return new EquipmentLayerDyeImpl(colorWhenUndyed);
    }

    /**
     * Returns a {@link EquipmentLayerDye} instance with the
     * specified color when undyed, this means the item will
     * be tinted with the specified color if it's not dyed.
     *
     * <p>If {@code colorWhenUndyed} is null, the layer is hidden
     * if the item is not dyed.</p>
     *
     * @param colorWhenUndyed The RGB color used to tint the item
     * @return A {@link EquipmentLayerDye} instance with the specified color
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull EquipmentLayerDye equipmentLayerDye(final @Nullable Integer colorWhenUndyed) {
        return new EquipmentLayerDyeImpl(colorWhenUndyed);
    }
}
