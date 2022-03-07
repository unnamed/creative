## Putting it all together

Now we can build the resource-pack!

```java
ResourcePack resourcePack = ResourcePack.build(tree -> {
    
    // TODO: Write top-level metadata file
    
    // writing a font...
    tree.write(font);
    
    // writing a texture...
    tree.write(texture);
    
    // writing a non special file
    tree.write("credits.txt", Writable.bytes("Unnamed Team".getBytes()));
});
```