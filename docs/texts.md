## Texts

Minecraft supports modifying texts that appear in-game. This
includes various texts such as splash texts, end text and the credits.

### Interacting with texts

Interacting with texts can be done by either adding texts or reading them.
Below is an example with splash texts.

<!--@formatter:off-->
```java
// Reading...
Writable content = pack.texts(Key.key(Key.MINECRAFT_NAMESPACE, "splashes"));

List<String> texts = content.toUTF8String().split("\n");

// Writing...
pack.texts(
        Key.key(Key.MINECRAFT_NAMESPACE, "splashes"),
        Writable.stringUtf8(String.join("\n", texts))
);
```
<!--@formatter:on-->

