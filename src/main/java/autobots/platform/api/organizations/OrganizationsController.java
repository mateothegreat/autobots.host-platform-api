package autobots.platform.api.organizations;

import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
public class OrganizationsController {

    private OrganizationsRepository repository;
    private OrganizationsService    organizationsService;
    private UsersRepository         usersRepository;

    @Autowired
    public OrganizationsController(final OrganizationsRepository repository, final OrganizationsService organizationsService, final UsersRepository usersRepository) {

        this.repository = repository;
        this.organizationsService = organizationsService;
        this.usersRepository = usersRepository;

    }

    @GetMapping()
    public Page<Organization> getAllOrganizations(Pageable pageable) {

        return repository.findAll(pageable);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Organization> getById(@PathVariable("id") Long id) {

        try {

            Optional<Organization> Organization = repository.findById(id);

            if (Organization.isPresent()) {

                return new ResponseEntity<>(Organization.get(), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new Organization(), HttpStatus.NOT_FOUND);

            }

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(new Organization(), HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping
    public ResponseEntity<Organization> create(@RequestBody Organization entity) {

        return new ResponseEntity<>(repository.save(entity), HttpStatus.OK);

    }

    @PostMapping("/my")
    public ResponseEntity<Organization> createByPrincipal(@RequestBody Organization organization, Principal principal) {

        return new ResponseEntity<>(organizationsService.createByPrincipal(organization, principal).get(), HttpStatus.OK);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Organization> update(@PathVariable("id") Long id, @RequestBody Organization entity) {

        entity.setId(id);

        return new ResponseEntity<>(repository.save(entity), HttpStatus.OK);

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

    @GetMapping(path = "/{id}/motiva.datalink.platform.api.organizations.users")
    public Page<User> getUsersByOrganizationId(@PathVariable("id") Long organizationId, Pageable pageable) {

        Page<User> page = this.usersRepository.getByOrganization_id(organizationId, pageable);

        return page;

    }

    @GetMapping(path = "/my/list")
    public ResponseEntity<List<Organization>> getMyOrganizationsByPrincipal(Principal principal, Pageable pageable) {

        return new ResponseEntity<>(organizationsService.getAllByPrincipalOrganization(principal, pageable), HttpStatus.OK);

    }

    @GetMapping(path = "/my/{id}")
    public ResponseEntity<Organization> getByIdAndPrincipal(@PathVariable("id") Long id, Principal principal) {

        Optional<Organization> organization = organizationsService.getByIdAndPrincipal(id, principal);

        if (organization.isPresent()) {

            return new ResponseEntity<>(organizationsService.getByIdAndPrincipal(id, principal).get(), HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(path = "/my/{id}")
    public ResponseEntity<Organization> updateByIdAndPrincipal(@PathVariable("id") Long id, @RequestBody Organization organization, Principal principal) {

        Optional<Organization> updatedOrganization = organizationsService.updateByIdAndPrincipal(id, organization, principal);

        if (updatedOrganization.isPresent()) {

            return new ResponseEntity<>(updatedOrganization.get(), HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping(path = "/my/{id}")
    public ResponseEntity<?> deleteByIdAndPrincipal(@PathVariable("id") Long id, Principal principal) {

        organizationsService.deleteByIdAndPrincipal(id, principal);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
