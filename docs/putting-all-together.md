## Putting it all together

Now we can build the resource-pack!

```java
ResourcePack resourcePack = ResourcePack.create();

// Required to have a valid resource pack
resourcePack.packMeta(9, "Description!");

// adding the resource pack icon
resourcePack.icon(Writable.file(...)); // Use any type of Writable
        
// adding a font
resourcePack.font(font);

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