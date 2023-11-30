## Handling Requests

As we said in [Start and Stop](./start-stop.md), when constructing a resource-pack
server, we can set either a `handler` or a `pack`, see how this works:

When setting a resource-pack to the server, it creates a default handler that will
always return it no matter the resource-pack request information

We can replace this behavior by specifying a custom resource-pack request handler

<!--@formatter:off-->
```java
ResourcePackServer server = ResourcePackServer.server()
        .address("127.0.0.1", 7270)
        .handler(handler) // <-- Here, see below
        // ...
        .build();
```
<!--@formatter:on-->

### Handler

A handler is a `ResourcePackRequestHandler` implementation, it receives a
*(nullable)* `ResourcePackDownloadRequest` which contains the player uuid, username,
client version, and pack format. It also receives an `HttpExchange` used to set the
response information

The received `ResourcePackDownloadRequest` is null when the server couldn't
parse the received information from the request. `null` indicates that
the request was not made from a vanilla Minecraft client.

Please also note that the information may be spoofed, and may not be
actually sent by the client.

Example:

<!--@formatter:off-->
```java
BuiltResourcePack pack8 = MinecraftResourcePackWriter.minecraft().build(this::createPack8);
BuiltResorucePack pack9 = MinecraftResourcePackWriter.minecraft().build(this::createPack9);

ResourcePackRequestHandler handler = (request, exchange) -> {
    // available methods:
    // - request.uuid()
    // - request.username()
    // - request.clientVersion()
    // - request.clientVersionId()
    // - request.packFormat()
        
    int expectedPackFormat;
    if (request == null) {
        // no information provided, fall back to 9
        expectedPackFormat = 9;
    } else {
        expectedPackFormat = request.packFormat();
    }
    
    BuiltResourcePack pack;
    
    if (expectedPackFormat == 9) {
        pack = pack9;
    } else if (expectedPackFormat == 8) {
        pack = pack8;
    } else {
        // no pack with this format :(
        byte[] response = "No pack for u\n".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(400, response.length);
        try (OutputStream responseStream = exchange.getResponseBody()) {
            responseStream.write(response);
        }
        return;
    }

    // write our resource pack
    byte[] data = pack.bytes();
    exchange.getResponseHeaders().set("Content-Type", "application/zip");
    exchange.sendResponseHeaders(200, data.length);
    try (OutputStream responseStream = exchange.getResponseBody()) {
        responseStream.write(data);
    }
};
```
<!--@formatter:on-->

### Executor

By default, the requests handlers will be executed in a single thread, the same
thread that receives the requests. This means that if a request takes a long time
to process, the next request will have to wait until the previous one finishes.

To avoid this, we can set a multithreaded executor to the server, so the requests
will be processed in different threads, allowing multiple requests to be processed
at the same time:

<!--@formatter:off-->
```java
ResourcePackServer server = ResourcePackServer.server()
        .address("127.0.0.1", 7270)
        .handler(...)
        .executor(Executors.newFixedThreadPool(8)) // <-- will use 8 threads
        .build();
```
<!--@formatter:on-->