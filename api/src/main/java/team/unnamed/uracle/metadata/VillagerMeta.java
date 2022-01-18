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
package team.unnamed.uracle.metadata;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.uracle.file.ResourceWriter;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Villager textures (from entity/villager and entity/zombie_villager)
 * support a .MCMETA file containing additional effects to apply to the
 * hat layer
 *
 * @since 1.0.0
 */
public class VillagerMeta implements MetadataPart {

    private final Hat hat;

    private VillagerMeta(Hat hat) {
        this.hat = requireNonNull(hat, "hat");
    }

    @Override
    public String name() {
        return "villager";
    }

    /**
     * Determines how the villager hat should render
     * ("none", "partial", "full")
     */
    public Hat hat() {
        return hat;
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.key("villager").startObject();
        if (hat != Hat.NONE) {
            // only write if not default value
            writer.key("hat").value(hat.name().toLowerCase(Locale.ROOT));
        }
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("hat", hat)
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
        VillagerMeta that = (VillagerMeta) o;
        return Objects.equals(hat, that.hat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hat);
    }

    /**
     * Creates a new {@link VillagerMeta} instance
     * from the given values
     *
     * @param hat Hat mode
     * @return A new instance of {@link VillagerMeta}
     * @since 1.0.0
     */
    public static VillagerMeta of(Hat hat) {
        return new VillagerMeta(hat);
    }

    public enum Hat {
        NONE, // default
        PARTIAL,
        FULL
    }

}
