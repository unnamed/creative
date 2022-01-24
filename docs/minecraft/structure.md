## Resource Pack File Structure

**Note that resource pack file names can not have uppercase characters**

- `pack.mcmeta`: The resource pack metadata ([See more](pack-metadata.md))
- `pack.png`: The resource pack icon
- `assets`: Root for client resources 
- - `<namespace>`: The resources namespace
- - - `sounds.json`: The resource pack sound definitions ([See more](sounds.md))
- - - `blockstates`:
- - - - `<blockstate>.json`: A block state definition ([See more](blockstate.md))
- - - `font`:
- - - - `<font>.json`: A font definition ([See more](font.md))
- - - `lang`:
- - - - `<lang>.json`: A set of translations for a language ([See more](lang.md))
- - - `models`:
- - - - `<model>.json`: A model definition ([See more](model.md))
- - - `sounds`:
- - - - `<sound>.ogg`: An OGG sound
- - - `textures`:
- - - - `<texture>.png`: A PNG texture