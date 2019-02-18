package autobots.platform.api.bots;

import autobots.platform.api.bots.environment.BotEnvironment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BotDeploy {

    private UUID                 uuid;
    private String               gitUrl;
    private List<BotEnvironment> environments;

}
