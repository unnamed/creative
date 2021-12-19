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
package team.unnamed.uracle.model;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.uracle.Vector3Float;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Place where an item or block model is displayed. Holds its
 * rotation, translation and scale for the specified situation
 *
 * @since 1.0.0
 */
public class ModelDisplay implements Examinable {

    /**
     * Specifies the rotation of the model
     */
    private final Vector3Float rotation;

    /**
     * Specifies the position of the model.
     * If the value is greater than 80, it
     * is displayed as 80. If the value is
     * less than -80, it is displayed as -80.
     */
    private final Vector3Float translation;

    /**
     * Specifies the scale of the mode. If
     * the value is greater than 4, it is
     * displayed as 4.
     */
    private final Vector3Float scale;

    private ModelDisplay(
            Vector3Float rotation,
            Vector3Float translation,
            Vector3Float scale
    ) {
        this.rotation = rotation;
        this.translation = translation;
        this.scale = scale;
    }

    /**
     * Returns the rotation for this model
     * display
     *
     * @return The model display rotation
     */
    public Vector3Float rotation() {
        return rotation;
    }

    /**
     * Returns the translation values for
     * this model display
     *
     * @return The model display translation
     */
    public Vector3Float translation() {
        return translation;
    }

    /**
     * Returns the scale values for this
     * model display
     *
     * @return The model display scale
     */
    public Vector3Float scale() {
        return scale;
    }

    /**
     * Enum of all possible model displays
     *
     * @since 1.0.0
     */
    public enum Type {
        THIRDPERSON_RIGHTHAND,
        THIRDPERSON_LEFTHAND,
        FIRSTPERSON_RIGHTHAND,
        FIRSTPERSON_LEFTHAND,
        GUI,
        HEAD,
        GROUND,
        FIXED
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("translation", translation),
                ExaminableProperty.of("scale", scale)
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
        ModelDisplay that = (ModelDisplay) o;
        return rotation.equals(that.rotation) && translation.equals(that.translation) && scale.equals(that.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotation, translation, scale);
    }

}
