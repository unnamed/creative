## Textures

A texture requires: a `Key` (identifier), and the actual
texture PNG data, which can be passed using the `Writable` API,
e.g.

<!--@formatter:off-->
```java
Texture texture = Texture.texture()
    .key(Key.key("namespace", "my_texture.png"))
    .data(Writable.file(new File("exampleTexture.png")))
    .build();
```
<!--@formatter:on-->

A texture is also meta-datable, this means you can add some
specific metadata sections to it, e.g. animating a texture

<!--@formatter:off-->
```java
Texture texture = Texture.texture()
    .key(Key.key("namespace", "my_texture.png"))
    .data(Writable.file(new File("exampleTexture.png")))
    .meta(
        Metadata.builder()
            .add(AnimationMeta.builder().height(16).build())
            .build()
    )
    .build();
```
<!--@formatter:on-->

Then you can add the texture using `ResourcePack#texture(Texture)`