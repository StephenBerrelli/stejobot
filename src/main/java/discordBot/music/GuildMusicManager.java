package discordBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import java.nio.ByteBuffer;

public class GuildMusicManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    // Constructor initializes the audio player and the track scheduler.
    public GuildMusicManager(AudioPlayerManager manager) {
        this.player = manager.createPlayer();  // Create a new audio player
        this.scheduler = new TrackScheduler(player);  // Create the track scheduler
        this.player.addListener(scheduler);  // Add the scheduler as a listener to the audio player
    }

    // This method returns the custom AudioSendHandler to send audio to Discord.
    public AudioSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);  // Return an AudioPlayerSendHandler for this guild
    }
}
