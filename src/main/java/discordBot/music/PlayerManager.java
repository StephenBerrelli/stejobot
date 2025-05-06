package discordBot.music;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    private PlayerManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);  // Register remote sources like YouTube, SoundCloud
        AudioSourceManagers.registerLocalSource(playerManager);     // Register local file sources
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager manager = new GuildMusicManager(playerManager);
            AudioPlayerSendHandler sendHandler = new AudioPlayerSendHandler(manager.player);
            guild.getAudioManager().setSendingHandler(sendHandler);  // Set the send handler here
            return manager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        Guild guild = channel.getGuild();
        GuildMusicManager musicManager = getGuildMusicManager(guild);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue: `" + track.getInfo().title + "`").queue();
                play(channel, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);  // Get the first track from the playlist
                }

                channel.sendMessage("Adding to queue: `" + firstTrack.getInfo().title + "` from playlist `" + playlist.getName() + "`").queue();
                play(channel, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("❌ Could not find any matches for: `" + trackUrl + "`").queue();
                System.out.println("No matches found for URL: " + trackUrl);  // Debugging
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("❌ Failed to load track: `" + exception.getMessage() + "`").queue();
                exception.printStackTrace();  // Log the full stack trace for debugging
                System.out.println("Failed to load track from URL: " + trackUrl);
            }
        });
    }

    private void play(TextChannel channel, GuildMusicManager musicManager, AudioTrack track) {
        connectToAudioChannel(channel.getGuild(), musicManager);  // Ensure bot connects to the voice channel
        playTrack(channel, musicManager, track);
    }

    private void connectToAudioChannel(Guild guild, GuildMusicManager musicManager) {
        // Ensure the bot is connected to the voice channel
        if (!guild.getAudioManager().isConnected()) {
            AudioChannel voiceChannel = guild.getVoiceChannels().get(0);  // Connect to the first voice channel available
            guild.getAudioManager().openAudioConnection(voiceChannel);
        }
    }

    private void playTrack(TextChannel channel, GuildMusicManager musicManager, AudioTrack track) {
        AudioPlayer player = musicManager.player;
        player.playTrack(track);  // Start playing the track

        channel.sendMessage("Now playing: `" + track.getInfo().title + "`").queue();
    }

    public void testLoadAndPlay(TextChannel channel) {
        // Test URL for loading and playing
        String testUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";  // A valid YouTube URL

        // Now call loadAndPlay with this URL
        loadAndPlay(channel, testUrl);
    }
}
