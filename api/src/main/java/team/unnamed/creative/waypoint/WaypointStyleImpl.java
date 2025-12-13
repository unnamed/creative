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
package team.unnamed.creative.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.Examiner;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@ApiStatus.Internal
public class WaypointStyleImpl implements WaypointStyle {
    private final @NotNull Key key;
    private final int nearDistance;
    private final int farDistance;
    private final @NotNull List<Key> sprites;

    public WaypointStyleImpl(@NotNull Key key, int nearDistance, int farDistance, @NotNull List<Key> sprites) {
        this.key = key;
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;
        this.sprites = sprites;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public int nearDistance() {
        return nearDistance;
    }

    @Override
    public int farDistance() {
        return farDistance;
    }

    @Override
    public @NotNull List<Key> sprites() {
        return Collections.unmodifiableList(sprites);
    }

    @Override
    public @NotNull String examinableName() {
        return WaypointStyle.super.examinableName();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("nearDistance", nearDistance),
                ExaminableProperty.of("farDistance", farDistance),
                ExaminableProperty.of("sprites", sprites)
        );
    }

    @Override
    public <R> @NotNull R examine(@NotNull Examiner<R> examiner) {
        return WaypointStyle.super.examine(examiner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WaypointStyleImpl)) return false;
        WaypointStyleImpl that = (WaypointStyleImpl) obj;
        return nearDistance == that.nearDistance
                && farDistance == that.farDistance
                && key.equals(that.key)
                && sprites.equals(that.sprites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, nearDistance, farDistance, sprites);
    }

    static final class BuilderImpl implements WaypointStyle.Builder {
        private Key key;
        private int nearDistance = WaypointStyle.DEFAULT_NEAR_DISTANCE;
        private int farDistance = WaypointStyle.DEFAULT_FAR_DISTANCE;
        private final @NotNull ArrayList<Key> sprites = new ArrayList<>();

        @Override
        public @NotNull WaypointStyle.Builder key(@NotNull Key key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull WaypointStyle.Builder nearDistance(int nearDistance) {
            this.nearDistance = nearDistance;
            return this;
        }

        @Override
        public @NotNull WaypointStyle.Builder farDistance(int farDistance) {
            this.farDistance = farDistance;
            return this;
        }

        @Override
        public @NotNull WaypointStyle.Builder sprite(@NotNull Key sprite) {
            this.sprites.add(sprite);
            return this;
        }

        @Override
        public @NotNull WaypointStyle build() {
            if (key == null) {
                throw new IllegalStateException("Key cannot be null");
            }
            if (sprites.isEmpty()) {
                throw new IllegalStateException("Sprites cannot be empty");
            }
            if (nearDistance < 0) {
                throw new IllegalStateException("Near distance cannot be negative");
            }
            if (farDistance < 0) {
                throw new IllegalStateException("Far distance cannot be negative");
            }
            if (nearDistance > farDistance) {
                throw new IllegalStateException("Near distance cannot be greater than far distance");
            }
            return new WaypointStyleImpl(key, nearDistance, farDistance, sprites);
        }
    }
}
