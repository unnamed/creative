## Models

Minecraft supports adding and modifying item and block models. Models determine the
shape and textures of items and blocks.

Blocks use a block state configuration to determine which model is used for each variant.
Meanwhile, each item has a single model, which either loads a block's model, contains data
for its own custom model, or uses the default "flat" or "entity" model.

In this page we will first learn about the parts of a model, and at the end we will
see how to create the model.

### Element

Minecraft Models are composed only by cubes, and each cube is an `Element`. An Element has
a from and to position, a rotation and an optional texture for each of the 6 faces.

We build an Element like this:

<!--@formatter:off-->
```java
Element element = Element.element()
        .from(0, 0, 0)
        .to(16, 16, 16)
        .addFace(CubeFace.UP, ElementFace.face()
                .texture("#0") // use the texture with id '0'
                .uv(TextureUV.uv(0, 0, 1, 1)) // use the full texture
                .build())
        // add more faces, if you do not add one, it won't be visible
        .build();
```
<!--@formatter:on-->

* Note that `16` units is the size of a Minecraft block, but this may depend on where the
  model is displayed.

### Textures

A Model can have multiple textures to be used by the elements. Textures must be specified
and assigned an id to be then referenced by the elements.

<!--@formatter:off-->
```java
ModelTextures textures = ModelTextures.builder()
        .layers(
                // without .png extension
                ModelTexture.ofKey(Key.key("key/to/my/texture"))
        )
        .build();
```
<!--@formatter:on-->

In this example, we are adding a single texture with the key `key/to/my/texture.png`,
which will be referenced by the elements using the id `0` (because it's the first texture,
the layers list is ordered).

### Displays

The displays are used to specify how the model is displayed in different contexts. For
example, we can specify a different scale for the model when it's displayed in an entity's
head, or when it's displayed in a player's hand.

<!--@formatter:off-->
```java
// (API subject to change in next major version)
Map<ItemTransform.Type, ItemTransform> displays = new HashMap<>();

// this will scale the model to 0.5 when it's displayed in an entity's head
displays.put(ItemTransform.Type.HEAD, ItemTransform.transform(
        Vector3Float.ZERO, // rotation: no rotation
        Vector3Float.ZERO, // translation: no translation
        new Vector3Float(0.5, 0.5, 0.5)
));
```
<!--@formatter:on-->

Note that there are some limits regarding the translation and scale of a model. For
translation, all the values must be between `-80` and `80`. For scale, all the value
must be between `0` and `4`. Everything inclusive.

### Overrides

Overrides are used to change the model of an item model depending on its state. For
example, we can change the model of a bow depending on how much it's pulled. Check the
`ItemPredicate` class for a full list of item override predicates.

<!--@formatter:off-->
```java
// this will change the model when the item has a custom model data of '2'
ItemOverride override = ItemOverride.of(
        Key.key("key/to/the/model"),
        ItemPredicate.customModelData(2)
);
```
<!--@formatter:on-->

We can have as many overrides as we want.

### Creating a Model

Finally, we can create a model using the elements and textures we created before:

<!--@formatter:off-->
```java
Model model = Model.model()
        .key(Key.key("custom/my_model"))
        .addElement(element) // the element we created before
        .addOverride(override) // the override we created before
        .displays(displays) // the displays we created before
        .textures(textures) // the textures we created before
        .build();

// There are some extra options like:
//   .parent(Key.key("parent/model")) // to inherit from another model
//   .ambientOcclusion(false)         // to disable ambient occlusion
//   .guiLight(ModelGuiLight.FRONT)   // to change the gui light
```
<!--@formatter:on-->

And then we can add it to the resource-pack using `ResourcePack#model(Model)`

<!--@formatter:off-->
```java
ResourcePack resourcePack = ...;
Model model = ...;
resourcePack.model(model);
```
<!--@formatter:on-->