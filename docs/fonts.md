## Fonts

Minecraft supports adding and modifying fonts. We can do a lot of
customization using this, such as:

- Adding emojis to the Minecraft chat *(And our plugin [µŋglyphs](https://github.com/unnamed/emojis)
is an example)*
- Creating custom inventories
- And more...!

### Font Providers

Font providers are the base components of a complete font, a font
may complete multiple font providers. Font providers can be: a bitmap
font provider, legacy unicode font provider, true type font provider


### BitMap Font Provider

This type of font provider requires a bitmap *(PNG)* texture
(see [Textures](textures.md)), ascent *(vertical offset for
the glyphs)* and characters to use for this font

```java
Texture texture = ...;

FontProvider provider = FontProvider.bitMap()
        .file(texture.key())
        .ascent(7)
        .characters(
            "abcdef",
            "012345",
            "6789ñá"
        )
        .build();
```
*TODO: add the other font providers*


### Putting all together

To finally create a `Font`, we just merge all the font
providers and assign a key

```java
Font font = Font.of(
    Key.key("namespace", "my_font"),
    fontProvider1,
    fontProvider2,
    ...
);
```


### Using a font

*Using Adventure*, you could use:
```java
Font font = MyFonts.MY_SPECIAL_FONT;

player.sendMessage(
        text()
            .content("Hello world")
            .font(font.key())
);
```