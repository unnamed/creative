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
package team.unnamed.creative.util;

import org.jetbrains.annotations.ApiStatus;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.base.Vector4Float;

@ApiStatus.Internal
public class Range {
    public static int coerceIn(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static long coerceIn(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float coerceIn(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double coerceIn(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static Vector2Float coerceIn(Vector2Float value, float min, float max) {
        if (value == null) return null;
        return new Vector2Float(
                coerceIn(value.x(), min, max),
                coerceIn(value.y(), min, max)
        );
    }

    public static Vector2Float coerceIn(Vector2Float value, Vector2Float min, Vector2Float max) {
        if (value == null) return null;
        return new Vector2Float(
                coerceIn(value.x(), min.x(), max.x()),
                coerceIn(value.y(), min.y(), max.y())
        );
    }

    public static Vector3Float coerceIn(Vector3Float value, float min, float max) {
        if (value == null) return null;
        return new Vector3Float(
                coerceIn(value.x(), min, max),
                coerceIn(value.y(), min, max),
                coerceIn(value.z(), min, max)
        );
    }

    public static Vector3Float coerceIn(Vector3Float value, Vector3Float min, Vector3Float max) {
        if (value == null) return null;
        return new Vector3Float(
                coerceIn(value.x(), min.x(), max.x()),
                coerceIn(value.y(), min.y(), max.y()),
                coerceIn(value.z(), min.z(), max.z())
        );
    }

    public static Vector4Float coerceIn(Vector4Float value, float min, float max) {
        if (value == null) return null;
        return new Vector4Float(
                coerceIn(value.x(), min, max),
                coerceIn(value.y(), min, max),
                coerceIn(value.x2(), min, max),
                coerceIn(value.y2(), min, max)
        );
    }

    public static Vector4Float coerceIn(Vector4Float value, Vector4Float min, Vector4Float max) {
        if (value == null) return null;
        return new Vector4Float(
                coerceIn(value.x(), min.x(), max.x()),
                coerceIn(value.y(), min.y(), max.y()),
                coerceIn(value.x2(), min.x2(), max.x2()),
                coerceIn(value.y2(), min.y2(), max.y2())
        );
    }

    public static boolean isBetween(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isBetween(long value, long min, long max) {
        return value >= min && value <= max;
    }

    public static boolean isBetween(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isBetween(Vector2Float value, float min, float max) {
        return isBetween(value.x(), min, max) && isBetween(value.y(), min, max);
    }

    public static boolean isBetween(Vector2Float value, Vector2Float min, Vector2Float max) {
        return isBetween(value.x(), min.x(), max.x()) && isBetween(value.y(), min.y(), max.y());
    }

    public static boolean isBetween(Vector3Float value, float min, float max) {
        return isBetween(value.x(), min, max) && isBetween(value.y(), min, max) && isBetween(value.z(), min, max);
    }

    public static boolean isBetween(Vector3Float value, Vector3Float min, Vector3Float max) {
        return isBetween(value.x(), min.x(), max.x()) && isBetween(value.y(), min.y(), max.y()) && isBetween(value.z(), min.z(), max.z());
    }

    public static boolean isBetween(Vector4Float value, float min, float max) {
        return isBetween(value.x(), min, max) && isBetween(value.y(), min, max) && isBetween(value.x2(), min, max) && isBetween(value.y2(), min, max);
    }

    public static boolean isBetween(Vector4Float value, Vector4Float min, Vector4Float max) {
        return isBetween(value.x(), min.x(), max.x()) && isBetween(value.y(), min.y(), max.y()) && isBetween(value.x2(), min.x2(), max.x2()) && isBetween(value.y2(), min.y2(), max.y2());
    }
}
