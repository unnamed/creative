package team.unnamed.uracle.event;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import team.unnamed.uracle.io.TreeOutputStream;
import team.unnamed.uracle.io.Writeable;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ResourcePackGenerateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final TreeOutputStream output;

    public ResourcePackGenerateEvent(TreeOutputStream output) {
        this.output = output;
    }

    public void write(String name, Writeable writeable) {
        try {
            output.useEntry(name);
            writeable.write(output);
            output.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void write(String name, byte[] bytes) {
        try {
            output.useEntry(name);
            output.write(bytes);
            output.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void write(String name, String string) {
        write(name, string.getBytes(StandardCharsets.UTF_8));
    }

    public void write(String name, JsonElement element) {
        try {
            output.useEntry(name);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            jsonWriter.setLenient(true);
            Streams.write(element, jsonWriter);
            output.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void call(TreeOutputStream output) {
        Bukkit.getPluginManager()
                .callEvent(new ResourcePackGenerateEvent(output));
    }

}
