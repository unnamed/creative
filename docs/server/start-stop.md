## Start and Stop

Before everything, we must build a resource-pack server, during this
phase we can set some settings like the server address, server port,
handler path, SSL/TLS for HTTPS, etc

Example:

<!--@formatter:off-->
```java
ResourcePackServer server = ResourcePackServer.builder()
        .address("127.0.0.1", 7270) // (required) address and port
        .pack(ResourcePack) // (required) pack to serve
        .secure(...) // (optional) SSL/TLS configuration
        .path("/get/") // (optional) handler path, default = "/"
        .build();
```
<!--@formatter:on-->

**Note:**

- This **will not** automatically start the server
- `pack(...)` can be replaced by `handler(...)`, see
  [Handling Requests](./handle-request.md)

### Start

After we built the resource-pack server we can start it, so it starts
listening for requests

**Note**: This method is not blocking, so it is not necessary to invoke
it in a different thread, the internal HttpServer already starts it in a
background thread,
see [HttpServer#start](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html#start--)

```java
server.start(); // NOT BLOCKING!
```

### Stop

We stop the server using the `stop(int)` method, it asks for an integer value,
which specifies the maximum time in seconds to wait until requests have finished

**Note**: Waiting for the requests is a blocking operation if the maximum time
is greater than zero

```java
server.stop(0); // BLOCKING IF NOT ZERO!
```

