package autobots.platform.api.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogsService {

    private LogsRepository logsRepository;

    @Autowired
    public LogsService(final LogsRepository logsRepository) {

        this.logsRepository = logsRepository;

    }

    public Log create(UUID uuid, String title, String description) {

        Log log = new Log();

        log.setUuid(uuid);
        log.setTitle(title);
        log.setDescription(description);

        return logsRepository.save(log);

    }

//    public Page<Log> getByUser(User user, Pageable pageable) {
//
//        return logsRepository.getByUserOrderByIdDesc(user, pageable);
//
//    }

}
