## Resource Pack Networking

The Minecraft: Java Edition protocol defines two packets for resource pack
handling
- **The client-bound Resource Pack packet** which makes the client download and use a resource pack
- **The server-bound Resource Pack packet** which informs the server about the resource pack download status


### Process
After the client receives the resource pack packet, it can respond with
a server-bound Resource Pack Status packet indicating if the client
accepted the resource pack and will download it (`ACCEPTED`), if the
client declined the resource-pack (`DECLINED`), if resource pack download
failed (`FAILED`) or if it successfully downloaded the resource pack (`SUCCESS`)

See [Serving Resource Packs](serving.md) for information about resource pack
serving on the server side


### Client-bound Resource Pack Packet Structure

| Field Name | Type | Description |
|------------|---------|-------------|
| URL        | String  | The resource pack URL, accepted schemes are `http`, `https` and `level`
| Hash       | String  | SHA-1 hash of the resource pack file, must be at most 40 characters long, must match `^[a-fA-F0-9]{40}$` to be considered a valid hash by the client
| Required   | Boolean | `true` makes the client disconnect if it declines the resource pack
| Prompt     | Optional Component | Message shown as part of the resource pack prompt


### Server-bound Resource Pack Packet Structure

| Field Name | Type | Description |
|---|---|---|
| Status | VarInt | The client resource pack status (0 = `SUCCESS`, 1 = `DECLINED`, 2 = `FAILED`, 3 = `ACCEPTED`)