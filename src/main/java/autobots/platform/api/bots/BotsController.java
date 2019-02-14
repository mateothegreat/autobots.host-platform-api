package autobots.platform.api.bots;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/bots")
public class BotsController {

    private final BotsService botsService;

    public BotsController(final BotsService botsService) {

        this.botsService = botsService;

    }

    @GetMapping
    public ResponseEntity<Page<Bot>> getPageable(Pageable pageable, Principal principal) {

        return new ResponseEntity<>(botsService.getPageableByPrincipalUser(pageable, principal), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Bot> create(@RequestBody Bot createBot, Principal principal) {

        Optional<Bot> optionalBot = botsService.create(createBot, principal);

        return optionalBot.map(bot -> new ResponseEntity<>(bot, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

}
