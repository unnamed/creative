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
package team.unnamed.creative.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

class InvalidRequestTest extends LocalHttpTestBase {

    @Test
    @DisplayName("Test making a request without Minecraft's headers")
    void test_no_headers() throws IOException {
        HttpURLConnection connection = open("/");
        connection.setRequestMethod("GET");

        try {
            connection.getInputStream().close();
            Assertions.fail("Response was valid!");
        } catch (IOException e) {
            String errorResponse = streamToString(connection.getErrorStream());
            Assertions.assertEquals("Please use a Minecraft client\n", errorResponse);
        }
    }

    @Test
    @DisplayName("Test sending invalid Minecraft headers")
    void test_invalid_headers() throws IOException {
        HttpURLConnection connection = open("/");
        connection.setRequestMethod("GET");

        connection.setRequestProperty("X-Minecraft-UUID", "123456789"); // invalid!
        connection.setRequestProperty("X-Minecraft-Username", "Yusshu");
        connection.setRequestProperty("X-Minecraft-Pack-Format", "STRING"); // invalid!
        connection.setRequestProperty("X-Minecraft-Version", "1.18.2");
        connection.setRequestProperty("X-Minecraft-Version-ID", "1.18.2");

        try {
            connection.getInputStream().close();
            Assertions.fail("Response was valid!");
        } catch (IOException e) {
            String errorResponse = streamToString(connection.getErrorStream());
            Assertions.assertEquals("Please use a Minecraft client\n", errorResponse);
        }
    }

}
