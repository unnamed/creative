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
import team.unnamed.creative.util.Validate;

import java.util.List;

/**
 * @sincePackFormat 12
 * @sinceMinecraft 1.19.3
 */
public class UnstitchAtlasSource implements AtlasSource {

    public static final double DEFAULT_X_DIVISOR = 1.0D;
    public static final double DEFAULT_Y_DIVISOR = 1.0D;

    private final Key resource;
    private final List<Region> regions;
    private final double xDivisor;
    private final double yDivisor;

    protected UnstitchAtlasSource(
            Key resource,
            List<Region> regions,
            double xDivisor,
            double yDivisor
    ) {
        this.resource = resource;
        this.regions = regions;
        this.xDivisor = xDivisor;
        this.yDivisor = yDivisor;
    }

    public Key resource() {
        return resource;
    }

    public List<Region> regions() {
        return regions;
    }

    public double xDivisor() {
        return xDivisor;
    }

    public double yDivisor() {
        return yDivisor;
    }

    public static class Region {

        private final Key sprite;
        private final double x;
        private final double y;
        private final double width;
        private final double height;

        private Region(
                Key sprite,
                double x,
                double y,
                double width,
                double height
        ) {
            this.sprite = Validate.isNotNull(sprite);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Key sprite() {
            return sprite;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        public double width() {
            return width;
        }

        public double height() {
            return height;
        }

        public static Region of(Key sprite, double x, double y, double width, double height) {
            return new Region(sprite, x, y, width, height);
        }

    }

}
