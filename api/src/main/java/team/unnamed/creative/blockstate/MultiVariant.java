/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.blockstate;

import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.file.ResourceWriter;
import team.unnamed.creative.file.SerializableResource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class MultiVariant implements SerializableResource {

    private final List<Variant> variants;

    private MultiVariant(List<Variant> variants) {
        this.variants = requireNonNull(variants, "variants");
        validate();
    }

    private void validate() {
        if (variants.size() < 1) {
            throw new IllegalArgumentException("No variants provided!");
        }
    }

    public List<Variant> variants() {
        return variants;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("variants", variants)
        );
    }

    @Override
    public void serialize(ResourceWriter writer) {
        if (variants.size() == 1) {
            // single variant, write as an object
            variants.get(0).serialize(writer);
        } else {
            // multiple variants, write as an array
            writer.startArray();
            for (Variant variant : variants) {
                variant.serialize(writer);
            }
            writer.endArray();
        }
    }

}