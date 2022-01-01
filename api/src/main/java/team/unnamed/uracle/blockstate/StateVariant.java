/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package team.unnamed.uracle.blockstate;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * @since 1.0.0
 */
public class StateVariant implements SerializableResource, Examinable {

    private final Key model;
    private final int x;
    private final int y;
    private final boolean uvLock;
    private final int weight;

    private StateVariant(
            Key model,
            int x,
            int y,
            boolean uvLock,
            int weight
    ) {
        this.model = requireNonNull(model, "model");
        this.x = x;
        this.y = y;
        this.uvLock = uvLock;
        this.weight = weight;
    }

    /**
     * Returns the {@link Key} instance which specifies
     * the new model location (resource location)
     *
     * @return The model resource location
     */
    public Key model() {
        return model;
    }

    /**
     * Returns the rotation of the model on the x-axis in
     * increments of 90 degrees
     *
     * @return The model rotation in the X axis
     */
    public int x() {
        return x;
    }

    /**
     * Returns the rotation of the model on the y-axis in
     * increments of 90 degrees
     *
     * @return The model rotation in the Y axis
     */
    public int y() {
        return y;
    }

    /**
     * Can be true or false (default). Locks the rotation of
     * the texture of a block, if set to true. This way the
     * texture does not rotate with the block when using the
     * x and y-tags above
     *
     * @return True if the model texture is locked
     */
    public boolean uvLock() {
        return uvLock;
    }

    /**
     * Sets the probability of the model for being used in the game,
     * defaults to 1 (=100%).
     *
     * <p>If more than one model is used for the same variant, the
     * probability is calculated by dividing the individual model's
     * weight by the sum of the weights of all models</p>
     *
     * @return This state variant weight
     */
    public int weight() {
        return weight;
    }

    @Override
    public void serialize(AssetWriter writer) {
        writer
                .startObject()
                .key("model").value(model)
                .key("x").value(x)
                .key("y").value(y)
                .key("uvlock").value(uvLock);
        if (weight != 1) {
            writer.key("weight").value(weight);
        }
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("model", model),
                ExaminableProperty.of("x", x),
                ExaminableProperty.of("y", y),
                ExaminableProperty.of("uvlock", uvLock),
                ExaminableProperty.of("weight", weight)
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
        StateVariant that = (StateVariant) o;
        return x == that.x
                && y == that.y
                && uvLock == that.uvLock
                && weight == that.weight
                && model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, x, y, uvLock, weight);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key model;
        private int x;
        private int y;
        private boolean uvLock;
        private int weight = 1;

        private Builder() {
        }

        public Builder model(Key model) {
            this.model = requireNonNull(model, "model");
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder uvLock(boolean uvLock) {
            this.uvLock = uvLock;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public StateVariant build() {
            return new StateVariant(model, x, y, uvLock, weight);
        }

    }

}
