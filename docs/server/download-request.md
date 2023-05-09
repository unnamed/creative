## Download Request

From our Minecraft server we must ask the player to download our
resource-pack, this is platform-dependent, but it commonly takes
the resource-pack server address and the resource-pack SHA-1 hash
(which can be taken from `BuiltResourcePack#hash()`)

```java
BuiltResourcePack pack = ...;

String hash = pack.hash();

// The resource-pack path, can be empty, but due to a bug
// on the Minecraft client, the hashes are not correctly
// checked, and it will fail to update
String path = hash + ".zip";

// just an example! replace this by your server's
// public address for production
// HTTP is required if you did not configure HTTPS
String url = "http://127.0.0.1:7270/" + path;
```


### Paper

On [Paper](https://papermc.io/) we can use the `Player#setResourcePack`
method:

```java
player.setResourcePack(url, hash /*, prompt, force */);
```


### Minestom

On [Minestom](https://minestom.net/) we can use the `Player#setResourcePack`
method:

```java
// forced(...) or optional(...)
player.setResourcePack(ResourcePack.forced(url, hash /*, prompt */));
```