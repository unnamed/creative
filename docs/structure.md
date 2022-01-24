## Resource Pack File Structure

```yaml
pack.mcmeta: The pack metadata (required)
pack.png: The pack logo
assets:
  <namespace>:
    sounds.json: The resource pack sounds
    blockstates:
      <blockstate>.json: A block state definition
    font:
      <font>.json: A font definition
    lang:
      <lang>.json: A set of translations for a language
    models:
      <model>.json: A model definition
    sounds:
      <sound>.ogg: An OGG sound
    textures:
      <texture>.png: A PNG texture
```