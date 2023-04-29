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
package team.unnamed.creative.central.common.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import team.unnamed.creative.central.event.EventBus;
import team.unnamed.creative.central.event.EventListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class EventBusTest {

    @Test
    public void test_listener_is_called() {
        EventBus eventBus = new EventBusImpl<>(Object.class, Logger.getLogger("TestLogger"));
        Object plugin1 = new Object();

        AtomicBoolean wasCalled = new AtomicBoolean(false);
        eventBus.listen(plugin1, TestEvent.class, event -> wasCalled.set(true));

        eventBus.call(TestEvent.class, new TestEvent());
        Assertions.assertTrue(wasCalled.get(), "Event was not called!");
    }

    @Test
    public void test_listeners_are_called_in_order() {
        EventBus eventBus = new EventBusImpl<>(Object.class, Logger.getLogger("TestLogger 2"));
        Object plugin1 = new Object();

        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.LOWEST, event -> event.assertAndIncrement(0));
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.LOW, event -> event.assertAndIncrement(1));
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.NORMAL, event -> event.assertAndIncrement(2));
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.HIGH, event -> event.assertAndIncrement(3));
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.HIGHEST, event -> event.assertAndIncrement(4));

        TestEvent event = new TestEvent();
        eventBus.call(TestEvent.class, event);
        Assertions.assertEquals(5, event.counter(), "Counter should be equal to five after the event is called");
    }

    @Test
    public void test_an_exception_doesnt_stop_others() {
        // configure logger so the test exceptions don't show in the console
        Logger logger = Logger.getLogger("TestLogger 3");
        logger.setUseParentHandlers(false);

        EventBus eventBus = new EventBusImpl<>(Object.class, logger);
        Object plugin1 = new Object();

        // this listener will not fail
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.LOWEST, event -> event.assertAndIncrement(0));

        // this listener will fail
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.NORMAL, event -> {
            Assertions.assertEquals(1, event.counter(), "Counter should be 1!");
            throw new IllegalStateException("Test exception (ignore if this appears in the console)");
        });

        // this listener will not fail and should be called
        eventBus.listen(plugin1, TestEvent.class, EventListener.Priority.NORMAL, event -> event.assertAndIncrement(1));

        TestEvent event = new TestEvent();
        eventBus.call(TestEvent.class, event);
        Assertions.assertEquals(2, event.counter(), "Counter should be equal to two after the event is called");
    }

}
