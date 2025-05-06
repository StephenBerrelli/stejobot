package discordBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private ByteBuffer buffer;
    private final byte[] data;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.data = new byte[1024];
    }

    @Override
    public boolean canProvide() {
        final AudioFrame frame = audioPlayer.provide();
        if (frame == null) return false;

        buffer = ByteBuffer.wrap(frame.getData());
        return true;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}