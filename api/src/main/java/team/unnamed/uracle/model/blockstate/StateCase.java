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
package team.unnamed.uracle.model.blockstate;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.uracle.util.MoreCollections.immutableListOf;
import static team.unnamed.uracle.util.MoreCollections.immutableMapOf;

/**
 * Can be used instead of direct {@link StateVariant} to
 * combine models based on block state attributes
 *
 * <p>Contains a set of conditions that must be met to use
 * the {@code apply} models</p>
 *
 * @since 1.0.0
 */
public class StateCase implements Examinable {

    private final When when;
    @Unmodifiable private final List<StateVariant> apply;

    private StateCase(
            When when,
            List<StateVariant> apply
    ) {
        requireNonNull(when, "when");
        requireNonNull(apply, "apply");
        this.when = when;
        this.apply = immutableListOf(apply);
    }

    public When when() {
        return when;
    }

    public @Unmodifiable List<StateVariant> apply() {
        return apply;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("when", when),
                ExaminableProperty.of("apply", apply)
        );
    }

    /**
     * Represents a filter for {@link StateCase},
     * only contains a set of rules that filter
     * the block block-state
     *
     * @since 1.0.0
     */
    public static class Filter implements Examinable {

        @Unmodifiable private final Map<String, String> state;

        private Filter(Map<String, String> state) {
            requireNonNull(state, "state");
            this.state = immutableMapOf(state);
        }

        /**
         * Returns an unmodifiable map containing the internal
         * set of rules to match a block by its state
         *
         * @return The required state
         */
        public @Unmodifiable Map<String, String> state() {
            return state;
        }

        @Override
        public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(
                    ExaminableProperty.of("state", state)
            );
        }

        @Override
        public String toString() {
            return examine(StringExaminer.simpleEscaping());
        }

    }

    /**
     * Similar to {@link Filter}, but can match other
     * cases specified at the {@code or} list
     *
     * @since 1.0.0
     */
    public static class When extends Filter {

        @Unmodifiable private final List<Filter> or;

        private When(
                List<Filter> or,
                Map<String, String> state
        ) {
            super(state);
            requireNonNull(or, "or");
            this.or = immutableListOf(or);
        }

        public @Unmodifiable List<Filter> or() {
            return or;
        }

    }

}
