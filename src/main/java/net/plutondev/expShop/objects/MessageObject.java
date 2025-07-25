package net.plutondev.expShop.objects;

import java.util.List;

public class MessageObject {
    private final List<String> message;
    private final SoundObject sound;

    public MessageObject(List<String> message, SoundObject sound) {
        this.message = message;
        this.sound = sound;
    }

    public List<String> getMessage() {
        return message;
    }

    public SoundObject getSound() {
        return sound;
    }
}
