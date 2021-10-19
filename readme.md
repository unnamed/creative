# Uracle
Library API for next generation servers using resource packs.

## Features
- Library for compatibility between external plugins using resource packs
- Support for a variety of resource-pack servers

## Usage
Usage is simple, simply listen to `ResourcePackGenerateEvent` and write
your assets, see this example:
```java
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.io.Writeable;

public class PackGenerationListener implements Listener {
    
    @EventHandler
    public void onGenerate(ResourcePackGenerateEvent event) {
        
        // writing an image
        File icon = new File("/path/to/icon.png");
        event.write("pack.png", Writeable.ofFile(icon));
        
        // writing a credits.txt file
        event.write("credits.txt", "Credits");
        
        // writing raw bytes
        event.write("path/to/binary", new byte[0]);
        
        // writing a json element (Gson)
        JsonObject json = new JsonObject();
        json.addProperty("hello", "world");
        json.addProperty("foo", true);
        event.write("path/to/file", json);
    }
    
}
```

## Building
You can build Uracle by executing `./gradlew build`, or install to your Maven local repository
using `./gradlew publishToMavenLocal`