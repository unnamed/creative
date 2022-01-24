## Resource Pack Font

A font is completely defined by font providers

|Field Name|Type|Description|
|---|---|---|
|`providers`|Array\<Provider>|The array of font providers that compound this font

### Font Provider

A font provider can be of type `bitmap`, `legacy_unicode` or `ttf`, every
type has its own structure and fields

#### BitMap Font Provider
```json
{
    "type": "bitmap",
    "file": "namespace:path/to/texture",
    "height": 8,
    "ascent": 8,
    "chars": [
        "qwerty",
        "asdfgh"
    ]
}
```

#### Legacy Unicode Font Provider
```json
{
    "type": "legacy_unicode",
    "sizes": "namespace:path/to/sizes",
    "template": "namespace:path/to/%s"
}
```

#### TrueType Font Provider
```json
{
    "type": "ttf",
    "file": "namespace:font",
    "shift": [ 0.5, 0.5 ],
    "size": 0.5,
    "oversample": 1.0,
    "skip": ["skip", "this", "char"]
}
```