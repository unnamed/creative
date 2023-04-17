## Hello World

Let's create a simple and empty resource-pack using `creative-api`
and `creative-serializer-minecraft`

```java
ResourcePack resourcePack = ResourcePack.create();

// Here we specify the resource-pack format (12) and description (Hello world!)
resourcePack.packMeta(12, "Hello world!");

// Then we write the resource-pack to a folder
MinecraftResourcePackWriter.minecraft()
    .writeToDirectory(new File("folder"), resourcePack);
```

The code above writes the resource-pack to a folder named 'folder', its content
will be the following:

```
folder/
 |------- pack.mcmeta
```

Where `pack.mcmeta` file has the following content (but minimized):
```json
{
    "pack": {
        "pack_format": 12,
        "description": "Hello world!"
    }
}
```