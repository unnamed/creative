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
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.List;
import java.util.Map;

/**
 * Defines the appearance of equipment when equipped by players
 * or certain mobs.
 *
 * <p>First added in 1.21.2, see <a href="https://feedback.minecraft.net/hc/en-us/articles/31261174284557-Minecraft-Java-Edition-1-21-2-Bundles-of-Bravery">1.21.2 Changelog</a></p>
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.2
 */
public interface Equipment extends ResourcePackPart, Keyed, Examinable {
    /**
     * Returns the key of this equipment, which can
     * be referenced by the {@code equippable} component
     * in items.
     *
     * @return The key of this equipment
     */
    @Override
    @NotNull Key key();

    /**
     * Returns the layers of this equipment, which
     * define the appearance of the equipment when
     * equipped by players or mobs.
     *
     * @return The layers of this equipment
     */
    @NotNull Map<EquipmentLayerType, List<EquipmentLayer>> layers();

    /**
     * Creates a new equipment instance with the given layers.
     *
     * @param layers The layers of the equipment
     * @return The created equipment
     * @since 1.8.0
     */
    @Contract("_ -> new")
    default @NotNull Equipment layers(final @NotNull Map<EquipmentLayerType, List<EquipmentLayer>> layers) {
        return equipment(key(), layers);
    }

    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.equipment(this);
    }

    /**
     * Creates a new equipment instance with the given key
     * and layers.
     *
     * @param key The key of the equipment
     * @param layers The layers of the equipment
     * @return The created equipment
     * @since 1.8.0
     */
    @Contract("_, _ -> new")
    static @NotNull Equipment equipment(final @NotNull Key key, final @NotNull Map<EquipmentLayerType, List<EquipmentLayer>> layers) {
        return new EquipmentImpl(key, layers);
    }

    /**
     * Creates a new builder for creating {@link Equipment} instances.
     *
     * @return A new builder
     * @since 1.8.0
     */
    @Contract("-> new")
    static @NotNull Builder equipment() {
        return new EquipmentImpl.BuilderImpl();
    }

    /**
     * Represents a builder for creating {@link Equipment} instances.
     *
     * @since 1.8.0
     */
    interface Builder {
        /**
         * Sets the key of the equipment.
         *
         * @param key The key of the equipment
         * @return This builder
         */
        @Contract("_ -> this")
        @NotNull Builder key(@NotNull Key key);

        /**
         * Adds a layer to the equipment.
         *
         * @param type The type of the layer
         * @param layer The layer to add
         * @return This builder
         */
        @Contract("_, _ -> this")
        @NotNull Builder addLayer(@NotNull EquipmentLayerType type, @NotNull EquipmentLayer layer);

        //#region Helpers
        /**
         * Adds a humanoid layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addHumanoidLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.HUMANOID, layer);
        }

        /**
         * Adds a humanoid leggings layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addHumanoidLeggingsLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.HUMANOID_LEGGINGS, layer);
        }

        /**
         * Adds a wings layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addWingsLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.WINGS, layer);
        }

        /**
         * Adds a wolf body layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addWolfBodyLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.WOLF_BODY, layer);
        }

        /**
         * Adds a horse body layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addHorseBodyLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.HORSE_BODY, layer);
        }

        /**
         * Adds a llama body layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.0
         * @sinceMinecraft 1.21.2
         */
        @Contract("_ -> this")
        default @NotNull Builder addLlamaBodyLayer(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.LLAMA_BODY, layer);
        }

        /**
         * Adds a llama saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addCamelSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.CAMEL_SADDLE, layer);
        }

        /**
         * Adds a donkey saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addDonkeySaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.DONKEY_SADDLE, layer);
        }

        /**
         * Adds a horse saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addHorseSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.HORSE_SADDLE, layer);
        }

        /**
         * Adds a mule saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addMuleSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.MULE_SADDLE, layer);
        }

        /**
         * Adds a pig saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addPigSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.PIG_SADDLE, layer);
        }

        /**
         * Adds a skeleton horse saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addSkeletonHorseSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.SKELETON_HORSE_SADDLE, layer);
        }

        /**
         * Adds a strider saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addStriderSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.STRIDER_SADDLE, layer);
        }

        /**
         * Adds a zombie horse saddle layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.4
         * @sinceMinecraft 1.21.5
         */
        @Contract("_ -> this")
        default @NotNull Builder addZombieHorseSaddle(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.ZOMBIE_HORSE_SADDLE, layer);
        }

        /**
         * Adds a happy ghast harness layer to the equipment.
         *
         * @param layer The layer to add
         * @return This builder
         * @since 1.8.5
         * @sinceMinecraft 1.21.6
         */
        @Contract("_ -> this")
        default @NotNull Builder addHappyGhastHarness(@NotNull EquipmentLayer layer) {
            return addLayer(EquipmentLayerType.HAPPY_GHAST_HARNESS, layer);
        }
        //#endregion

        /**
         * Builds the equipment.
         *
         * @return The built equipment
         */
        @Contract("-> new")
        @NotNull Equipment build();
    }
}
