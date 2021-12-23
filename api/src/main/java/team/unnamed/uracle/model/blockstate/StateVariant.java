/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.model.blockstate;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;

import static java.util.Objects.requireNonNull;

/**
 * @since 1.0.0
 */
public class StateVariant implements Examinable {

    /**
     * Specifies the new model location (resource location)
     */
    private final Key model;

    /**
     * Rotation of the model on the x-axis in increments of
     * 90 degrees
     */
    private final int x;

    /**
     * Rotation of the model on the y-axis in increments of
     * 90 degrees
     */
    private final int y;

    /**
     * Can be true or false (default). Locks the rotation of
     * the texture of a block, if set to true. This way the
     * texture does not rotate with the block when using the
     * x and y-tags above
     */
    private final boolean uvLock;

    /**
     * Sets the probability of the model for being used in the game,
     * defaults to 1 (=100%).
     *
     * <p>If more than one model is used for the same variant, the
     * probability is calculated by dividing the individual model's
     * weight by the sum of the weights of all models</p>
     */
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
}
