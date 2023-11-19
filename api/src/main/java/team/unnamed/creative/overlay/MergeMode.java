/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
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
package team.unnamed.creative.overlay;

/**
 * Represents a merging mode, determines how to merge
 * two {@link ResourceContainer} instances in one.
 *
 * @see ResourceContainer#merge(ResourceContainer, MergeMode)
 * @since 1.4.0
 */
public enum MergeMode {
    /**
     * Overrides the resources of the first container
     * with the resources of the second container.
     *
     * @since 1.4.0
     */
    OVERRIDE,

    /**
     * Merges the resources of the first container
     * with the resources of the second container.
     *
     * <p>If there are duplicates that can't be merged,
     * the merging will fail.</p>
     *
     * @since 1.4.0
     */
    MERGE_AND_FAIL_ON_ERROR,

    /**
     * Merges the resources of the first container
     * with the resources of the second container.
     *
     * <p>If there are duplicates that can't be merged,
     * the resources of the first container will be kept.</p>
     *
     * @since 1.4.0
     */
    MERGE_AND_KEEP_FIRST_ON_ERROR
}
