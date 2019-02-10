package autobots.platform.api.bots;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bots")
public class BotsController {

    private final BotsService botsService;

    public BotsController(final BotsService botsService) {

        this.botsService = botsService;

    }

}
