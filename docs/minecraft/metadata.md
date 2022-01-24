## Resource Metadata

Every resource pack file and the resource pack itself can have metadata,
metadata is sub-divided in metadata parts

The metadata file is always suffixed with `.mcmeta`, i.e. `pack.mcmeta`,
`block.png.mcmeta`, the data inside is a JSON object only compound by
the metadata parts, i.e.
```json
{
    "<part name>": {...},
    "<part name 2>": {...},
    ...
    "<part name N>": {...}
}
```

Minecraft defines the following metadata part types:
- `pack`
- `animation`
- `texture`
- `villager`
- `language`


### Pack Metadata Part

Named `pack`, specifies the resource pack metadata, compound by
the resource pack format version and description

| Field Name | Type | Description |
|---|---|---|
| `pack_format` | Integer | The resource pack format version, every client version has its own pack format. `8` for Minecraft 1.18.1
| `description` | String or Object | The resource pack description, may be a string or a component JSON


### Animation Metadata Part

Named `animation`, specifies the animation metadata, applicable for
textures

| Field Name | Type | Description |
|---|---|---|
| `interpolate` | Boolean | Makes the animation be interpolated, the client will generate frames if necessary
| `width` | Integer | The texture width
| `height` | Integer | The texture height
| `frametime` | Integer | The animation default frame time in ticks, frames will use this frame time if they do not specify one
| `frames` | Array\<Frame> | The animation frames, see below

Where `Frame` can be an integer or an object.
If it is an integer, it represents the frame position starting from
the top.
If it is an object, it can specify the frame `index`, which is the
frame position starting from the top, and the frame `time` in ticks


### Texture Metadata Part

Named `texture`, modifies a texture, applicable to all textures

| Field Name | Type | Description |
|---|---|---|
| `blur` | Boolean | `true` to blur texture when viewed from close up
| `clamp` | Boolean | `true` to stretch the texture


### Villager Metadata Part

Named `villager`, modifies the villager hat rendering, applicable to
`entity/villager`and `entity/zombie_villager` textures

| Field Name | Type | Description |
|---|---|---|
| `hat` | String | May be `none`, `partial` or `full`


### Language Metadata Part

Named `language`, declares custom languages added by this resource pack,
applicable to the resource pack metadata (root `pack.mcmeta`)

| Field Name | Type | Description |
|---|---|---|
| *Language Code* | LanguageEntry |  Declares the language entry

Where LanguageEntry is an object with the following structure:

| Field Name | Type | Description |
|---|---|---|
| `name` | String | The language display name |
| `region` | String | The language region |
| `bidirectional` | Boolean | `true` if read from right to left |

#### Example
```json
{
    "en_US": {
        "name": "English",
        "region": "US"
    },
    "namespace:hello": {
        "name": "Custom",
        "region": "Custom"
    }
}
```

### Examples
#### Pack Metadata (pack.mcmeta)
```json
{
    "pack": {
        "pack_format": 8,
        "description": "My resource pack"
    },
    "language": {
        "en_CUSTOM": {
            "name": "Custom English",
            "region": "Custom",
            "bidirectional": false
        }
    }
}
```

#### Texture Metadata (\<texture>.mcmeta)
```json
{
    "animation": {
        "interpolate": true,
        "width": 8,
        "height": 8,
        "frames": [ 0, 1, 2, 5, 4 ]
    },
    "texture": {
        "blur": true,
        "clamp": true
    }
}
```