package team.unnamed.uracle.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import team.unnamed.uracle.ResourcePackBuilder;

public class ResourcePackGenerateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ResourcePackBuilder builder;

    public ResourcePackGenerateEvent(ResourcePackBuilder builder) {
        this.builder = builder;
    }

    public ResourcePackBuilder builder() {
        return builder;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void call(ResourcePackBuilder builder) {
        Bukkit.getPluginManager()
                .callEvent(new ResourcePackGenerateEvent(builder));
    }

}
