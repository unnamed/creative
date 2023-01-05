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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Namespaced;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.Keys;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class Credits implements Namespaced {

    @Subst(Key.MINECRAFT_NAMESPACE)
    private final String namespace;
    private final List<Section> sections;

    private Credits(String namespace, List<Section> sections) {
        this.namespace = requireNonNull(namespace, "namespace");
        this.sections = requireNonNull(sections, "entries");
        Keys.validateNamespace(namespace);
    }

    @Override
    @Pattern("[a-z0-9_\\-.]+")
    public @NotNull String namespace() {
        return namespace;
    }

    public List<Section> sections() {
        return sections;
    }

    public static class Section {

        private final String name;
        private final List<Title> titles;

        private Section(String name, List<Title> titles) {
            this.name = name;
            this.titles = titles;
        }

        public String name() {
            return name;
        }

        public @Unmodifiable List<Title> titles() {
            return titles;
        }

    }

    public static class Title {

        private final String title;
        private final List<String> names;

        private Title(String title, List<String> names) {
            this.title = title;
            this.names = names;
        }

        public String title() {
            return title;
        }

        public @Unmodifiable List<String> names() {
            return names;
        }

    }

}
