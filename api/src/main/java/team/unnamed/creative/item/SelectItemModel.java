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

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.item.property.ItemStringProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an item model that renders models based on matching
 * string properties.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 * @see Item
 * @see ItemModel
 * @see ItemStringProperty
 */
public interface SelectItemModel extends ItemModel {
    @NotNull ItemStringProperty property();

    @NotNull List<SelectItemModel.Case> cases();

    @Nullable ItemModel fallback();

    default @NotNull Builder toBuilder() {
        return ItemModel.select()
                .property(property())
                .fallback(fallback())
                .addCases(cases());
    }

    interface Case extends Examinable {
        @NotNull List<String> when();

        @NotNull ItemModel model();

        static @NotNull Case _case(final @NotNull ItemModel model, final @NotNull List<String> when) {
            return new SelectItemModelImpl.CaseImpl(when, model);
        }

        static @NotNull Case _case(final @NotNull ItemModel model, final @NotNull String @NotNull ... when) {
            return _case(model, Arrays.asList(when));
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval
        static @NotNull Case _case(final @NotNull ItemModel model) {
            return _case(model, Collections.emptyList());
        }
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder property(final @NotNull ItemStringProperty property);

        @Contract("_ -> this")
        @NotNull Builder addCase(final @NotNull Case _case);

        @Contract("_, _ -> this")
        default @NotNull Builder addCase(final @NotNull ItemModel model, final @NotNull List<String> when) {
            return addCase(Case._case(model, when));
        }

        @Contract("_, _ -> this")
        default @NotNull Builder addCase(final @NotNull ItemModel model, final @NotNull String @NotNull ... when) {
            return addCase(model, Arrays.asList(when));
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval
        @Contract("_ -> this")
        default @NotNull Builder addCase(final @NotNull ItemModel model) {
            return addCase(model, Collections.emptyList());
        }

        @Contract("_ -> this")
        default @NotNull Builder addCases(final @NotNull List<Case> cases) {
            cases.forEach(this::addCase);
            return this;
        }

        @Contract("_ -> this")
        @NotNull Builder fallback(final @Nullable ItemModel fallback);

        @Contract("-> new")
        @NotNull SelectItemModel build();
    }
}
