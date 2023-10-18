## Language

Using a resource-pack, you can add or modify language translations.
A creative language representation is very simple, it is just a
keyed map of translations, e.g.

<!--@formatter:off-->
```java
Language language = Language.language(
    Key.key("minecraft", "en_US"),
    Map.of(
        // translation key // translation
        "gui.yes",            "Yes please",
        "gui.no",             "No!",
        "gui.proceed",        "Do it!"
    )
);
```
<!--@formatter:on-->

Then you can add the language using `ResourcePack#language(Language)`