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
package team.unnamed.creative.equipment;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the appearance of an equipment layer, used to define
 * the appearance of equipment when equipped by players or certain mobs.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.2
 * @see Equipment
 */
public interface EquipmentLayer extends Examinable {
    /**
     * The default value for the {@link #usePlayerTexture()} method.
     */
    @ApiStatus.Internal
    boolean DEFAULT_USE_PLAYER_TEXTURE = false;

    /**
     * Returns the equipment layer texture.
     *
     * @return The equipment layer texture
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @NotNull Key texture();

    /**
     * Returns the equipment layer dye configuration, which
     * specifies how the layer behaves when the equipment is
     * dyed.
     *
     * <p>If specified,  layer will be tinted by the color contained
     * in the {@code dyed_color} component in the item.</p>
     *
     * @return The equipment layer dye configuration
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Nullable EquipmentLayerDye dye();

    /**
     * Determines if this layer texture should be overridden by a
     * texture given by the player. Used only by the {@link EquipmentLayerType#WINGS}
     * layer, which will override with the player's custom Elytra texture.
     *
     * <p>False by default.</p>
     *
     * @return If this layer texture should be overridden by the player's texture
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    boolean usePlayerTexture();

    /**
     * Creates a new equipment layer with the given texture, dye configuration
     * and player texture override.
     *
     * @param texture The equipment layer texture
     * @param dye The equipment layer dye configuration
     * @param usePlayerTexture If the player texture should override this layer texture
     * @return A new equipment layer
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull EquipmentLayer layer(final @NotNull Key texture, final @Nullable EquipmentLayerDye dye, final boolean usePlayerTexture) {
        return new EquipmentLayerImpl(texture, dye, usePlayerTexture);
    }

    /**
     * Creates a new equipment layer with the given texture and dye configuration.
     *
     * @param texture The equipment layer texture
     * @param dye The equipment layer dye configuration
     * @return A new equipment layer
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull EquipmentLayer layer(final @NotNull Key texture, final @Nullable EquipmentLayerDye dye) {
        return layer(texture, dye, DEFAULT_USE_PLAYER_TEXTURE);
    }

    /**
     * Creates a new equipment layer with the given texture and player texture override.
     *
     * @param texture The equipment layer texture
     * @param usePlayerTexture If the player texture should override this layer texture
     * @return A new equipment layer
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull EquipmentLayer layer(final @NotNull Key texture, final boolean usePlayerTexture) {
        return layer(texture, null, usePlayerTexture);
    }

    /**
     * Creates a new equipment layer with the given texture.
     *
     * @param texture The equipment layer texture
     * @return A new equipment layer
     * @since 1.8.0
     * @sinceMinecraft 1.21.2
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull EquipmentLayer layer(final @NotNull Key texture) {
        return layer(texture, null, DEFAULT_USE_PLAYER_TEXTURE);
    }
}
