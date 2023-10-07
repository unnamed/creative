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
package team.unnamed.creative.metadata.overlays;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.pack.PackFormat;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.*;
import static java.util.Objects.requireNonNull;

/**
 * Represents an overlay entry for the {@link OverlaysMeta}
 * part.
 *
 * <p>An overlay entry tells the client that there is an
 * available overlay for an specific pack format range.</p>
 *
 * @since 1.1.0
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 */
public final class OverlayEntry implements Examinable {

    private final PackFormat formats;
    private final String directory;

    private OverlayEntry(
            final @NotNull PackFormat formats,
            final @NotNull @Directory String directory
    ) {
        this.formats = requireNonNull(formats, "formats");
        this.directory = requireNonNull(directory, "directory");
    }

    /**
     * Gets the pack formats range that this overlay
     * supports.
     *
     * @return The pack formats.
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    public @NotNull PackFormat formats() {
        return formats;
    }

    /**
     * Gets the directory name that this overlay uses.
     *
     * @return The directory name.
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    public @NotNull @Subst("dir") String directory() {
        return directory;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("formats", formats),
                ExaminableProperty.of("directory", directory)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverlayEntry that = (OverlayEntry) o;
        if (!formats.equals(that.formats)) return false;
        return directory.equals(that.directory);
    }

    @Override
    public int hashCode() {
        int result = formats.hashCode();
        result = 31 * result + directory.hashCode();
        return result;
    }

    /**
     * Creates a new {@link OverlayEntry} instance with the
     * given pack formats range and directory name.
     *
     * @param formats The pack formats.
     * @param directory The directory name.
     * @return The overlay entry.
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    public static @NotNull OverlayEntry of(
            final @NotNull PackFormat formats,
            final @NotNull @Subst("dir") @Directory String directory
    ) {
        return new OverlayEntry(formats, directory);
    }

    /**
     * Specifies that the annotated string must be a valid directory
     * string for overlay entries. They must match a specific regex
     * pattern.
     *
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    @Retention(RetentionPolicy.CLASS)
    @Pattern("[a-z0-9-_]+")
    @Documented
    @NonNls
    @Target({ METHOD, FIELD, PARAMETER, LOCAL_VARIABLE })
    public @interface Directory {
    }

}
