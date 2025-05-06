package discordBot;
import discordBot.commands.Command;
import java.util.HashMap;
import java.util.Map;
import discordBot.commands.ShutdownCommand;
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager() {
        addCommand(new ShutdownCommand());
 }
    public void addCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
 }
    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
 }
    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
 }
}