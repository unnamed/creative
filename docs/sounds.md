## Sounds

In Minecraft resource-packs, sounds have different levels of structure

### 1. `Sound`

It is the smallest unit of the sound system, it just represents a sound (`.ogg`)
file
<!--@formatter:off-->
```java
Sound sound = Sound.sound(
        // location of the sound file in the resource-pack
        // (assets/<namespace>/sounds/<name>.ogg)
        Key.key("creative:meow_1"),
        
        // the sound data, check the Writable API,
        // in this case, the sound data is taken from
        // a file called "the_sound.ogg"
        Writable.file(new File("the_sound.ogg"))
);
```
<!--@formatter:on-->

Remember that sound files are independently written to the resource-pack, to
add a sound to a resource-pack, you use:
<!--@formatter:off-->
```java
ResourcePack resourcePack = ...;
resourcePack.sound(sound);
```
<!--@formatter:on-->

As simple as that!

### 2. `SoundEntry`

It is the second-smallest unit of the sound system, there are two types of sound entries:

2.1. `FILE`, which is just configuration for a single `Sound`, it contains options like
volume, pitch and attenuation distance
<!--@formatter:off-->
```java
SoundEntry soundEntry = SoundEntry.soundEntry()
        .type(SoundEntry.Type.FILE) // <-- Specify type to FILE
        .key(Key.key("creative:meow_1")) // <-- set the key of a Sound
        .volume(1.0F)
        .pitch(1.0F)
        .weight(1)
        .stream(false)
        .attenuationDistance(16)
        .preload(false)
        .build();
```
<!--@formatter:on-->

2.2. `EVENT`, which is configuration for a `SoundEvent` (see below), it allows the same
options as the FILE type
<!--@formatter:off-->
```java
SoundEntry soundEntry = SoundEntry.soundEntry()
        .type(SoundEntry.Type.EVENT) // <-- Specify type to EVENT
        .key(Key.key("creative:meow")) // <-- set the key of a SoundEvent
        .volume(1.0F)
        .pitch(1.0F)
        .weight(1)
        .stream(false)
        .attenuationDistance(16)
        .preload(false)
        .build();
```
<!--@formatter:on-->

### 3. `SoundEvent`

The `SoundEvent` class represents a named sound, this is the one you will use
in-game. A sound event is just a named list of `SoundEntry`'s.

From the server, you can't play specific sound entries (`SoundEntry`) or sounds
(`Sound`), you play/fire sound events (`SoundEvent`)

When a sound event is played/fired, a `SoundEntry` is randomly selected *(based
on their `weight` property)* and played.

<!--@formatter:off-->
```java
SoundEvent soundEvent = SoundEvent.soundEvent()
        .key(Key.key("creative:creative.cat.meow"))
        .sounds(
                soundEntry1,
                soundEntry2,
                soundEntry3,
                ...
        );
        .replace(false)
        .build();
```
<!--@formatter:on-->

Now write the `SoundEvent` in the resource-pack:

<!--@formatter:off-->
```java
ResourcePack resourcePack = ...;
resourcePack.soundEvent(soundEvent);
```
<!--@formatter:on-->

And that's it! We are ready to play sounds in the game

### Playing a custom sound

In **Paper**, you can do the following:

<!--@formatter:off-->
```java
SoundEvent soundEvent = ...;
player.playSound(Sound.sound(soundEvent, Sound.Source.AMBIENT, 1f, 1.1f));
```
<!--@formatter:on-->

See [Adventure documentation](https://docs.adventure.kyori.net/sound.html)