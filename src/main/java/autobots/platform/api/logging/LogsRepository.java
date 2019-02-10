package autobots.platform.api.logging;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends PagingAndSortingRepository<Log, Long> {

//    Page<Log> getByUserOrderByIdDesc(User user, Pageable pageable);

}
