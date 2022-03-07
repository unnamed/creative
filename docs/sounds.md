## Sounds

*This is not currently done like this, but it is planned*

Create a sound using the creative's `Sound` class

```java
// where "key" points to an OGG audio file
Key key = Key.key("namespace", "brrr");

Sound sound = Sound.builder()
    .nameSound(key)
    .volume(2.0f)
    .pitch(2.0f)
    .attenuationDistance(10)
    .build();
```

Then assign it to a `SoundEvent`

```java
SoundEvent soundEvent = SoundEvent.builder()
    .key(Key.key("namespace", "my_sound"))
    .sounds(List.of(sound1, sound2, ...))
    .build();
```


### Playing a custom sound

*Using adventure*, you could use

```java
SoundEvent soundEvent = ...;

player.playSound(Sound.sound(soundEvent.key(), Sound.Source.AMBIENT, 1f, 1.1f));
```

See [their documentation](https://docs.adventure.kyori.net/sound.html)