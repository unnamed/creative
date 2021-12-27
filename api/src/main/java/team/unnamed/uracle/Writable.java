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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for representing objects that can
 * be written to a {@link OutputStream}, this
 * class is util for representing assets that
 * can be exported anytime, without necessarily
 * loading them
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface Writable {

    /**
     * Writes this object information to a
     * {@link OutputStream}, this method can be
     * called anytime, it should be consistent
     * with its data and generate the exact same
     * output for the same input (attributes) of
     * the implementation
     *
     * @param output The target output stream
     * @throws IOException If write fails
     * @since 1.0.0
     */
    void write(OutputStream output) throws IOException;

}
