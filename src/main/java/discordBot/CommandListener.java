package discordBot;
import discordBot.SteJoBott;
import discordBot.music.PlayerManager;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;


public class CommandListener extends ListenerAdapter {
    private final CommandManager commandManager;
    private final SteJoBott bot;
    private final String prefix = "!";

    public CommandListener(SteJoBott bot) {
        this.bot = bot;
        this.commandManager = new CommandManager();
    }
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            String raw = event.getMessage().getContentRaw();
            System.out.println("Received message: " + raw);

            if (event.getAuthor().isBot() || event.getChannel().getType().isThread()) return;
            if (!raw.startsWith(prefix)) return;

            String[] args = raw.substring(prefix.length()).split("\\s+");
            String commandName = args[0].toLowerCase();

            if(commandManager.hasCommand(commandName)){
                commandManager.getCommand(commandName).execute(event, args, bot);
            }
            if (raw.startsWith("!play")) {
                String[] parts = raw.split("\\s+", 2);
                if (parts.length < 2) {
                    event.getChannel().sendMessage("You need to give me a URL!").queue();
                    return;
                }

                String url = parts[1];
                AudioFrame vc = (AudioFrame) Objects.requireNonNull(Objects.getVoiceState()).getChannel();
                if (vc == null) {
                    event.getChannel().sendMessage("You need to be in a voice channel first!").queue();
                    return;
                }

                AudioManager audioManager = event.getGuild().getAudioManager();
                audioManager.openAudioConnection((AudioChannel) vc);
                PlayerManager.getInstance().loadAndPlay(event.getGuild(), url);
                event.getChannel().sendMessage("Playing: " + url).queue();
            }

        }

}
