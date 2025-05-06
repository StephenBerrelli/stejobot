package discordBot;

import discordBot.SteJoBott;
import discordBot.music.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

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

        String message = event.getMessage().getContentRaw();

        if (message.equals("!testplay")) {
            PlayerManager playerManager = PlayerManager.getInstance();

            // Ensure the channel is a TextChannel
            if (event.getChannel() instanceof TextChannel) {
                TextChannel channel = (TextChannel) event.getChannel();
                // Call the test method that loads and plays the test URL
                playerManager.testLoadAndPlay(channel);
            } else {
                event.getChannel().sendMessage("This command can only be used in a text channel.").queue();
            }
        }

        String raw = event.getMessage().getContentRaw();
        System.out.println("Received message: " + raw);

        if (event.getAuthor().isBot() || event.getChannel().getType().isThread()) return;
        if (!raw.startsWith(prefix)) return;

        String[] args = raw.substring(prefix.length()).split("\\s+");
        String commandName = args[0].toLowerCase();

        if (commandManager.hasCommand(commandName)) {
            commandManager.getCommand(commandName).execute(event, args, bot);
        }

        if (raw.startsWith("!play")) {
            String[] parts = raw.split("\\s+", 2);
            if (parts.length < 2) {
                event.getChannel().sendMessage("You need to give me a URL!").queue();
                return;
            }

            String url = parts[1];

            // Ensure the member is in a voice channel
            if (event.getMember() == null || event.getMember().getVoiceState() == null) {
                event.getChannel().sendMessage("You need to be in a voice channel first!").queue();
                return;
            }

            // Debugging: print the user's current voice state
            System.out.println("Member's Voice State: " + event.getMember().getVoiceState());

            AudioChannel channel = event.getMember().getVoiceState().getChannel();

            // Debugging: print the channel the user is in (if any)
            if (channel == null) {
                System.out.println("User is not in a voice channel.");
                event.getChannel().sendMessage("You need to be in a voice channel first!").queue();
                return;
            } else {
                System.out.println("User is in voice channel: " + channel.getName());
            }

            // Ensure the bot has the permissions to join and speak in the channel
            if (!channel.getGuild().getSelfMember().hasPermission(channel, net.dv8tion.jda.api.Permission.VOICE_CONNECT)) {
                event.getChannel().sendMessage("I don't have permission to join this voice channel!").queue();
                return;
            }

            if (!channel.getGuild().getSelfMember().hasPermission(channel, net.dv8tion.jda.api.Permission.VOICE_SPEAK)) {
                event.getChannel().sendMessage("I don't have permission to speak in this voice channel!").queue();
                return;
            }

            // Open audio connection
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(channel);

            // Proceed with loading and playing the music
            if (event.getChannel() instanceof TextChannel) {
                PlayerManager.getInstance().loadAndPlay((TextChannel) event.getChannel(), url);
                event.getChannel().sendMessage("Playing: " + url).queue();
            } else {
                event.getChannel().sendMessage("This command can only be used in a text channel!").queue();
            }
        }


    }
}
