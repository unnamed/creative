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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.MoreCollections;
import team.unnamed.creative.util.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @sincePackFormat 12
 * @sinceMinecraft 1.19.3
 */
public class Atlas implements Keyed, Examinable {

    public static final Key BLOCKS = Key.key("blocks");
    public static final Key BANNER_PATTERNS = Key.key("banner_patterns");
    public static final Key BEDS = Key.key("beds");
    public static final Key CHESTS = Key.key("chests");
    public static final Key SHIELD_PATTERNS = Key.key("shield_patterns");
    public static final Key SHULKER_BOXES = Key.key("shulker_boxes");
    public static final Key SIGNS = Key.key("signs");
    public static final Key MOB_EFFECTS = Key.key("mob_effects");
    public static final Key PAINTINGS = Key.key("paintings");
    public static final Key PARTICLES = Key.key("particles");
    public static final Key ARMOR_TRIMS = Key.key("armor_trims");
    public static final Key DECORATED_POT = Key.key("decorated_pot");

    private final Key key;
    private final List<AtlasSource> sources;

    private Atlas(Key key, List<AtlasSource> sources) {
        Validate.isNotNull(key, "key");
        Validate.isNotNull(sources);
        this.key = key;
        this.sources = MoreCollections.immutableListOf(sources);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @Unmodifiable List<AtlasSource> sources() {
        return sources;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("sources", sources)
        );
    }

    public static Atlas of(Key key, List<AtlasSource> sources) {
        return new Atlas(key, sources);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Key key;
        private List<AtlasSource> sources;

        private Builder() {
        }

        public Builder key(Key key) {
            this.key = Validate.isNotNull(key, "key");
            return this;
        }

        public Builder sources(List<AtlasSource> sources) {
            this.sources = new ArrayList<>(sources);
            return this;
        }

        public Builder sources(AtlasSource... sources) {
            this.sources = new ArrayList<>(Arrays.asList(sources));
            return this;
        }

        public Builder addSource(AtlasSource source) {
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(source);
            return this;
        }

        public Atlas build() {
            return new Atlas(key, sources);
        }

    }

}
