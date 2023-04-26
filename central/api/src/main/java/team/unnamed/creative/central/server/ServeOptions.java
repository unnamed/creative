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
package team.unnamed.creative.central.server;

import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.central.request.ResourcePackRequest;

public final class ServeOptions {

    private @Nullable ResourcePackRequest request = null;
    private int delay = 0;
    private boolean serve = true;

    public @Nullable ResourcePackRequest request() {
        return request;
    }

    public void request(@Nullable ResourcePackRequest request) {
        this.request = request;
    }

    public int delay() {
        return delay;
    }

    public void delay(int delay) {
        this.delay = delay;
    }

    public boolean serve() {
        return serve;
    }

    public void serve(boolean serve) {
        this.serve = serve;
    }

}
