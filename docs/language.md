## Language

Using a resource-pack, you can add or modify language translations.
A creative language representation is very simple, it is just a
keyed map of translations, e.g.

```java
Language language = Language.of(
    Key.key("minecraft", "en_US"),
    Map.of(
        // translation key // translation
        "gui.yes",            "Yes please",
        "gui.no",             "No!",
        "gui.proceed",        "Do it!"
    )
);
```

Then you can add the language using `ResourcePack#language(Language)`