package autobots.platform.api.organizations;

import autobots.platform.api.organizations.links.OrganizationLinksService;
import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationsService {

    private OrganizationsRepository  organizationsRepository;
    private OrganizationLinksService organizationLinksService;

    private UsersService usersService;

    @Autowired
    public OrganizationsService(final OrganizationsRepository organizationsRepository, final OrganizationLinksService organizationLinksService, final UsersService usersService) {

        this.organizationsRepository = organizationsRepository;
        this.organizationLinksService = organizationLinksService;
        this.usersService = usersService;

    }

    public Optional<Organization> getById(Long id) {

        return organizationsRepository.findById(id);

    }

    public List<Organization> getAllByPrincipalOrganization(Principal principal, Pageable pageable) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return organizationsRepository._getByParentId(principalUser.get().getOrganization().getId());

        }

        return new ArrayList<>(0);

    }

    public boolean ifOrganizationIdBelongsToPrincipal(Long id, Principal principal) {

        List<Organization> organizations = getAllByPrincipalOrganization(principal, Pageable.unpaged());

        for (int i = 0; i < organizations.size(); i++) {

            if (organizations.get(i).getId().equals(id)) {

                return true;

            }

        }

        return false;

    }

    public Optional<Organization> createByPrincipal(Organization organization, Principal principal) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Organization createdOrganization = organizationsRepository.save(organization);

            organizationLinksService.createLink(principalUser.get().getOrganization(), createdOrganization);

            return Optional.of(createdOrganization);

        }

        return Optional.empty();

    }

    @Transactional
    public Optional<Organization> getByIdAndPrincipal(Long id, Principal principal) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return organizationsRepository._getByChildIdAndParentId(id, principalUser.get().getOrganization().getId());

        }

        return Optional.empty();

    }

    @Transactional
    public Optional<Organization> updateByIdAndPrincipal(Long id, Organization organization, Principal principal) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Optional<Organization> getOrganization = getByIdAndPrincipal(id, principal);

            if (getOrganization.isPresent()) {

                getOrganization.get().setName(organization.getName());
                getOrganization.get().setDescription(organization.getDescription());
                getOrganization.get().setStatus(organization.getStatus());

                return Optional.of(organizationsRepository.save(getOrganization.get()));

            }

        }

        return Optional.empty();

    }

    public void deleteByIdAndPrincipal(Long id, Principal principal) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Optional<Organization> getOrganization = getByIdAndPrincipal(id, principal);

            if (getOrganization.isPresent()) {

                organizationLinksService.deleteByChildAndParent(getOrganization.get(), principalUser.get().getOrganization());

                organizationsRepository.deleteById(id);


            }

        }

    }

}
