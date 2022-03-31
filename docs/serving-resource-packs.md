## Serving Resource Packs

### Starting the server

You must create a resource pack server first. *Requires `creative-server` library*

```java
ResourcePack resourcePack = ...;

// create and bind the HTTP server instance
ResourcePackServer server = ResourcePackServer.builder()
    .address("127.0.0.1", 8080)
    .pack(resourcePack)
    .secure(...) // (optional) HTTPS support
    .build();

// start the server
// (Note that this operation is not blocking, the
// server is created in a background thread)
server.start();

// stop the server (later, obviously)
// 10 seconds as maximum to wait the end of the current requests
server.stop(10);
```


### Setting the resource pack

So we have a resource-pack server, we must tell the player to download
the resource-pack from there, this depends on the platform

In [Paper](https://papermc.io/) you can use:

```java
ResourcePack pack = ...;
// The resource-pack path, can be empty, but due to a bug
// on the Minecraft client, the hashes are not correctly
// checked, and it will fail to update
String path = pack.hash() + ".zip";

player.setResourcePack(
        // just an example! replace this by your server's
        // public address for production
        "http://127.0.0.1:8080/" + path,
        pack.hash()
);
```