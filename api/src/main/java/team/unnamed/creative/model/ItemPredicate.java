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
package team.unnamed.creative.model;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents an item predicate, used to know
 * whether to override an item model or not
 *
 * @since 1.0.0
 */
public class ItemPredicate implements Examinable {

    private final String name;
    private final Object value;

    private ItemPredicate(String name, Object value) {
        this.name = requireNonNull(name, "name");
        this.value = requireNonNull(value, "value");
    }

    /**
     * Returns the name of this item predicate,
     * e.g. "custom_model_data"
     *
     * @return The item predicate name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the value for this item predicate,
     * e.g. 1, 0.5, etc.
     *
     * @return The item predicate value
     */
    public Object value() {
        return value;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("name", name),
                ExaminableProperty.of("value", value)
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
        ItemPredicate that = (ItemPredicate) o;
        return name.equals(that.name)
                && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    /**
     * Used on compasses to determine the current angle,
     * expressed in a decimal value of less than one
     */
    public static ItemPredicate angle(float angle) {
        return new ItemPredicate("angle", angle);
    }

    /**
     * Used on shields to determine if currently blocking.
     */
    public static ItemPredicate blocking() {
        return truePredicate("blocking");
    }

    /**
     * Used on Elytra to determine if broken
     */
    public static ItemPredicate broken() {
        return truePredicate("broken");
    }

    /**
     * Used on fishing rods to determine if the fishing rod
     * has been cast
     */
    public static ItemPredicate cast() {
        return truePredicate("cast");
    }

    /**
     * Used on ender pearls and chorus fruit to determine the
     * remaining cooldown, expressed in a decimal value between
     * 0 and 1
     */
    public static ItemPredicate cooldown(float cooldown) {
        return new ItemPredicate("cooldown", cooldown);
    }

    /**
     * Used on items with durability to determine the amount of
     * damage, expressed in a decimal value between 0 and 1
     */
    public static ItemPredicate damage(float damage) {
        return new ItemPredicate("damage", damage);
    }

    /**
     * Used on items with durability to determine if it is damaged
     */
    public static ItemPredicate damaged() {
        return truePredicate("damaged");
    }

    /**
     * Determines the model used by left-handed players. It affects the
     * item they see in inventories, along with the item players see them
     * holding or wearing.
     */
    public static ItemPredicate lefthanded() {
        return truePredicate("lefthanded");
    }

    /**
     * Determines the amount a bow or crossbow has been pulled, expressed
     * in a decimal value of less than one.
     */
    public static ItemPredicate pull(float pull) {
        return new ItemPredicate("pull", pull);
    }

    /**
     * Used on bows and crossbows to determine if the bow is being pulled.
     */
    public static ItemPredicate pulling() {
        return truePredicate("pulling");
    }

    /**
     * Used on crossbows to determine if they are charged with any projectile
     */
    public static ItemPredicate charged() {
        return truePredicate("charged");
    }

    /**
     * Used on crossbows to determine if it is charged with a
     * firework rocket
     */
    public static ItemPredicate firework() {
        return truePredicate("firework");
    }

    /**
     * Used on the trident to determine if the trident is ready to be thrown
     * by the player
     */
    public static ItemPredicate throwing() {
        return truePredicate("throwing");
    }

    /**
     * Used on clocks to determine the current time, expressed in a decimal
     * value of less than one
     */
    public static ItemPredicate time(float time) {
        return new ItemPredicate("time", time);
    }

    /**
     * Used on any item and is compared to the tag.CustomModelData NBT,
     * expressed in an integer value. If the value read from the item data
     * is greater than or equal to the value used for the predicate, the
     * predicate is positive
     */
    public static ItemPredicate customModelData(int data) {
        return new ItemPredicate("custom_model_data", data);
    }

    /**
     * Creates a custom {@link ItemPredicate} instance with
     * the provided name and value
     */
    public static ItemPredicate custom(String name, Object value) {
        return new ItemPredicate(name, value);
    }

    private static ItemPredicate truePredicate(String name) {
        return new ItemPredicate(name, 1.0F);
    }

}
