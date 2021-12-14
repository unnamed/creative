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
package team.unnamed.uracle.font;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.TreeWriter;
import team.unnamed.uracle.Vector2Float;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * A {@link Font} implementation that uses fonts
 * with the True Type Font format
 *
 * @since 1.0.0
 */
public class TrueTypeFont implements Font {

    /**
     * The resource location of the TrueType font file within
     * assets/&lt;namespace&gt;/font
     */
    private final Key file;

    /**
     * The distance by which the characters of this provider are
     * shifted. X: Left shift, Y: Downwards shift
     */
    private final Vector2Float shift;

    /**
     * Font size to render at
     */
    private final float size;

    /**
     * Resolution to render at
     */
    private final float oversample;

    /**
     * List of strings to exclude
     */
    @Unmodifiable private final List<String> skip;

    protected TrueTypeFont(
            Key file,
            Vector2Float shift,
            float size,
            float oversample,
            List<String> skip
    ) {
        requireNonNull(skip, "skip");
        this.file = requireNonNull(file, "file");
        this.shift = requireNonNull(shift, "shift");
        this.size = size;
        this.oversample = oversample;
        this.skip = unmodifiableList(new ArrayList<>(skip));
    }

    @Override
    public Type type() {
        return Type.TTF;
    }

    public Key file() {
        return file;
    }

    public Vector2Float shift() {
        return shift;
    }

    public float size() {
        return size;
    }

    public float oversample() {
        return oversample;
    }

    public @Unmodifiable List<String> skip() {
        return skip;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("file", file),
                ExaminableProperty.of("shift", shift),
                ExaminableProperty.of("size", size),
                ExaminableProperty.of("oversample", oversample),
                ExaminableProperty.of("skip", skip)
        );
    }

    @Override
    public void write(TreeWriter.Context context) {
        context.startObject();
        context.writeStringField("file", file.asString());

        context.writeKey("shift");
        context.startArray();
        context.writeFloatValue(shift.x());
        context.writeFloatValue(shift.y());
        context.endArray();

        context.writeFloatField("size", size);
        context.writeFloatField("oversample", oversample);

        context.writeKey("skip");
        context.startArray();
        for (String toSkip : skip) {
            context.writeStringValue(toSkip);
        }
        context.endArray();

        context.endObject();
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrueTypeFont that = (TrueTypeFont) o;
        return Float.compare(that.size, size) == 0
                && Float.compare(that.oversample, oversample) == 0
                && file.equals(that.file)
                && shift.equals(that.shift)
                && skip.equals(that.skip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, shift, size, oversample, skip);
    }

}
