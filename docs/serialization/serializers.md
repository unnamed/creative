## Serializers

Serializers *(Resource Pack Reader & Writer)* are a way to convert
creative's `ResourcePack` to another format.

Currently, there is only one supported format for resource-packs: the
vanilla Minecraft one, which represents all the resources as files in
a file tree *(Like ZIP or a folder)*.

If you need more information on usage for the Minecraft serializers,
[skip to the next page](./minecraft.md)

### API

The `creative-api` provides very general and abstract interfaces which
are responsible for reading and writing resource-packs to "somewhere".

- `ResourcePackReader`: Reads a `ResourcePack` object from a generic `T` object
- `ResourcePackWriter`: Writes a `ResourcePack` object to a generic `T` object

Also note that the only serializable type for the `creative-api` is the
`ResourcePack` type, any other elements are only considered parts of the
resource pack.

If you want to serialize specific elements from the resource pack, you will
need to use implementation-specific methods *(more on that in next pages)*