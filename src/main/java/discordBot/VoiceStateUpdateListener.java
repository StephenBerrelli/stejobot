package discordBot;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import org.jetbrains.annotations.NotNull;

public class VoiceStateUpdateListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        Member member = event.getMember();

        AudioChannel joined = event.getChannelJoined();
        AudioChannel left = event.getChannelLeft();

        if (joined != null) {
            System.out.println(member.getEffectiveName() + " joined voice channel: " + joined.getName());
        }

        if (left != null) {
            System.out.println(member.getEffectiveName() + " left voice channel: " + left.getName());
        }
    }
}
