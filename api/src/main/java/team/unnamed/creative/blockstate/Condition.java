/*
 * This file is part of creative, licensed under the MIT license
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
package team.unnamed.creative.blockstate;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a {@link Selector} condition, may be used
 * to create from simple to very complex conditions to
 * match block state properties ({@link Condition#match}
 *
 * @see Selector
 * @since 1.0.0
 */
public interface Condition {

    Condition NONE = new Condition() {

        @Override
        public String toString() {
            return "Condition.NONE";
        }

    };

    static Condition and(Condition... conditions) {
        return new Condition.And(Arrays.asList(conditions));
    }

    static Condition or(Condition... conditions) {
        return new Condition.Or(Arrays.asList(conditions));
    }

    static Condition match(String key, Object value) {
        return new Condition.Match(key, value);
    }

    class And implements Condition {

        private final List<Condition> conditions;

        public And(List<Condition> conditions) {
            this.conditions = conditions;
        }

        public List<Condition> conditions() {
            return conditions;
        }

    }

    class Or implements Condition {

        private final List<Condition> conditions;

        public Or(List<Condition> conditions) {
            this.conditions = conditions;
        }

        public List<Condition> conditions() {
            return conditions;
        }

    }

    class Match implements Condition {

        private final String key;
        private final Object value;

        private Match(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String key() {
            return key;
        }

        public Object value() {
            return value;
        }

    }

}
