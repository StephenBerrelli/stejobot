package discordBot.commands;

import discordBot.SteJoBott;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
       void execute(MessageReceivedEvent event, String[] args, SteJoBott bot);
       String getName();
}
