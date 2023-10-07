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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.pack.PackFormat;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class OverlayEntryImpl implements OverlayEntry {

    static final @RegExp String DIRECTORY_PATTERN = "[a-z0-9-_]+";
    private static final Pattern DIRECTORY_COMPILED_PATTERN = Pattern.compile(DIRECTORY_PATTERN);

    private final PackFormat formats;
    private final String directory;

    OverlayEntryImpl(
            final @NotNull PackFormat formats,
            final @NotNull @Directory String directory
    ) {
        this.formats = requireNonNull(formats, "formats");
        this.directory = requireNonNull(directory, "directory");

        if (!DIRECTORY_COMPILED_PATTERN.matcher(directory).matches()) {
            throw new IllegalArgumentException("Invalid directory name: '" + directory
                    + "' must match pattern: " + DIRECTORY_PATTERN);
        }
    }

    @Override
    public @NotNull PackFormat formats() {
        return formats;
    }

    @Override
    public @NotNull String directory() {
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
        OverlayEntryImpl that = (OverlayEntryImpl) o;
        if (!formats.equals(that.formats)) return false;
        return directory.equals(that.directory);
    }

    @Override
    public int hashCode() {
        int result = formats.hashCode();
        result = 31 * result + directory.hashCode();
        return result;
    }

}
