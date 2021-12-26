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
package team.unnamed.uracle;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Class providing static access to a singleton {@link Uracle}
 * API entry point
 */
public final class UracleProvider {

    private static Uracle instance;

    private UracleProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the held {@link Uracle} singleton instance to
     * access the entire library
     *
     * @throws IllegalStateException If API has not been initialized
     * yet
     */
    public static @NotNull Uracle get() {
        Uracle instance = UracleProvider.instance;
        if (instance == null) {
            throw new IllegalStateException(
                    "Cannot access Uracle API before it is initialized. This"
                    + " error can be caused by a plugin that doesn't declare its"
                    + " dependency on Uracle"
            );
        }
        return instance;
    }

    @ApiStatus.Internal
    static void setService(Uracle instance) {
        UracleProvider.instance = instance;
    }

    @ApiStatus.Internal
    static void unsetService() {
        UracleProvider.instance = null;
    }

}
