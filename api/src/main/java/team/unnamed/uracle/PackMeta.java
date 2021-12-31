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
package team.unnamed.uracle;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.lang.LanguageEntry;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Class representing the "pack.mcmeta" file of
 * Minecraft: Java Edition resource packs
 *
 * @since 1.0.0
 */
public class PackMeta implements Examinable {

    private final PackInfo pack;
    @Unmodifiable private final Map<Key, LanguageEntry> languages;

    private PackMeta(
            PackInfo pack,
            Map<Key, LanguageEntry> languages
    ) {
        requireNonNull(pack, "pack");
        requireNonNull(languages, "languages");
        this.pack = pack;
        this.languages = immutableMapOf(languages);
    }

    /**
     * Returns the pack information of this resource
     * pack, it contains data like format version and
     * pack description
     *
     * @return The pack information
     */
    public PackInfo pack() {
        return pack;
    }

    /**
     * Returns the registered languages for this resource
     * pack
     *
     * @return The registered languages
     */
    public @Unmodifiable Map<Key, LanguageEntry> languages() {
        return languages;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("pack", pack),
                ExaminableProperty.of("language", languages)
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
        return pack.equals(packMeta.pack)
                && languages.equals(packMeta.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pack, languages);
    }

    /**
     * Creates a {@link PackMeta} instance from
     * the given values
     *
     * @param info The resource-pack information
     * @param languages The registered languages
     * @return A new pack meta instance
     * @since 1.0.0
     */
    public static PackMeta of(
            PackInfo info,
            Map<Key, LanguageEntry> languages
    ) {
        return new PackMeta(info, languages);
    }

    /**
     * Creates a new {@link PackMeta} instance
     * from the given {@link PackInfo} instance
     *
     * @param info The resource-pack information
     * @return A new pack meta instance
     * @since 1.0.0
     */
    public static PackMeta of(PackInfo info) {
        return new PackMeta(info, Collections.emptyMap());
    }

}
