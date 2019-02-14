package autobots.platform.api.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.stereotype.Service;

@Service
public class DiscordBotService {

    public void doStuff() {

        DiscordApi api = new DiscordApiBuilder().setToken("NTQ0MDA1NTI0NjQ4MDk5ODQw.D0E1Eg.dyGcuNCswY0rS_lanLnc0r2d9o4").login().join();

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {

            if (event.getMessageContent().equalsIgnoreCase("!ping")) {

                event.getChannel().sendMessage("Pong!");

            }

        });

    }

}
