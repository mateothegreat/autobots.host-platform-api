package autobots.platform.api.bots.environment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotEnvironmentsRepository extends PagingAndSortingRepository<BotEnvironment, Long> {

}
