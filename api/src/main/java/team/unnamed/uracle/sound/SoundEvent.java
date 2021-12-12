package team.unnamed.uracle.sound;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.ResourceLocation;

import java.util.List;

public class SoundEvent {

    /**
     * The sound event location, usually separated in categories
     * (such as entity.enderman.stare), the file is always at
     * assets/&lt;namespace&gt;/sounds.json. This property specifies
     * the namespace and name of this file
     */
    private ResourceLocation location;

    /**
     * True if the sounds listed in {@link SoundEvent#sounds}
     * should replace the sounds listed in the default
     * sounds.json for this sound event. False if the
     * sounds listed should be added to the list of
     * default sounds
     */
    private boolean replace;

    /**
     * Translated as the subtitle of the sound if
     * "Show Subtitles" is enabled in-game, optional
     */
    @Nullable private String subtitle;

    /**
     * The sounds this sound event uses, one of the
     * listed sounds is randomly selected to play when
     * this sound event is triggered, optional
     */
    @Nullable private List<Sound> sounds;

}
