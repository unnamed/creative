/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
package team.unnamed.creative.resources;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.overlay.ResourceContainer;

/**
 * Represents a merging strategy, determines how to merge
 * two {@link ResourceContainer} instances in one.
 *
 * @see ResourceContainer#merge(ResourceContainer, MergeStrategy)
 * @since 1.4.0
 */
@ApiStatus.NonExtendable
public interface MergeStrategy {
    /**
     * Gets a merge strategy that overrides the resources of
     * the first container with the resources of the second
     * container if there are duplicates.
     *
     * @return The override merge strategy
     * @since 1.4.0
     */
    static @NotNull MergeStrategy override() {
        return MergeStrategyImpl.OVERRIDE;
    }

    /**
     * Gets a merge strategy that merges the resources of the
     * first container with the resources of the second container,
     * resulting in an exception if there are duplicates that can't
     * be merged.
     *
     * @return The merge and fail on error merge strategy
     * @since 1.4.0
     */
    static @NotNull MergeStrategy mergeAndFailOnError() {
        return MergeStrategyImpl.MERGE_AND_FAIL_ON_ERROR;
    }

    /**
     * Gets a merge strategy that merges the resources of the
     * first container with the resources of the second container,
     * keeping the resources of the first container if there are
     * duplicates that can't be merged.
     *
     * @return The merge and keep first on error merge strategy
     * @since 1.4.0
     */
    static @NotNull MergeStrategy mergeAndKeepFirstOnError() {
        return MergeStrategyImpl.MERGE_AND_KEEP_FIRST_ON_ERROR;
    }
}
