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
package team.unnamed.creative.item.tint;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.item.ReferenceItemModel;

/**
 * Represents a source of tint for a block item model.
 *
 * <p>Tint sources are always expected to return an RGB color.</p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 * @see ReferenceItemModel
 */
public interface TintSource extends Examinable {
    /**
     * Returns a tint source that always returns the same color.
     * (constant RGB color)
     *
     * @param tint The RGB constant color
     * @return A constant tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ConstantTintSource constant(final int tint) {
        return new ConstantTintSourceImpl(tint);
    }

    /**
     * Returns a tint source that uses the {@code color} value from the
     * {@code minecraft:custom_model_data} component, or the default tint if
     * not present.
     *
     * @param index The custom model data index to use
     * @param defaultTint The default tint to use if the component is not present
     * @return A custom model data tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CustomModelDataTintSource customModelData(final int index, final int defaultTint) {
        return new CustomModelDataTintSourceImpl(index, defaultTint);
    }

    /**
     * Returns a tint source that uses the value from the {@code minecraft:dyed_color}
     * component, or the default tint if the component is not present.
     *
     * @param defaultTint The default tint to use if the component is not present
     * @return A dye tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeyedAndBackedTintSource dye(final int defaultTint) {
        return new KeyedAndBackedTintSourceImpl(KeyedAndBackedTintSourceImpl.DYE, defaultTint);
    }

    /**
     * Returns a tint source that returns a grass color at specific climate parameters,
     * based on {@code textures/colormap/grass.png} from the resource-pack. This yields the
     * same colors as ones selected by downfall and temperature in biome configuration.
     *
     * @param temperature The temperature value
     * @param downfall The downfall value
     * @return A grass tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull GrassTintSource grass(final float temperature, final float downfall) {
        return new GrassTintSourceImpl(temperature, downfall);
    }

    /**
     * Returns a tint source that uses the average colors from the {@code minecraft:firework_explosion}
     * component, or the default tint if the component is not present.
     *
     * @param defaultTint The default tint to use if the component is not present
     * @return A firework tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeyedAndBackedTintSource firework(final int defaultTint) {
        return new KeyedAndBackedTintSourceImpl(KeyedAndBackedTintSourceImpl.FIREWORK, defaultTint);
    }

    /**
     * Returns a tint source that uses the value from the {@code minecraft:map_color}
     * component, or the default tint if the component is not present.
     *
     * @param defaultTint The default tint to use if the component is not present
     * @return A map color tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeyedAndBackedTintSource mapColor(final int defaultTint) {
        return new KeyedAndBackedTintSourceImpl(KeyedAndBackedTintSourceImpl.MAP_COLOR, defaultTint);
    }

    /**
     * Returns a tint source that uses a color based on the {@code minecraft:potion_contents}
     * component, if the component is present:
     * <ul>
     *     <li>Custom color, if there is one in the component</li>
     *     <li>Average of effect colors, if at least one is present</li>
     * </ul>
     *
     * <p>The default tint is used if the component is not present.</p>
     *
     * @param defaultTint The default tint to use if the component is not present
     * @return A potion tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeyedAndBackedTintSource potion(final int defaultTint) {
        return new KeyedAndBackedTintSourceImpl(KeyedAndBackedTintSourceImpl.POTION, defaultTint);
    }

    /**
     * Returns a tint source that uses the team color of the context entity, if any.
     *
     * @param defaultTint The default tint to use if there is no context entity, entity
     *                    is not in a team or the team has no color
     * @return A team tint source
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeyedAndBackedTintSource team(final int defaultTint) {
        return new KeyedAndBackedTintSourceImpl(KeyedAndBackedTintSourceImpl.TEAM, defaultTint);
    }
}
