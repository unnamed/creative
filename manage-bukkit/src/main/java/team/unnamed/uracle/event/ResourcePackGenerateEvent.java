package team.unnamed.uracle.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import team.unnamed.uracle.ResourcePack;

public class ResourcePackGenerateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ResourcePack.Builder builder;

    public ResourcePackGenerateEvent(ResourcePack.Builder builder) {
        this.builder = builder;
    }

    public ResourcePack.Builder builder() {
        return builder;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void call(ResourcePack.Builder builder) {
        Bukkit.getPluginManager()
                .callEvent(new ResourcePackGenerateEvent(builder));
    }

}
