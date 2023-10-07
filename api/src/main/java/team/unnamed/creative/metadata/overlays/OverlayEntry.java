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
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.pack.PackFormat;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

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
@ApiStatus.NonExtendable
public interface OverlayEntry extends Examinable {

    /**
     * Gets the pack formats range that this overlay
     * supports.
     *
     * @return The pack formats.
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    @NotNull PackFormat formats();

    /**
     * Gets the directory name that this overlay uses.
     *
     * @return The directory name.
     * @since 1.1.0
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     */
    @NotNull @Subst("dir") String directory();

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
        return new OverlayEntryImpl(formats, directory);
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
