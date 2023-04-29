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
package team.unnamed.creative.central;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Static provider for the {@link CreativeCentral} instance
 *
 * @since 1.0.0
 */
public final class CreativeCentralProvider {

    // Very based on:
    // https://github.com/LuckPerms/LuckPerms/blob/master/api/src/main/java/net/luckperms/api/LuckPermsProvider.java

    private static @Nullable CreativeCentral instance;

    private CreativeCentralProvider() {
    }

    public static CreativeCentral get() {
        CreativeCentral instance = CreativeCentralProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    @ApiStatus.Internal
    public static void set(CreativeCentral instance) {
        requireNonNull(instance, "instance");
        if (CreativeCentralProvider.instance != null) {
            throw new IllegalStateException("Service was already set, you cannot set it twice!");
        }
        CreativeCentralProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unset() {
        CreativeCentralProvider.instance = null;
    }

    private static final class NotLoadedException extends IllegalStateException {

        private static final String MESSAGE = "Creative Central isn't loaded yet!\n" +
                "This could be because:\n" +
                "  a) The Creative Central plugin is not installed or it failed to enable\n" +
                "  b) The plugin in the stacktrace does not declare a dependency on Creative Central\n" +
                "  c) The plugin in the stacktrace is retrieving the API before the plugin 'enable' phase\n" +
                "     (call the #get method in onEnable, not the constructor!)\n";

        NotLoadedException() {
            super(MESSAGE);
        }

    }

}
