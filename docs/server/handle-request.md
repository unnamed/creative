## Handling Requests

As we said in [Start and Stop](./start-stop.md), when constructing a resource-pack
server, we can set either a `handler` or a `pack`, see how this works:

When setting a resource-pack to the server, it creates a default handler that will
always return it no matter the resource-pack request information

We can replace this behavior by specifying a custom resource-pack request handler

```java
ResourcePackServer server = ResourcePackServer.builder()
        .address("127.0.0.1", 7270)
        .handler(handler) // <-- Here, see below
        // ...
        .build();
```


### Handler

A handler is a `ResourcePackRequestHandler` implementation, it receives a
parsed `ResourcePackRequest` which contains the player uuid, username, client
version, and pack format. It also receives an `HttpExchange` used to set
the response information

Example:

```java
ResourcePack pack8 = ResourcePack.build(this::createPack8);
ResorucePack pack9 = ResourcePack.build(this::createPack9);

ResourcePackRequestHandler handler = (request, exchange) -> {
    // available methods:
    // - request.uuid()
    // - request.username()
    // - request.clientVersion()
    // - request.clientVersionId()
    // - request.packFormat()
        
    int expectedPackFormat = request.packFormat();
    ResourcePack pack;
    
    if (expectedPackFormat == 9) {
        pack = pack9;
    } else if (expectedPackFormat == 8) {
        pack = pack8;
    } else {
        // no pack with this format :(
        byte[] response = "No pack for u".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(400, response.length);
        exchange.getResponseBody().write(response);
        return;
    }

    // write our resource pack
    byte[] data = pack.bytes();
    exchange.getResponseHeaders().set("Content-Type", "application/zip");
    exchange.sendResponseHeaders(200, data.length);
    exchange.getResponseBody().write(data);
};
```


### Invalid Requests

Invalid requests are those that do not have the information that the
vanilla Minecraft client sends, we can handle them by overriding the
`onInvalidRequest` method

In the example above we did not override the `onInvalidRequest` method,
so the behavior is defaulted (a 'Please use a Minecraft client' message
is shown)

```java
ResourcePackRequestHandler handler = new ResourcePackRequestHandler() {
    
    @Override
    public void onRequest(ResourcePackRequest request, HttpExchange exchange) throws IOException {
        // handle valid request
    }
    
    @Override
    public void onInvalidRequest(HttpExchange exchange) throws IOException {
        // handle invalid request
        // we can return resource-packs here too!
        // we just do not have the 'request' information available
    }
    
};
```