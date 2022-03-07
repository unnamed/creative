## Putting it all together

Now we can build the resource-pack!

```java
ResourcePack resourcePack = ResourcePack.build(tree -> {
    
    // Required to be a valid resource-pack!
    tree.write(Metadata.builder()
        .add(PackMeta.of(8, "Description!"))
        // .add(LanguageMeta...) // to register custom languages!
        .build());
    
    // The resource-pack icon
    tree.write("pack.png", Writable...);
    
    // writing a font...
    tree.write(font);
    
    // writing a texture...
    tree.write(texture);
    
    // writing a non special file
    tree.write("credits.txt", Writable.bytes("Unnamed Team".getBytes()));
});
```