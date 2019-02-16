package autobots.platform.api.bots;

import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class BotsService {

    private final BotsRepository botsRepository;

    private final UsersService usersService;

    public BotsService(final BotsRepository botsRepository, final UsersService usersService) {

        this.botsRepository = botsRepository;
        this.usersService = usersService;

    }

    public Page<Bot> getPageableByPrincipalUser(Pageable pageable, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            return botsRepository.getByUser(optionalUser.get(), pageable);

        }

        return Page.empty();

    }

    public Optional<Bot> getByUUIDandPrincipal(UUID uuid, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            return botsRepository.getByUuidAndUser(uuid, optionalUser.get());

        }

        return Optional.empty();

    }

    public Optional<Bot> create(Bot bot, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            bot.setUser(optionalUser.get());
            bot.setUuid(UUID.randomUUID());

            return Optional.of(botsRepository.save(bot));

        }

        return Optional.empty();

    }

}
