## Developer API Usage

Now that we have a `CreativeCentral` instance, we can use it to subscribe to
events, fire the resource-pack generate event, etc.

### Adding resources to the server resource-pack
You can add your own resources to the server resource-pack by subscribing
to the `ResourcePackGenerateEvent`, which is called when the server is
loading or when the `/central reload` command is executed.

```java
CreativeCentral central = ...;

central.eventBus().listen(plugin, ResourcePackGenerateEvent.class, event -> {
    ResourcePack resourcePack = event.resourcePack();
   
    // now we do the most important part, adding our own resources to
    // the resource-pack, imagine that we want to add a texture, we
    // would do the following:
        
    // here we create the texture for the minecraft note block
    Texture texture = Texture.builder()
        .key(Key.key("minecraft:block/note_block.png"))
        .data(Writable.file(new File("path/to/texture.png"))
        .build();
        
    // here we register the texture
    resourcePack.texture(texture);
});
```

For more information, check the [creative API](../getting-started.md)