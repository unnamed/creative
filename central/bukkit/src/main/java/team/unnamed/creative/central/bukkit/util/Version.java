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
package team.unnamed.creative.central.bukkit.util;

import org.bukkit.Bukkit;

import java.util.regex.Pattern;

/**
 * Class that represents a version following the
 * semantic versioning. See https://semver.org
 */
public class Version {

    public static final String VERSION_STRING
            = Bukkit.getServer().getClass().getName().split(Pattern.quote("."))[3];

    public static final Version CURRENT = parseString(VERSION_STRING);

    private final byte major;
    private final byte minor;
    private final byte patch;

    public Version(byte major, byte minor, byte patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public byte major() {
        return major;
    }

    public byte minor() {
        return minor;
    }

    public byte patch() {
        return patch;
    }

    @Override
    public String toString() {
        return "v" + major + '_' + minor + "_R" + patch;
    }

    /**
     * Resolves a {@link Version} from a given {@code versionString}
     * with the format v(major)_(minor)_R(patch)
     *
     * @throws NumberFormatException If major, minor or patch versions
     *                               are not bytes
     */
    public static Version parseString(String versionString) {
        String[] args = versionString.split(Pattern.quote("_"));
        byte major = Byte.parseByte(args[0].substring(1));
        byte minor = Byte.parseByte(args[1]);
        byte patch = Byte.parseByte(args[2].substring(1));
        return new Version(major, minor, patch);
    }

}