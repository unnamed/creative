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
package team.unnamed.uracle.blockstate;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.uracle.serialize.AssetWriter;
import team.unnamed.uracle.serialize.SerializableResource;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class StateCase implements SerializableResource {

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
    public void serialize(AssetWriter writer) {
        writer.startObject()
                .key("when")
                .startObject();

        List<StateCase.Filter> filters = when.or();

        if (!filters.isEmpty()) {
            // write "OR" cases if not empty
            writer.key("or").startArray();
            for (StateCase.Filter filter : filters) {
                writer.startObject();
                for (Map.Entry<String, String> condition : filter.state().entrySet()) {
                    writer.key(condition.getKey()).value(condition.getValue());
                }
                writer.endObject();
            }
            writer.endArray();
        }

        for (Map.Entry<String, String> condition : when.state().entrySet()) {
            writer.key(condition.getKey()).value(condition.getValue());
        }
        writer.endObject();

        writer.key("apply").value(apply);
        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("when", when),
                ExaminableProperty.of("apply", apply)
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
        StateCase stateCase = (StateCase) o;
        return when.equals(stateCase.when)
                && apply.equals(stateCase.apply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(when, apply);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Filter filter = (Filter) o;
            return state.equals(filter.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state);
        }

        /**
         * Creates a new filter from the given
         * rules
         *
         * @param state The filter rules
         * @return A new filter instance
         * @since 1.0.0
         */
        public static Filter of(Map<String, String> state) {
            return new Filter(state);
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

        @Override
        public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(
                    ExaminableProperty.of("or", or),
                    ExaminableProperty.of("state", state())
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
            if (!super.equals(o)) return false;
            When when = (When) o;
            return or.equals(when.or);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), or);
        }

        /**
         * Creates a new {@link When} case from the
         * given values
         *
         * @param or Extra cases to match
         * @param state The states
         * @return A new case instance
         * @since 1.0.0
         */
        public static When of(List<Filter> or, Map<String, String> state) {
            return new When(or, state);
        }

    }

}
