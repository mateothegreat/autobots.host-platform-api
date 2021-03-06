package autobots.platform.api.bots;

import autobots.platform.api.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BotsRepository extends PagingAndSortingRepository<Bot, Long> {

    Page<Bot> getByUser(User user, Pageable pageable);

    Optional<Bot> getByUuidAndUser(UUID uuid, User user);

    int deleteByUuidAndUser(UUID uuid, User user);

}
