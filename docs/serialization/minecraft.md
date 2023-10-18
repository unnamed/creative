## Minecraft Serializers

The Minecraft serializers represent resource-packs in a way the vanilla Minecraft
client can load them. That is, representing the resource-pack as a ZIP file or as
a folder, where every file inside it is a resource pack element.

The Minecraft serializers also let you convert `ResourcePack` object into a
`BuiltResourcePack`, which can be hosted using the
[creative-server](../server/creative-server.md) subproject.

Note that if you want to use the Minecraft serializers, you will have to include
the `creative-serializer-minecraft` dependency.

```gradle
dependencies {
    implementation("team.unnamed:creative-serializer-minecraft:%%REPLACE_latestRelease{team.unnamed:creative-serializer-minecraft}%%")
}
```

### Usage

Usage is simple and pretty similar to adventure's text serializers. In this
usage guide we are going to provide some examples to use Minecraft resource
pack serializers.

Writing the resource-pack as a ZIP file:

<!--@formatter:off-->
```java
ResourcePack resourcePack = ...;
File output = new File("/path/to/resource-pack.zip");
MinecraftResourcePackWriter.minecraft().writeToZipFile(output, resourcePack);
```
<!--@formatter:on-->

Compiling the resource-pack in memory *(`BuiltResourcePack`)*

<!--@formatter:off-->
```java
ResourcePack resourcePack = ...;
BuiltResourcePack builtResourcePack = MinecraftResourcePackWriter.minecraft().build(resourcePack);
```
<!--@formatter:on-->

Reading the resource-pack from a ZIP file:

<!--@formatter:off-->
```java
File input = new File("/path/to/input/resource-pack.zip");
ResourcePack resourcePack = MinecraftResourcePackReader.minecraft().readFromZipFile(input);
```
<!--@formatter:on-->

### Unitary Serialization

*(Experimental, may drastically change in next major releases)*

The Minecraft serializer knows that the resource-pack elements can be serialized
separately and lets you do that using element-specific serializers, for example:

Deserializing a font from resources:

<!--@formatter:off-->
```java
Font font = FontSerializer.INSTANCE.deserialize(
    Readable.resource(getClass().getClassLoader(), "font.json"),
    Key.key("custom:fontkey")
);
```
<!--@formatter:on-->

Serializing a language to a file:

<!--@formatter:off-->
```java
Language language = ...;
try (OutputStream output = new FileOutputStream("es.json")) {
    LanguageSerializer.INSTANCE.serialize(language, output);
}
```
<!--@formatter:on-->