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
package team.unnamed.creative.metadata.pack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Class representing the "pack" section of the
 * pack.mcmeta file for Minecraft Resource Packs
 *
 * @since 1.0.0
 */
public class PackMeta implements MetadataPart {

    private final PackFormat format;
    private final Component description;
    private String legacyDescription;

    private PackMeta(
            final @NotNull PackFormat format,
            final @NotNull Component description
    ) {
        this.format = requireNonNull(format, "format");
        this.description = requireNonNull(description, "description");
    }

    /**
     * Returns the pack version. If this number does not match the
     * current required number, the resource pack displays an error
     * and requires additional confirmation to load the pack
     *
     * <p>There are format versions assigned to specific Minecraft
     * client versions, e.g.: 7 for Minecraft 1.17 and 1.17.1, 8
     * for Minecraft 1.18 and 1.18.1</p>
     *
     * @return The resource pack format number
     * @since 1.0.0
     * @deprecated Use {@link PackMeta#formats()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public int format() {
        return format.format();
    }

    /**
     * Returns the supported pack formats. The pack format specifies
     * how the resource-pack is structured and which features it
     * uses.
     *
     * <p>There are format versions assigned to specific Minecraft
     * client versions, e.g.: 7 for Minecraft 1.17(.1), 8 for Minecraft
     * 1.18(.1), and so on.</p>
     *
     * @return The resource pack formats
     * @since 1.1.0
     */
    public @NotNull PackFormat formats() {
        return format;
    }

    /**
     * Returns the pack description. Text shown below the pack name in
     * the resource pack menu. The text is shown on two lines. If the
     * text is too long it is truncated
     *
     * @return The resource-pack description
     * @since 1.0.0
     */
    public @NotNull String description() {
        if (legacyDescription != null) {
            return legacyDescription;
        } else {
            return legacyDescription = LegacyComponentSerializer.legacySection().serialize(description);
        }
    }

    /**
     * Returns the pack description. Text shown below the pack name in
     * the resource pack menu. The text is shown on two lines. If the
     * text is too long, it is truncated.
     *
     * @return The resource-pack description
     * @since 1.1.0
     * @deprecated This method will be renamed to description() and the
     * description() method that returns a string will be removed in
     * release 2.0.0
     */
    @ApiStatus.Internal
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Deprecated
    public @NotNull Component description0() {
        return description;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("format", format),
                ExaminableProperty.of("description", description)
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
        PackMeta packMeta = (PackMeta) o;
        return format == packMeta.format
                && description.equals(packMeta.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, description);
    }

    /**
     * Creates a new {@link PackMeta} instance from
     * the given values
     *
     * @param format The pack format
     * @param description The pack description
     * @return A new {@link PackMeta} instance
     * @since 1.0.0
     */
    public static PackMeta of(int format, String description) {
        return new PackMeta(PackFormat.format(format), LegacyComponentSerializer.legacySection().deserialize(description));
    }

    /**
     * Creates a new {@link PackMeta} instance from
     * the given values.
     *
     * @param format The pack format
     * @param description The pack description
     * @return A new {@link PackMeta} instance
     * @since 1.1.0
     */
    public static @NotNull PackMeta of(final @NotNull PackFormat format, final @NotNull Component description) {
        return new PackMeta(format, description);
    }

    /**
     * Creates a new {@link PackMeta} instance from
     * the given values.
     *
     * @param format The pack format
     * @param description The pack description
     * @return A new {@link PackMeta} instance
     * @since 1.1.0
     */
    public static @NotNull PackMeta of(final int format, final @NotNull Component description) {
        return new PackMeta(PackFormat.format(format), description);
    }

}
