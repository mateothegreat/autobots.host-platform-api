package autobots.platform.api.bots;

import autobots.platform.api.common.Patterns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping(value = Patterns.UUIDv4)
    public ResponseEntity<Bot> getByUUIDandPrincipal(@PathVariable("uuid") UUID uuid, Principal principal) {

        Optional<Bot> optionalBot = botsService.getByUUIDandPrincipal(uuid, principal);

        return optionalBot.map(bot -> new ResponseEntity<>(bot, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping(value = Patterns.UUIDv4)
    public ResponseEntity<Boolean> deleteByUUIDandPrincipal(@PathVariable("uuid") UUID uuid, Principal principal) {

        botsService.deleteByUUIDandPrincipal(uuid, principal);

        return new ResponseEntity<>(true, HttpStatus.OK);

    }

    @PostMapping(value = Patterns.UUIDv4)
    public ResponseEntity<Bot> update(@PathVariable("uuid") UUID uuid, @RequestBody Bot createBot, Principal principal) {

        Optional<Bot> optionalBot = botsService.update(uuid, createBot, principal);

        return optionalBot.map(bot -> new ResponseEntity<>(bot, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @PostMapping
    public ResponseEntity<Bot> create(@RequestBody Bot createBot, Principal principal) {

        Optional<Bot> optionalBot = botsService.create(createBot, principal);

        return optionalBot.map(bot -> new ResponseEntity<>(bot, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @PostMapping("/{uuid:[0-9a-fxA-FX]{8}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{12}}/deploy")
    public ResponseEntity<Boolean> deploy(@PathVariable("uuid") UUID uuid, Principal principal) {

        Optional<Boolean> optionalBoolean = botsService.deployByUUID(uuid, principal);

        return optionalBoolean.map(b -> new ResponseEntity<>(true, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
