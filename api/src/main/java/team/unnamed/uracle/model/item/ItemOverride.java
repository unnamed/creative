/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle.model.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

public class ItemOverride {

    private final Predicate predicate;

    private final Key model;

    public static class Predicate {

        // todo: change some values to booleans?

        /**
         * Used on compasses to determine the current angle,
         * expressed in a decimal value of less than one.
         */
        @Nullable private final Float angle;

        /**
         * Used on shields to determine if currently blocking.
         * If 1, the player is blocking.
         */
        @Nullable private final Float blocking;

        /**
         * Used on Elytra to determine if broken. If 1, the
         * Elytra is broken.
         */
        @Nullable private final Float broken;

        /**
         * Used on fishing rods to determine if the fishing rod
         * has been cast. If 1, the fishing rod has been cast.
         */
        @Nullable private final Float cast;

        /**
         * Used on ender pearls and chorus fruit to determine the
         * remaining cooldown, expressed in a decimal value between
         * 0 and 1.
         */
        @Nullable private final Float cooldown;

        /**
         * Used on items with durability to determine the amount of
         * damage, expressed in a decimal value between 0 and 1.
         */
        @Nullable private final Float damage;

        /**
         * Used on items with durability to determine if it is damaged.
         * If 1, the item is damaged. Note that if an item has the unbreakable
         * tag, this may be 0 while the item has a non-zero "damage" tag.
         */
        @Nullable private final Float damaged;

        /**
         * Determines the model used by left-handed players. It affects the
         * item they see in inventories, along with the item players see them
         * holding or wearing.
         */
        @Nullable private final Float lefthanded;

        /**
         * Determines the amount a bow or crossbow has been pulled, expressed
         * in a decimal value of less than one.
         */
        @Nullable private final Float pull;

        /**
         * Used on bows and crossbows to determine if the bow is being pulled.
         * If 1, the bow is currently being pulled.
         */
        @Nullable private final Float pulling;

        /**
         * Used on crossbows to determine if they are charged with any projectile.
         * If 1, the crossbow is charged.
         */
        @Nullable private final Float charged;

        /**
         * Used on crossbows. If 1, the crossbow is charged with a firework rocket.
         */
        @Nullable private final Float firework;

        /**
         * Used on the trident to determine if the trident is ready to be thrown
         * by the player. If 1, the trident is ready for fire.
         */
        @Nullable private final Float throwing;

        /**
         * Used on clocks to determine the current time, expressed in a decimal
         * value of less than one.
         */
        @Nullable private final Float time;

        /**
         * Used on any item and is compared to the tag.CustomModelData NBT,
         * expressed in an integer value. The number is still internally converted
         * to float, causing a precision loss for some numbers above 16 million. If
         * the value read from the item data is greater than or equal to the value
         * used for the predicate, the predicate is positive.
         */
        @Nullable private final Integer customModelData;

    }

}
