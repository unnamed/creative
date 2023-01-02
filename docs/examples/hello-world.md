## Hello World

Let's create a simple and empty resource-pack using `creative-api`

```java
try (FileTree tree = FileTree.directory(new File("folder"))) {
    int packFormat = 12;
    
    // We write the pack.mcmeta file
    tree.write(
            Metadata.builder()
                .add(PackMeta.of(packFormat, "Resource pack description"))
                .build()
    );
}
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
        "description": "Resource pack description"
    }
}
```