/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * There are several variants of some blocks (like doors, which can be
 * open or closed), hence each block has its own block state file, which
 * lists all its existing variants and links them to their corresponding
 * models
 *
 * <p>Blocks can also be compound of several models at the same time,
 * called "multipart". The models are then used depending on the block
 * states of the block</p>
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface BlockState extends ResourcePackPart, Keyed, Examinable {

    @Override
    @NotNull Key key();

    Map<String, MultiVariant> variants();

    List<Selector> multipart();

    /**
     * Adds this block state to the given resource container
     *
     * @param resourceContainer The resource container
     * @since 1.1.0
     */
    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.blockState(this);
    }

    /**
     * Creates a new {@link BlockState} object from
     * the given values
     *
     * @param variants  The block state variants
     * @param multipart The block state variants
     *                  (multipart)
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    static BlockState of(
            Key key,
            Map<String, MultiVariant> variants,
            List<Selector> multipart
    ) {
        return new BlockStateImpl(key, variants, multipart);
    }

    /**
     * Creates a new {@link BlockState} instance with
     * the given block state variants
     *
     * @param variants The block state variants
     * @return A new {@link BlockState} variants
     * @since 1.0.0
     */
    static BlockState of(Key key, Map<String, MultiVariant> variants) {
        return new BlockStateImpl(
                key,
                variants,
                Collections.emptyList()
        );
    }

    /**
     * Creates a new {@link BlockState} instance from
     * the given block state multipart info
     *
     * @param multipart The block state cases to use
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    static BlockState of(Key key, List<Selector> multipart) {
        return new BlockStateImpl(
                key,
                Collections.emptyMap(),
                multipart
        );
    }

    static BlockState of(Key key, Selector... multipart) {
        return new BlockStateImpl(
                key,
                Collections.emptyMap(),
                Arrays.asList(multipart)
        );
    }

}
