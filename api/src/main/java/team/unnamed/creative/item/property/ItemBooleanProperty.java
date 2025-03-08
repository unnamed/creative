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

/**
 * Represents an item condition which can be used in conditional
 * item models ({@link team.unnamed.creative.item.ConditionItemModel}) in order
 * to use different item models depending on specific item properties.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 * @see team.unnamed.creative.item.ConditionItemModel
 */
public interface ItemBooleanProperty extends ItemProperty {
    @Override
    default boolean isBoolean() {
        return true;
    }

    /**
     * Returns an item condition that checks whether the item is
     * damageable and has only one use remaining before breaking.
     *
     * @return The broken item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty broken() {
        return NoFieldItemBooleanPropertyImpl.BROKEN;
    }

    /**
     * Returns an item condition that checks whether Bundle is "open",
     * i.e. it has selected item visible in the GUI.
     *
     * @return The bundle has selected item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty bundleHasSelectedItem() {
        return NoFieldItemBooleanPropertyImpl.BUNDLE_HAS_SELECTED_ITEM;
    }

    /**
     * Returns an item condition that checks whether the item is being carried
     * between slots in GUI.
     *
     * @return The carried item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty carried() {
        return NoFieldItemBooleanPropertyImpl.CARRIED;
    }

    /**
     * Returns an item condition that checks whether the item is damageable and
     * has been used at least once
     *
     * @return The damaged item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty damaged() {
        return NoFieldItemBooleanPropertyImpl.DAMAGED;
    }

    /**
     * Returns an item condition that checks if the player has requested extended details
     * by holding down the shift key (Note: not a keybind, can't be rebound)
     *
     * <p>Only works when item is displayed in UI</p>
     *
     * @return The extended view item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty extendedView() {
        return NoFieldItemBooleanPropertyImpl.EXTENDED_VIEW;
    }

    /**
     * Returns an item condition that checks whether there is a Fishing Bobber attached to
     * currently used Fishing Rod.
     *
     * @return The fishing rod cast item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty fishingRodCast() {
        return NoFieldItemBooleanPropertyImpl.FISHING_ROD_CAST;
    }

    /**
     * Returns an item condition that checks whether the item is selected in a hotbar.
     *
     * @return The selected item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty selected() {
        return NoFieldItemBooleanPropertyImpl.SELECTED;
    }

    /**
     * Returns an item condition that checks whether the player is using the item.
     *
     * @return The using item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty usingItem() {
        return NoFieldItemBooleanPropertyImpl.USING_ITEM;
    }

    /**
     * Returns an item condition with the following behavior:
     * <ul>
     *     <li>When not spectating, returns true if the context entity is the local Player entity, i.e. one controlled by client</li>
     *     <li>When spectating, returns true if the context entity is the spectated entity</li>
     *     <li>If no context entity is present, will return false</li>
     * </ul>
     *
     * @return The view entity item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ItemBooleanProperty viewEntity() {
        return NoFieldItemBooleanPropertyImpl.VIEW_ENTITY;
    }

    /**
     * Returns an item condition that checks the item's custom model data component,
     * it has the following behavior:
     * <ul>
     *     <li>Returns a value from the flags list in the minecraft:custom_model_data component, if present</li>
     *     <li>Returns false if the component is not present</li>
     * </ul>
     *
     * @param index The index for the entry to use in {@code flags} (minecraft:custom_model_data item component)
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CustomModelDataItemBooleanProperty customModelData(final int index) {
        return new CustomModelDataItemBooleanPropertyImpl(index);
    }

    /**
     * Returns an item condition that checks whether the item has a specific type of component.
     *
     * @param component The component to check for
     * @param ignoreDefault Whether to ignore the default component value
     * @return The item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HasComponentItemBooleanProperty hasComponent(final @NotNull String component, final boolean ignoreDefault) {
        return new HasComponentItemBooleanPropertyImpl(component, ignoreDefault);
    }

    /**
     * Returns an item condition that checks whether the item has a specific type of component.
     *
     * @param component The component to check for
     * @return The item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HasComponentItemBooleanProperty hasComponent(final @NotNull String component) {
        return hasComponent(component, HasComponentItemBooleanProperty.DEFAULT_IGNORE_DEFAULT);
    }

    /**
     * Returns an item condition that checks whether a keybind is currently active.
     *
     * @param keybind The keybind to check for, the same as used in keybind text components,
     *                e.g. "key.use", "key.left".
     * @return The item condition
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull KeybindDownItemBooleanProperty keybindDown(final @NotNull String keybind) {
        return new KeybindDownItemBooleanPropertyImpl(keybind);
    }
}
