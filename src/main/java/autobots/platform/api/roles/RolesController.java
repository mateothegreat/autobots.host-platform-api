package autobots.platform.api.roles;

import autobots.platform.api.organizations.Organization;
import autobots.platform.api.organizations.OrganizationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private RolesRepository         repository;
    private OrganizationsRepository organizationsRepository;

    @Autowired
    public RolesController(final RolesRepository repository, final OrganizationsRepository organizationsRepository) {

        this.repository = repository;
        this.organizationsRepository = organizationsRepository;

    }


    @GetMapping()
    public Page<Role> getAll(Pageable pageable) {

        return repository.findAll(pageable);

    }

//    @GetMapping(path = "/search", params = "terms")
//    public ResponseEntity<List<User>> search(@RequestParam("terms") String terms) {
//
//        return new ResponseEntity<List<User>>(repository.search(terms, ), HttpStatus.OK);
//
//    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Role> getById(@PathVariable("id") Long id) {

        try {

            Optional<Role> role = repository.findById(id);

            if (role.isPresent()) {

                return new ResponseEntity<>(role.get(), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new Role(), HttpStatus.NOT_FOUND);

            }

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(new Role(), HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Role role) {

        Optional<Organization> organization = organizationsRepository.findById(role.organization_id);

        if (organization.isPresent()) {

            role.setOrganization(organization.get());

            return new ResponseEntity<>(repository.save(role), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Role> update(@PathVariable("id") Long id, @RequestBody Role role) {

        role.id = id;

        return new ResponseEntity<>(repository.save(role), HttpStatus.OK);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        try {

            repository.deleteById(id);

            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

}
