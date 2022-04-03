## creative-server

Welcome to the `creative-server` documentation, creative-server is a fast,
lightweight and specialized HTTP(s) server for Minecraft: Java Edition
resource-packs


### Features

The server parses the information that the vanilla Minecraft client sends when
it requests a resource-pack, so we can:

- Return a different resource-pack depending on the player (this implies:
language, version, client name, etc.)
- Ignore or block non-minecraft clients (like browsers)


### More

Check the documentation for:

- [Installation](./installation.md): Dependency details
- [Start/Stop](./start-stop.md): Starting and stopping the server
- [Request Handling](./handle-request.md): Handling resource-pack requests
- [Download Request](./download-request.md): Requesting players to download
our resource-pack