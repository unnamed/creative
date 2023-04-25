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

import team.unnamed.creative.central.event.Event;
import team.unnamed.creative.central.event.EventListener;
import team.unnamed.creative.central.event.EventBus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public final class EventBusImpl<T> implements EventBus {

    private final Map<Class<?>, List<RegisteredEventListener<?>>> listenersByEventType = new HashMap<>();
    private final Class<T> pluginClass;

    private final Logger logger;

    public EventBusImpl(Class<T> pluginClass, Logger logger) {
        requireNonNull(pluginClass, "pluginClass");
        requireNonNull(logger, "logger");
        this.pluginClass = pluginClass;
        this.logger = logger;
    }

    @Override
    public <E extends Event> void listen(Object plugin, Class<E> eventType, EventListener<E> listener) {
        requireNonNull(plugin, "plugin");
        requireNonNull(eventType, "eventType");
        requireNonNull(listener, "listener");

        if (!pluginClass.isInstance(plugin)) {
            throw new IllegalArgumentException("Plugin is not an instance of " + pluginClass.getName());
        }

        RegisteredEventListener<E> registration = new RegisteredEventListener<>(plugin, listener);

        @SuppressWarnings({"unchecked", "rawtypes"})
        List<RegisteredEventListener<E>> listeners = (List) listenersByEventType
                .computeIfAbsent(eventType, k -> new LinkedList<>());
        listeners.add(registration);
    }

    @Override
    public <E extends Event> void call(Class<E> eventType, E event) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<RegisteredEventListener<E>> listeners = (List) listenersByEventType.get(eventType);
        if (listeners == null) {
            return;
        }

        for (RegisteredEventListener<E> listener : listeners) {
            try {
                listener.listener().on(event);
            } catch (Exception e) {
                logger.log(
                        Level.SEVERE,
                        "Unhandled exception caught when calling event\n"
                                + "    Event: " + eventType.getName() + "\n"
                                + "    For listener: " + listener.listener() + "\n"
                                + "    Of plugin: " + listener.plugin(),
                        e
                );
            }
        }
    }

}
