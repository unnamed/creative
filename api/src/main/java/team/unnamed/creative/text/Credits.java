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
package team.unnamed.creative.text;

import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Namespaced;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.List;

/**
 * Represents the Minecraft credits file.
 *
 * @sinceMinecraft 1.17
 * @sincePackFormat 7
 * @since 1.3.0
 */
public interface Credits extends Namespaced, ResourcePackPart, Examinable {
    /**
     * Gets the namespace for this credits file.
     *
     * @return The namespace
     * @sinceMinecraft 1.17
     * @sincePackFormat 7
     * @since 1.3.0
     */
    @KeyPattern.Namespace
    @Override
    @NotNull String namespace();

    /**
     * Gets the credits sections.
     *
     * @return The credits sections
     * @sinceMinecraft 1.17
     * @sincePackFormat 7
     * @since 1.3.0
     */
    @NotNull @Unmodifiable List<Section> sections();

    /**
     * Represents a section of the credits file.
     *
     * @sinceMinecraft 1.17
     * @sincePackFormat 7
     * @since 1.3.0
     */
    interface Section {
        /**
         * Gets the section name.
         *
         * @return The section name
         * @sinceMinecraft 1.17
         * @sincePackFormat 7
         * @since 1.3.0
         */
        @NotNull String name();

        /**
         * Gets the section disciplines.
         *
         * <p>Will contain a single discipline with an empty name
         * if creating a pre Minecraft 1.20 resource-pack, and all
         * the titles will be inside the single discipline.</p>
         *
         * @return The section disciplines
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.3.0
         */
        @NotNull @Unmodifiable List<Discipline> disciplines();

        /**
         * Gets the section titles.
         *
         * <p>As of Minecraft 1.20, a new organization level "disciplines"
         * was added. This method will collect all the titles from the
         * {@link #disciplines() disciplines}.</p>
         *
         * <p>If we are building a resource-pack pre Minecraft 1.20, the
         * builder will create a single discipline containing all the
         * titles and will work as normally.</p>
         *
         * @return The section titles
         * @sinceMinecraft 1.17
         * @sincePackFormat 7
         * @since 1.3.0
         * @deprecated Since Minecraft 1.20, use {@link #disciplines()} instead.
         */
        @Deprecated
        @NotNull @Unmodifiable List<Title> titles();
    }


    /**
     * Represents a credits section discipline.
     *
     * @sinceMinecraft 1.20
     * @sincePackFormat 15
     * @since 1.3.0
     */
    interface Discipline {
        /**
         * Gets the discipline name.
         *
         * @return The discipline name
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.3.0
         */
        @NotNull String name();

        /**
         * Gets the discipline titles.
         *
         * @return The discipline titles
         * @sinceMinecraft 1.20
         * @sincePackFormat 15
         * @since 1.3.0
         */
        @NotNull @Unmodifiable List<Title> titles();
    }

    /**
     * Represents a credits title.
     *
     * @sinceMinecraft 1.17
     * @sincePackFormat 7
     * @since 1.3.0
     */
    interface Title {
        /**
         * Gets the title name.
         *
         * @return The title name
         * @sinceMinecraft 1.17
         * @sincePackFormat 7
         * @since 1.3.0
         */
        @NotNull String name();

        /**
         * Gets the names of the people with
         * this title.
         *
         * @return The names of the people with
         * this title
         * @sinceMinecraft 1.17
         * @sincePackFormat 7
         * @since 1.3.0
         */
        @NotNull @Unmodifiable List<String> names();
    }
}
