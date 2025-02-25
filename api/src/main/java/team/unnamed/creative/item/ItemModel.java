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
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.item.condition.ItemCondition;
import team.unnamed.creative.item.property.ItemNumericProperty;
import team.unnamed.creative.item.property.ItemStringProperty;
import team.unnamed.creative.item.special.SpecialRender;
import team.unnamed.creative.item.tint.TintSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface ItemModel extends Examinable {
    /**
     * Creates a reference item model, which renders a plain model from the
     * {@code models} directory.
     *
     * @param model The model key
     * @param tints The list of tint sources
     * @return A reference item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ReferenceItemModel reference(final @NotNull Key model, final @NotNull List<TintSource> tints) {
        return new ReferenceItemModelImpl(model, tints);
    }

    /**
     * Creates a reference item model, which renders a plain model from the
     * {@code models} directory.
     *
     * @param model The model key
     * @param tints The list of tint sources
     * @return A reference item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ReferenceItemModel reference(final @NotNull Key model, final @NotNull TintSource @NotNull ... tints) {
        return reference(model, Arrays.asList(tints));
    }

    /**
     * Creates a reference item model, which renders a plain model from the
     * {@code models} directory.
     *
     * @param model The model key
     * @return A reference item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ReferenceItemModel reference(final @NotNull Key model) {
        return reference(model, Collections.emptyList());
    }

    /**
     * Returns an empty item model. Which does not render anything.
     *
     * @return An empty item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull EmptyItemModel empty() {
        return EmptyItemModelImpl.INSTANCE;
    }

    /**
     * Renders the selected stack in the minecraft:bundle_contents component,
     * if present, renders nothing if missing.
     *
     * @return A bundle selected item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull BundleSelectedItemModel bundleSelectedItem() {
        return BundleSelectedItemModelImpl.INSTANCE;
    }

    /**
     * Creates a composite item model, which renders multiple sub-models.
     * All models are rendered in the same space.
     *
     * @param models The sub-models
     * @return A composite item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CompositeItemModel composite(final @NotNull List<ItemModel> models) {
        return new CompositeItemModelImpl(models);
    }

    /**
     * Creates a composite item model, which renders multiple sub-models.
     * All models are rendered in the same space.
     *
     * @param models The sub-models
     * @return A composite item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull CompositeItemModel composite(final @NotNull ItemModel @NotNull ... models) {
        return new CompositeItemModelImpl(Arrays.asList(models));
    }

    /**
     * Creates a conditional item model, which renders a different item model based on a condition.
     *
     * @param condition The condition to check
     * @param onTrue The item model to render if the condition is true
     * @param onFalse The item model to render if the condition is false
     * @return A conditional item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ConditionItemModel conditional(final @NotNull ItemCondition condition, final @NotNull ItemModel onTrue, final @NotNull ItemModel onFalse) {
        return new ConditionItemModelImpl(condition, onTrue, onFalse);
    }

    /**
     * Creates a select item model (builder), which renders a different item model based on a string property.
     *
     * @return A select item model builder
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see SelectItemModel
     */
    static @NotNull SelectItemModel.Builder select() {
        return new SelectItemModelImpl.BuilderImpl();
    }

    /**
     * Creates a select item model, which renders a different item model based on a string property.
     *
     * @param property The string property to check
     * @param cases The cases to check
     * @param fallback The item model to render if no case matches
     * @return A select item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see SelectItemModel
     */
    static @NotNull SelectItemModel select(final @NotNull ItemStringProperty property, final @NotNull List<SelectItemModel.Case> cases, final @Nullable ItemModel fallback) {
        return new SelectItemModelImpl(property, cases, fallback);
    }

    /**
     * Creates a select item model, which renders a different item model based on a string property.
     *
     * @param property The string property to check
     * @param cases The cases to check
     * @return A select item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see SelectItemModel
     */
    static @NotNull SelectItemModel select(final @NotNull ItemStringProperty property, final @NotNull List<SelectItemModel.Case> cases) {
        return select(property, cases, null);
    }

    /**
     * Creates a select item model, which renders a different item model based on a string property.
     *
     * @param property The string property to check
     * @param cases The cases to check
     * @return A select item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     * @see SelectItemModel
     */
    static @NotNull SelectItemModel select(final @NotNull ItemStringProperty property, final @NotNull SelectItemModel.Case @NotNull ... cases) {
        return select(property, Arrays.asList(cases));
    }

    /**
     * Creates a special item model, which renders a special item model (not data-driven)
     *
     * @param render The special render to use
     * @param base The base key to use
     * @return A special item model
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull SpecialItemModel special(final @NotNull SpecialRender render, final @NotNull Key base) {
        return new SpecialItemModelImpl(render, base);
    }

    static @NotNull RangeDispatchItemModel rangeDispatch(final @NotNull ItemNumericProperty property, final float scale, final @NotNull List<RangeDispatchItemModel.Entry> entries, final @Nullable ItemModel fallback) {
        return new RangeDispatchItemModelImpl(property, scale, entries, fallback);
    }

    static @NotNull RangeDispatchItemModel rangeDispatch(final @NotNull ItemNumericProperty property, final float scale, final @NotNull List<RangeDispatchItemModel.Entry> entries) {
        return rangeDispatch(property, scale, entries, null);
    }

    static @NotNull RangeDispatchItemModel rangeDispatch(final @NotNull ItemNumericProperty property, final float scale, final @NotNull RangeDispatchItemModel.Entry @NotNull ... entries) {
        return rangeDispatch(property, scale, Arrays.asList(entries));
    }

    static @NotNull RangeDispatchItemModel.Builder rangeDispatch() {
        return new RangeDispatchItemModelImpl.BuilderImpl();
    }
}
