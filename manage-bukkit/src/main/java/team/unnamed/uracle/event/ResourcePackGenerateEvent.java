package team.unnamed.uracle.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import team.unnamed.uracle.ResourcePackWriter;

public class ResourcePackGenerateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ResourcePackWriter writer;

    public ResourcePackGenerateEvent(ResourcePackWriter writer) {
        this.writer = writer;
    }

    public ResourcePackWriter writer() {
        return writer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void call(ResourcePackWriter writer) {
        Bukkit.getPluginManager()
                .callEvent(new ResourcePackGenerateEvent(writer));
    }

}
