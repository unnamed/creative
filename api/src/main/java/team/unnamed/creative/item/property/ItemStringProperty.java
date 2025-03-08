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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an item string property, check implementations for more information.
 *
 * <p>Item string properties are used in {@link team.unnamed.creative.item.SelectItemModel}
 * in order to render an item model based on the item or context state.</p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface ItemStringProperty extends ItemProperty {
    @Override
    default boolean isString() {
        return true;
    }

    /**
     * Returns the <strong>block state</strong> item string property, which evaluates to
     * some property from the {@code minecraft:block_state} component
     *
     * <p>Possible values are any string.</p>
     *
     * @param property The string key to select from the component
     * @return The block state item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull BlockStateItemStringProperty blockState(final @NotNull String property) {
        return new BlockStateItemStringPropertyImpl(property);
    }

    /**
     * Returns the <strong>charge type</strong> item string property, which evaluates to the
     * charge type stored in the {@code minecraft:charged_projectiles} component, if present.
     *
     * <p>Possible values are: <ul>
     *     <li>{@code none} - If there are no projectiles, or the component is not present</li>
     *     <li>{@code rocket} - If there is at least one firework rocket</li>
     *     <li>{@code arrow} - In any other case</li>
     * </ul></p>
     *
     * @return The charge type item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty chargeType() {
        return NoFieldItemStringPropertyImpl.CHARGE_TYPE;
    }

    /**
     * Returns the <strong>context dimension</strong> item string property, which evaluates to the
     * ID of the dimension in the context, if any.
     *
     * <p>Possible values are namespaced dimension IDs like {@code minecraft:overworld}</p>
     *
     * @return The context dimension item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty contextDimension() {
        return NoFieldItemStringPropertyImpl.CONTEXT_DIMENSION;
    }

    /**
     * Returns the <strong>context entity type</strong> item string property, which evaluates to the
     * type of entity present in the context, if any.
     *
     * <p>Possible values are namespaced entity type IDs</p>
     *
     * @return The context entity type item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty contextEntityType() {
        return NoFieldItemStringPropertyImpl.CONTEXT_ENTITY_TYPE;
    }

    /**
     * Returns the <strong>custom model data</strong> item string property, which evaluates to
     * a value from {@code strings} list in the {@code minecraft:custom_model_data} component.
     *
     * <p>Possible values are any string</p>
     *
     * @param index The index of the custom model data entry to use in strings
     * @return The custom model data item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CustomModelDataItemStringProperty customModelData(final int index) {
        return new CustomModelDataItemStringPropertyImpl(index);
    }

    /**
     * Returns the <strong>custom model data</strong> item string property, which evaluates to
     * a value from {@code strings} list in the {@code minecraft:custom_model_data} component.
     *
     * <p>Possible values are any string</p>
     *
     * @return The custom model data item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CustomModelDataItemStringProperty customModelData() {
        return customModelData(CustomModelDataItemBooleanProperty.DEFAULT_INDEX);
    }

    /**
     * Returns the <strong>display context</strong> item string property, which evaluates to the
     * context this item is being rendered in.
     *
     * <p>Possible values are: <ul>
     *     <li>{@code none}</li>
     *     <li>{@code thirdperson_lefthand}</li>
     *     <li>{@code thirdperson_righthand}</li>
     *     <li>{@code firstperson_lefthand}</li>
     *     <li>{@code firstperson_righthand}</li>
     *     <li>{@code head}</li>
     *     <li>{@code gui}</li>
     *     <li>{@code ground}</li>
     *     <li>{@code fixed}</li>
     * </ul></p>
     *
     * @return The display context item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty displayContext() {
        return NoFieldItemStringPropertyImpl.DISPLAY_CONTEXT;
    }

    /**
     * Returns the <strong>local time</strong> item string property, which evaluates to the
     * current time, formatted according to the given locale, timezone and pattern. The value
     * is updated every second.
     *
     * <p>Possible values are any string.</p>
     *
     * @param locale The locale to use, e.g. {@code en_US}, {@code cs_AU@numbers=thai;calendar=japanese}
     * @param timezone The timezone to use, or {@code null} to use the client's computer, e.g. {@code Europe/Stockholm},
     *                 {@code GMT+0:45}
     * @param format The pattern to use, e.g. {@code yyyy-MM-dd}, {@code HH:mm:ss}
     * @return The local time item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see LocalTimeItemStringProperty
     */
    static @NotNull LocalTimeItemStringProperty localTime(final @NotNull String locale, final @Nullable String timezone, final @NotNull String format) {
        return new LocalTimeItemStringPropertyImpl(locale, timezone, format);
    }

    /**
     * Returns the <strong>local time</strong> item string property, which evaluates to the
     * current time, formatted according to the given pattern. The value is updated every second.
     *
     * <p>Possible values are any string.</p>
     *
     * @param format The pattern to use, e.g. {@code yyyy-MM-dd}, {@code HH:mm:ss}
     * @return The local time item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see LocalTimeItemStringProperty
     */
    static @NotNull LocalTimeItemStringProperty localTime(final @NotNull String format) {
        return localTime(LocalTimeItemStringProperty.DEFAULT_LOCALE, null, format);
    }

    /**
     * Returns the <strong>local time</strong> item string property, which evaluates to the
     * current time, formatted according to the given locale and pattern. The value
     * is updated every second.
     *
     * <p>Possible values are any string.</p>
     *
     * @param locale The locale to use, e.g. {@code en_US}, {@code cs_AU@numbers=thai;calendar=japanese}
     * @param format The pattern to use, e.g. {@code yyyy-MM-dd}, {@code HH:mm:ss}
     * @return The local time item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see LocalTimeItemStringProperty
     */
    static @NotNull LocalTimeItemStringProperty localTime(final @NotNull String locale, final @NotNull String format) {
        return localTime(locale, null, format);
    }

    /**
     * Returns the <strong>main hand</strong> item string property, which evaluates to the
     * main hand of the player holding the item.
     *
     * <p>Possible values are: <ul>
     *     <li>{@code left}</li>
     *     <li>{@code right}</li>
     * </ul></p>
     *
     * @return The main hand item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty mainHand() {
        return NoFieldItemStringPropertyImpl.MAIN_HAND;
    }

    /**
     * Returns the <strong>trim material</strong> item string property, which evaluates to the
     * value of the material field from the {@code minecraft:trim} component, if present.
     *
     * <p>Possible values are namespaced IDs</p>
     *
     * @return The trim material item string property
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldItemStringProperty trimMaterial() {
        return NoFieldItemStringPropertyImpl.TRIM_MATERIAL;
    }
}
