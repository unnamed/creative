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
package team.unnamed.creative.item.special;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.DyeColor;
import team.unnamed.creative.base.HeadType;
import team.unnamed.creative.base.WoodType;

/**
 * Represents a special model type which can be rendered via
 * {@link team.unnamed.creative.item.SpecialItemModel}.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface SpecialRender extends Examinable {
    /**
     * Returns a special renderer which renders a banner with
     * patterns from the {@code minecraft:banner_patterns} component
     *
     * @param color The color of the Banner base
     * @return A banner special renderer
     */
    static @NotNull BannerSpecialRender banner(final @NotNull DyeColor color) {
        return new BannerSpecialRenderImpl(color);
    }

    /**
     * Returns a special renderer which renders a whole bed.
     *
     * @param texture The texture of the bed
     * @return A bed special renderer
     */
    static @NotNull BedSpecialRender bed(final @NotNull Key texture) {
        return new BedSpecialRenderImpl(texture);
    }

    /**
     * Returns a conduit special renderer. Which just renders a conduit.
     *
     * @return A conduit special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldSpecialRender conduit() {
        return NoFieldSpecialRenderImpl.CONDUIT;
    }

    /**
     * Returns a chest special renderer, which just renders a chest using
     * the given properties.
     *
     * @param texture The texture of the chest (e.g. {@link ChestSpecialRender#NORMAL_CHEST_TEXTURE},
     *                {@link ChestSpecialRender#GIFT_CHEST_TEXTURE}, etc.)
     * @param openness The openness of the chest, from 0 (fully-closed) to 1 (fully-open)
     * @return A chest special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ChestSpecialRender chest(final @NotNull Key texture, final float openness) {
        return new ChestSpecialRenderImpl(texture, openness);
    }

    /**
     * Returns a chest special renderer, which just renders a chest using
     * the given properties.
     *
     * @param texture The texture of the chest (e.g. {@link ChestSpecialRender#NORMAL_CHEST_TEXTURE},
     *                {@link ChestSpecialRender#GIFT_CHEST_TEXTURE}, etc.)
     * @return A chest special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ChestSpecialRender chest(final @NotNull Key texture) {
        return chest(texture, ChestSpecialRender.DEFAULT_OPENNESS);
    }

    /**
     * Returns a chest special renderer, which just renders a chest using
     * the given properties.
     *
     * @return A chest special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull ChestSpecialRender normalChest() {
        return chest(ChestSpecialRender.NORMAL_CHEST_TEXTURE);
    }

    /**
     * Returns a decorated pot special renderer. Which renders a decorated pot, using values
     * from the {@code minecraft:pot_decorations} component.
     *
     * @return A decorated pot special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldSpecialRender decoratedPot() {
        return NoFieldSpecialRenderImpl.DECORATED_POT;
    }

    /**
     * Returns a shield special renderer. Which renders a shield, using patterns from the
     * {@code minecraft:banner_patterns} component and color from the {@code minecraft:base_color} component.
     *
     * @return A shield special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldSpecialRender shield() {
        return NoFieldSpecialRenderImpl.SHIELD;
    }

    /**
     * Returns a trident special renderer. Which renders a trident.
     *
     * @return A trident special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull NoFieldSpecialRender trident() {
        return NoFieldSpecialRenderImpl.TRIDENT;
    }

    /**
     * Returns a special renderer which renders a hanging sign with the given wood type and texture.
     *
     * @param woodType The wood type of the sign
     * @param texture The texture of the sign
     * @return A sign special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull SignSpecialRender hangingSign(final @NotNull WoodType woodType, final @Nullable Key texture) {
        return new SignSpecialRenderImpl(true, woodType, texture);
    }

    /**
     * Returns a special renderer which renders a standing sign with the given wood type and texture.
     *
     * @param woodType The wood type of the sign
     * @param texture The texture of the sign
     * @return A sign special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull SignSpecialRender standingSign(final @NotNull WoodType woodType, final @Nullable Key texture) {
        return new SignSpecialRenderImpl(false, woodType, texture);
    }

    /**
     * Returns a special renderer which renders a hanging sign with the given wood type.
     *
     * @param woodType The wood type of the sign
     * @return A sign special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull SignSpecialRender hangingSign(final @NotNull WoodType woodType) {
        return hangingSign(woodType, null);
    }

    /**
     * Returns a special renderer which renders a head with the given type, texture
     * and animation state.
     *
     * @param kind The type of the head
     * @param texture The texture of the head
     * @param animation The animation of the head
     * @return A head special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HeadSpecialRender head(final @NotNull HeadType kind, final @Nullable Key texture, final float animation) {
        return new HeadSpecialRenderImpl(kind, texture, animation);
    }

    /**
     * Returns a special renderer which renders a head with the given type and texture.
     *
     * @param kind The type of the head
     * @param texture The texture of the head
     * @return A head special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HeadSpecialRender head(final @NotNull HeadType kind, final @Nullable Key texture) {
        return head(kind, texture, HeadSpecialRender.DEFAULT_ANIMATION);
    }

    /**
     * Returns a special renderer which renders a head with the given type and animation state.
     *
     * @param kind The type of the head
     * @param animation The animation of the head
     * @return A head special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HeadSpecialRender head(final @NotNull HeadType kind, final float animation) {
        return head(kind, null, animation);
    }

    /**
     * Returns a special renderer which renders a head with the given type.
     *
     * @param kind The type of the head
     * @return A head special renderer
     * @since 1.8.0
     * @sinceMinecraft 1.21.4
     * @sincePackFormat 43
     */
    static @NotNull HeadSpecialRender head(final @NotNull HeadType kind) {
        return head(kind, null, HeadSpecialRender.DEFAULT_ANIMATION);
    }

    /**
     * Returns a special renderer which renders a shulker box with the given texture, openness
     * and orientation.
     *
     * @param texture The texture of the shulker box
     * @param openness The openness of the shulker box, from 0 (fully-closed) to 1 (fully-open)
     * @param orientation The orientation of the shulker box
     * @return A shulker box special renderer
     */
    static @NotNull ShulkerBoxSpecialRender shulkerBox(final @NotNull Key texture, final float openness, final @NotNull CubeFace orientation) {
        return new ShulkerBoxSpecialRenderImpl(texture, openness, orientation);
    }

    /**
     * Returns a special renderer which renders a shulker box with the given texture and openness.
     *
     * @param texture The texture of the shulker box
     * @param openness The openness of the shulker box, from 0 (fully-closed) to 1 (fully-open)
     * @return A shulker box special renderer
     */
    static @NotNull ShulkerBoxSpecialRender shulkerBox(final @NotNull Key texture, final float openness) {
        return shulkerBox(texture, openness, ShulkerBoxSpecialRender.DEFAULT_ORIENTATION);
    }

    /**
     * Returns a special renderer which renders a shulker box with the given texture and orientation.
     *
     * @param texture The texture of the shulker box
     * @param orientation The orientation of the shulker box
     * @return A shulker box special renderer
     */
    static @NotNull ShulkerBoxSpecialRender shulkerBox(final @NotNull Key texture, final @NotNull CubeFace orientation) {
        return shulkerBox(texture, ShulkerBoxSpecialRender.DEFAULT_OPENNESS, orientation);
    }

    /**
     * Returns a special renderer which renders a shulker box with the given texture.
     *
     * @param texture The texture of the shulker box
     * @return A shulker box special renderer
     */
    static @NotNull ShulkerBoxSpecialRender shulkerBox(final @NotNull Key texture) {
        return shulkerBox(texture, ShulkerBoxSpecialRender.DEFAULT_OPENNESS, ShulkerBoxSpecialRender.DEFAULT_ORIENTATION);
    }
}
