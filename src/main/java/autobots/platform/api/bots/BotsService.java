package autobots.platform.api.bots;

import autobots.platform.api.messaging.Message;
import autobots.platform.api.messaging.MessagingService;
import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class BotsService {

    private final BotsRepository botsRepository;

    private final UsersService     usersService;
    private final MessagingService messagingService;

    public BotsService(final BotsRepository botsRepository, final UsersService usersService, final MessagingService messagingService) {

        this.botsRepository = botsRepository;
        this.usersService = usersService;
        this.messagingService = messagingService;

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

    @Transactional
    public boolean deleteByUUIDandPrincipal(UUID uuid, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            botsRepository.deleteByUuidAndUser(uuid, optionalUser.get());

            return true;

        }

        return false;

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

    public Optional<Bot> update(UUID uuid, Bot bot, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            Optional<Bot> optionalBot = botsRepository.getByUuidAndUser(uuid, optionalUser.get());

            if (optionalBot.isPresent()) {

                optionalBot.get().setName(bot.getName());
                optionalBot.get().setDescription(bot.getDescription());
                optionalBot.get().setGitUrl(bot.getGitUrl());
                optionalBot.get().setImage(bot.getImage());
                optionalBot.get().setStatus(bot.getStatus());
                optionalBot.get().setEnvironments(bot.getEnvironments());

                return Optional.of(botsRepository.save(optionalBot.get()));

            }

        }

        return Optional.empty();

    }

    public Optional<Boolean> deployByUUID(UUID uuid, Principal principal) {

        Optional<User> optionalUser = usersService.getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            Optional<Bot> optionalBot = botsRepository.getByUuidAndUser(uuid, optionalUser.get());

            if (optionalBot.isPresent()) {

                messagingService.send(new Message<>(optionalBot.get().getUuid(), "deploy", new BotDeploy(optionalBot.get().getUuid(), optionalBot.get().getGitUrl(), optionalBot.get().getEnvironments())));

                return Optional.of(true);

            }

        }

        return Optional.empty();

    }

}
