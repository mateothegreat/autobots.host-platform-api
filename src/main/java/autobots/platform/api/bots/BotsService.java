package autobots.platform.api.bots;

import org.springframework.stereotype.Service;

@Service
public class BotsService {

    private final BotsRepository botsRepository;

    public BotsService(final BotsRepository botsRepository) {

        this.botsRepository = botsRepository;

    }

}
