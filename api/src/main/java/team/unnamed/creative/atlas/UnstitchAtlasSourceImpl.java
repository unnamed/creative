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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.MoreCollections;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class UnstitchAtlasSourceImpl implements UnstitchAtlasSource {

    private final Key resource;
    private final List<Region> regions;
    private final double xDivisor;
    private final double yDivisor;

    UnstitchAtlasSourceImpl(
            final @NotNull Key resource,
            final @NotNull List<Region> regions,
            final double xDivisor,
            final double yDivisor
    ) {
        this.resource = requireNonNull(resource, "resource");
        this.regions = MoreCollections.immutableListOf(requireNonNull(regions, "regions"));
        this.xDivisor = xDivisor;
        this.yDivisor = yDivisor;
    }

    @Override
    public @NotNull Key resource() {
        return resource;
    }

    @Override
    public @NotNull @Unmodifiable List<Region> regions() {
        return regions;
    }

    @Override
    public double xDivisor() {
        return xDivisor;
    }

    @Override
    public double yDivisor() {
        return yDivisor;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("resource", resource),
                ExaminableProperty.of("regions", regions),
                ExaminableProperty.of("xDivisor", xDivisor),
                ExaminableProperty.of("yDivisor", yDivisor)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnstitchAtlasSourceImpl that = (UnstitchAtlasSourceImpl) o;
        if (Double.compare(that.xDivisor, xDivisor) != 0) return false;
        if (Double.compare(that.yDivisor, yDivisor) != 0) return false;
        if (!resource.equals(that.resource)) return false;
        return regions.equals(that.regions);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = resource.hashCode();
        result = 31 * result + regions.hashCode();
        temp = Double.doubleToLongBits(xDivisor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yDivisor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    static final class RegionImpl implements Region {

        private final Key sprite;
        private final double x;
        private final double y;
        private final double width;
        private final double height;

        RegionImpl(
                final @NotNull Key sprite,
                final double x,
                final double y,
                final double width,
                final double height
        ) {
            this.sprite = requireNonNull(sprite, "sprite");
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public @NotNull Key sprite() {
            return sprite;
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double width() {
            return width;
        }

        @Override
        public double height() {
            return height;
        }

        @Override
        public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(
                    ExaminableProperty.of("sprite", sprite),
                    ExaminableProperty.of("x", x),
                    ExaminableProperty.of("y", y),
                    ExaminableProperty.of("width", width),
                    ExaminableProperty.of("height", height)
            );
        }

    }
}
