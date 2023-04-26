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
package team.unnamed.creative.central.bukkit.listener;

import org.bukkit.entity.Player;
import team.unnamed.creative.central.bukkit.action.Action;
import team.unnamed.creative.central.bukkit.action.ActionManager;
import team.unnamed.creative.central.event.EventListener;
import team.unnamed.creative.central.event.pack.ResourcePackStatusEvent;
import team.unnamed.creative.central.pack.ResourcePackStatus;

import java.util.List;

public class CreativeResourcePackStatusListener implements EventListener<ResourcePackStatusEvent> {

    private final ActionManager actionManager;

    public CreativeResourcePackStatusListener(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override
    public void on(ResourcePackStatusEvent event) {
        ResourcePackStatus status = event.status();
        Player player = (Player) event.player();

        List<Action> actions = actionManager.actions(status);

        for (Action action : actions) {
            action.execute(player);
        }
    }

}
