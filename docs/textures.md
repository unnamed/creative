## Textures

A texture requires: a `Key` (identifier), and the actual
texture PNG data, which can be passed using the `Writable` API,
e.g.

```java
Texture texture = Texture.builder()
    .key(Key.key("namespace", "my_texture"))
    .data(Writable.file(new File("exampleTexture.png")))
    .build();
```

A texture is also meta-datable, this means you can add some
specific metadata sections to it, e.g. animating a texture

```java
Texture texture = Texture.builder()
    .key(Key.key("namespace", "my_texture"))
    .data(Writable.file(new File("exampleTexture.png")))
    .meta(
        Metadata.builder()
            .add(AnimationMeta.builder().height(16).build())
            .build()
    )
    .build();
```

Then you can add the texture using `ResourcePack#texture(Texture)`