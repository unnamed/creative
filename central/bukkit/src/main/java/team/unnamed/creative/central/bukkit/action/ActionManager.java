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
package team.unnamed.creative.central.bukkit.action;

import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.creative.central.pack.ResourcePackStatus;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private final Map<ResourcePackStatus, List<Action>> actionsByStatus = new EnumMap<>(ResourcePackStatus.class);

    public void load(ConfigurationSection config) {
        actionsByStatus.put(ResourcePackStatus.DECLINED, ActionParser.parse(config, "declined"));
        actionsByStatus.put(ResourcePackStatus.ACCEPTED, ActionParser.parse(config, "accepted"));
        actionsByStatus.put(ResourcePackStatus.FAILED, ActionParser.parse(config, "failed"));
        actionsByStatus.put(ResourcePackStatus.LOADED, ActionParser.parse(config, "success"));
    }

    public List<Action> actions(ResourcePackStatus status) {
        return actionsByStatus.getOrDefault(status, Collections.emptyList());
    }

}
