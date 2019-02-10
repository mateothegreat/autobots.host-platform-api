package autobots.platform.api.roles.groups;

import autobots.platform.api.organizations.Organization;
import autobots.platform.api.organizations.OrganizationsRepository;
import autobots.platform.api.roles.Role;
import autobots.platform.api.roles.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/roles/groups")
public class RolesGroupsController {

    private RolesRepository         rolesRepository;
    private OrganizationsRepository organizationsRepository;
    private RolesGroupsRepository   rolesGroupsRepository;

    @Autowired
    public RolesGroupsController(final RolesRepository rolesRepository, final OrganizationsRepository organizationsRepository, final RolesGroupsRepository rolesGroupsRepository) {

        this.rolesRepository = rolesRepository;
        this.organizationsRepository = organizationsRepository;
        this.rolesGroupsRepository = rolesGroupsRepository;

    }


    @GetMapping()
    public Page<RoleGroup> getAll(Pageable pageable) {

        return rolesGroupsRepository.findAll(pageable);

    }

//    @GetMapping(path = "/search", params = "terms")
//    public ResponseEntity<List<User>> search(@RequestParam("terms") String terms) {
//
//        return new ResponseEntity<List<User>>(repository.search(terms, ), HttpStatus.OK);
//
//    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RoleGroup> getById(@PathVariable("id") Long id) {

        try {

            Optional<RoleGroup> roleGroup = rolesGroupsRepository.findById(id);

            if (roleGroup.isPresent()) {

                return new ResponseEntity<>(roleGroup.get(), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new RoleGroup(), HttpStatus.NOT_FOUND);

            }

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(new RoleGroup(), HttpStatus.NOT_FOUND);

        }

    }

    @GetMapping(path = "/{id}/roles")
    public ResponseEntity<Page<Role>> getByRolesId(@PathVariable("id") Long id, Pageable pageable) {

        try {

            Optional<RoleGroup> roleGroup = rolesGroupsRepository.findById(id);

            if (roleGroup.isPresent()) {

                return new ResponseEntity<>(rolesRepository.getByRoleGroupsContains(roleGroup.get(), pageable), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(Page.empty(), HttpStatus.NOT_FOUND);

            }

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(Page.empty(), HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleGroup roleGroup) {

        Optional<Organization> organization = organizationsRepository.findById(roleGroup.organization_id);

        if (organization.isPresent()) {

            roleGroup.setOrganization(organization.get());

            return new ResponseEntity<>(rolesGroupsRepository.save(roleGroup), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping(path = "{roleGroupId}/assignRole/{roleId}")
    public ResponseEntity<?> assignRoleToGroup(@PathVariable("roleGroupId") Long roleGroupId, @PathVariable("roleId") Long roleId) {

        Optional<RoleGroup> roleGroup = rolesGroupsRepository.findById(roleGroupId);

        if (roleGroup.isPresent()) {

            Optional<Role> _role = rolesRepository.findById(roleId);

            if (_role.isPresent()) {

                roleGroup.get().getRoles().add(_role.get());

                rolesGroupsRepository.save(roleGroup.get());

                return new ResponseEntity<>(null, HttpStatus.OK);

            } else {

                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<RoleGroup> update(@PathVariable("id") Long id, @RequestBody RoleGroup roleGroup) {

        roleGroup.id = id;

        return new ResponseEntity<>(rolesGroupsRepository.save(roleGroup), HttpStatus.OK);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        try {

            rolesGroupsRepository.deleteById(id);

            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (EmptyResultDataAccessException ex) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

}
