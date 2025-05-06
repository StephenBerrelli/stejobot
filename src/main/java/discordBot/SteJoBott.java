package discordBot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import io.github.cdimascio.dotenv.Dotenv;
import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.Set;

public class SteJoBott {

    private final ShardManager shardManager;
    private final Set<String> adminIds;


    public SteJoBott() throws LoginException {
        // Load environment variables
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .load();

        String token = dotenv.get("DISCORD_TOKEN");
        String admins = dotenv.get("BOT_ADMINS"); // comma-separated user IDs

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("DISCORD_TOKEN environment variable not set");
        }

        // Parse admin IDs
        if (admins == null || admins.isBlank()) {
            throw new IllegalStateException("BOT_ADMINS environment variable not set");
        }

        adminIds = Set.of(admins.split(","));

        // Enable gateway intents
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES
        );

        // Build the shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token, intents);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.customStatus("Sucking Jon"));

        shardManager = builder.build();
        shardManager.addEventListener(new CommandListener(this));
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Set<String> getAdminIds() {
        return adminIds;
    }

    public static void main(String[] arguments) {
        try {
            new SteJoBott();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
