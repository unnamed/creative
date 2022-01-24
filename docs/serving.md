# Serving Resource Packs

The Minecraft: Java Edition vanilla client sends the following HTTP headers to
the resource-pack server when downloading a resource-pack

```yaml
X-Minecraft-Username:     <Player Username>
X-Minecraft-UUID:         <Player UUID>
X-Minecraft-Version:      <Client Version>
X-Minecraft-Version-ID:   <Client Version ID>
X-Minecraft-Pack-Format:  <Resource Pack Format>
User-Agent:                Minecraft Java/<Client Version>
```