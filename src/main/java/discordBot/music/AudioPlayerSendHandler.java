package discordBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        // Provide a new audio frame if the previous one has been consumed.
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        // Return the data from the last provided frame.
        if (lastFrame == null) {
            return null;  // If no frame is available, return null.
        }
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;  // Audio sent is Opus encoded, which is required for Discord voice channels.
    }
}
