## Getting Started

Welcome to the `creative` documentation

`creative` is a resource-pack library for vanilla Minecraft: Java
Edition, it is not dependent on the server implementation *(It doesn't
matter if you use Spigot, Paper, Waterfall, BungeeCord, Velocity, etc.)*

See this example of the `creative` API:

<!--@formatter:off-->
```java
ResourcePack resourcePack = ResourcePack.create();
        
// Required to have a valid resource pack
resourcePack.packMeta(9, "Description!");

// adding the resource pack icon
resourcePack.icon(Writable.file(new File("my-icon.png")));

// adding a texture
resourcePack.texture(texture);

// adding a non special file
resourcePack.unknownFile("credits.txt", Writable.stringUtf8("Unnamed Team"));

// now we have a resource pack ready to be exported/written
// as a ZIP file or as a file tree (you need the 'creative-serializer-minecraft'
// dependency for this class)
MinecraftResourcePackWriter.minecraft().writeToZipFile(
        new File("my-resource-pack.zip"),
        resourcePack
);
```
<!--@formatter:on-->

### Features

- Create, verify and serve resource-packs programmatically
- Create the most compact resource-packs, everything is reduced if it is possible
- `creative` uses & supports [Kyori's adventure](https://github.com/KyoriPowered/adventure)
- ...More things are possible, you just have to discover them

See [hephaestus-engine](https://github.com/unnamed/hephaestus-engine), this
is an example of what can be done with creative

If you prefer to learn with examples you can see the [Examples section](./examples/hello-world.md)