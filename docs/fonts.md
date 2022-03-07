## Fonts

Minecraft supports adding and modifying fonts. We can do a lot of
customization using this, such as:

- Adding emojis to the Minecraft chat *(And our plugin [µŋglyphs](https://github.com/unnamed/emojis)
is an example)*
- Creating custom inventories
- And more...!

### BitMap Font


### Using them

*Using Adventure*, you could use:
```java
FontRegistry font = MyFonts.MY_SPECIAL_FONT;

player.sendMessage(
        text()
            .content("Hello world")
            .font(font.key())
);
```