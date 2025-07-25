package net.plutondev.expShop.objects;

import org.bukkit.Sound;

public class SoundObject {
    private final String soundName;
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundObject(String soundName, Sound sound, float volume, float pitch) {
        this.soundName = soundName;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public String getSoundName() {
        return soundName;
    }
}
