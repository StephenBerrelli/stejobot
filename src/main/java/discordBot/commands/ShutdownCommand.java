package discordBot.commands;
import discordBot.commands.ShutdownCommand;
import discordBot.SteJoBott;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

    public class ShutdownCommand implements Command {
        public void execute(MessageReceivedEvent event, String[] args, SteJoBott bot) {
            String userId = event.getAuthor().getId();


            if (!bot.getAdminIds().contains(userId)) {
                event.getChannel().sendMessage("Is you Ste??? Is you Jo? haaaaaaa....").queue();
                return;
            }

            event.getChannel().sendMessage("Going back to the sex dungeon...").queue();
            bot.getShardManager().shutdown();
            System.exit(0);
        }

        public String getName() {
            return "shutdown";
        }

}
