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

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

public interface ItemNumericProperty extends Examinable {
    static @NotNull NoFieldItemNumericProperty bundleFullness() {
        return NoFieldItemNumericPropertyImpl.BUNDLE_FULLNESS;
    }

    static @NotNull CompassItemNumericProperty compass(final @NotNull CompassItemNumericProperty.Target target, final boolean wobble) {
        return new CompassItemNumericPropertyImpl(target, wobble);
    }

    static @NotNull CompassItemNumericProperty compass(final @NotNull CompassItemNumericProperty.Target target) {
        return compass(target, CompassItemNumericProperty.DEFAULT_WOBBLE);
    }

    static @NotNull NoFieldItemNumericProperty cooldown() {
        return NoFieldItemNumericPropertyImpl.COOLDOWN;
    }

    static @NotNull NoFieldItemNumericProperty crossbowPull() {
        return NoFieldItemNumericPropertyImpl.CROSSBOW_PULL;
    }

    static @NotNull CountItemNumericProperty count(final boolean normalize) {
        return new CountItemNumericPropertyImpl(normalize);
    }

    static @NotNull CountItemNumericProperty count() {
        return count(CountItemNumericProperty.DEFAULT_NORMALIZE);
    }

    static @NotNull CustomModelDataItemNumericProperty customModelData(final int index) {
        return new CustomModelDataItemNumericPropertyImpl(index);
    }

    static @NotNull CustomModelDataItemNumericProperty customModelData() {
        return customModelData(CustomModelDataItemNumericProperty.DEFAULT_INDEX);
    }

    static @NotNull DamageItemNumericProperty damage(final boolean normalize) {
        return new DamageItemNumericPropertyImpl(normalize);
    }

    static @NotNull DamageItemNumericProperty damage() {
        return damage(DamageItemNumericProperty.DEFAULT_NORMALIZE);
    }

    static @NotNull TimeItemNumericProperty time(final boolean wobble, final @NotNull TimeItemNumericProperty.Source source) {
        return new TimeItemNumericPropertyImpl(wobble, source);
    }

    static @NotNull TimeItemNumericProperty time(final @NotNull TimeItemNumericProperty.Source source) {
        return time(TimeItemNumericProperty.DEFAULT_WOBBLE, source);
    }

    static @NotNull UseCycleItemNumericProperty useCycle(final float period) {
        return new UseCycleItemNumericPropertyImpl(period);
    }

    static @NotNull UseCycleItemNumericProperty useCycle() {
        return useCycle(UseCycleItemNumericProperty.DEFAULT_PERIOD);
    }

    static @NotNull UseDurationItemNumericProperty useDuration(final boolean remaining) {
        return new UseDurationItemNumericPropertyImpl(remaining);
    }

    static @NotNull UseDurationItemNumericProperty useDuration() {
        return useDuration(UseDurationItemNumericProperty.DEFAULT_REMAINING);
    }
}
