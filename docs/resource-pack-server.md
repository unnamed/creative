## Resource Pack Server

*Requires `creative-server` library*

After the client is requested to download a resource pack at a specified
`http` or `https` location, it will send an HTTP `GET` request to the
specified location. It will also add the following headers to the request

```yaml
X-Minecraft-Username:     <Player Username>
X-Minecraft-UUID:         <Player UUID without hyphens>
X-Minecraft-Version:      <Client Version>
X-Minecraft-Version-ID:   <Client Version ID>
X-Minecraft-Pack-Format:  <Resource Pack Format>
User-Agent:                Minecraft Java/<Client Version>
```

The HTTP server must return a ZIP file with the resource pack files, see the
ZIP file structure [here](structure.md)