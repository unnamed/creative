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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

final class UseCycleItemNumericPropertyImpl implements UseCycleItemNumericProperty {
    private final float period;

    UseCycleItemNumericPropertyImpl(final float period) {
        this.period = period;
        if (period < 0) {
            throw new IllegalArgumentException("Period must be greater than or equal to 0, got: " + period);
        }
    }

    @Override
    public float period() {
        return period;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
            ExaminableProperty.of("period", period)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UseCycleItemNumericPropertyImpl that = (UseCycleItemNumericPropertyImpl) o;
        return Float.compare(period, that.period) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period);
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }
}
