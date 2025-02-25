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
import team.unnamed.creative.item.property.ItemNumericProperty;

import java.util.List;

public interface RangeDispatchItemModel extends ItemModel {
    @ApiStatus.Internal
    float DEFAULT_SCALE = 1.0f;

    @NotNull ItemNumericProperty property();

    float scale();

    @NotNull List<Entry> entries();

    @Nullable ItemModel fallback();

    interface Entry extends Examinable {
        float threshold();

        @NotNull ItemModel model();

        static @NotNull Entry entry(final float threshold, final @NotNull ItemModel model) {
            return new RangeDispatchItemModelImpl.EntryImpl(threshold, model);
        }
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder property(final @NotNull ItemNumericProperty property);

        @Contract("_ -> this")
        @NotNull Builder scale(final float scale);

        @Contract("_, _ -> this")
        @NotNull Builder addEntry(final float threshold, final @NotNull ItemModel model);

        @Contract("_ -> this")
        @NotNull Builder fallback(final @Nullable ItemModel fallback);

        @Contract("-> new")
        @NotNull RangeDispatchItemModel build();
    }
}
