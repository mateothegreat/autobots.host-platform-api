package autobots.platform.api.users;

import autobots.platform.api.organizations.Organization;
import autobots.platform.api.organizations.OrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    @Lazy
    private OrganizationsService organizationsService;

    public Optional<User> updatePrincipalUser(User user, Principal principal) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            principalUser.get().setEmail(user.getEmail());
            principalUser.get().setFirstname(user.getFirstname());
            principalUser.get().setLastname(user.getLastname());

            return Optional.of(usersRepository.save(principalUser.get()));

        }

        return Optional.empty();

    }

    public Optional<User> updateUserByPrincipal(Long id, User user, Principal principal) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Optional<User> optionalUser = getByPrincipalOrganizationAndId(principal, id);

            if (optionalUser.isPresent()) {

                if (user.getEmail() != null) {

                    optionalUser.get().setEmail(user.getEmail());

                }

                if (user.getFirstname() != null) {

                    optionalUser.get().setFirstname(user.getFirstname());

                }

                if (user.getLastname() != null) {

                    optionalUser.get().setLastname(user.getLastname());

                }

                if (user.getPassword() != null) {

                    optionalUser.get().setPassword(user.getPassword());

                }

                if (user.getFirstname() != null) {

                    optionalUser.get().setFirstname(user.getFirstname());

                }

                if (user.getLastname() != null) {

                    optionalUser.get().setLastname(user.getLastname());

                }

                if (user.getPermissionUsersManage() != null) {

                    optionalUser.get().setPermissionUsersManage(user.getPermissionUsersManage());

                }

                return Optional.of(usersRepository.save(optionalUser.get()));

            }

        }

        return Optional.empty();

    }

    public Optional<User> getPrincipalUser(Principal principal) {

        return usersRepository.getByEmail(principal.getName());

    }

    public Optional<User> getByUserId(Long userId) {

        return usersRepository.findById(userId);

    }

    public List<User> getByOrganization(Organization organization) {

        return usersRepository.getByOrganization(organization);

    }

    public Optional<User> getByPrincipalOrganizationAndId(Principal principal, Long userId) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return usersRepository.getByOrganizationAndId(principalUser.get().getOrganization(), userId);

        }

        return Optional.empty();

    }

    public int deleteByPrincipalOrganizationAndId(Principal principal, Long userId) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return usersRepository.deleteByOrganizationAndId(principalUser.get().getOrganization(), userId);

        }

        return 0;

    }

    public Optional<User> getByEmailAndPassword(String email, String password) {

        return usersRepository.getByEmailAndPassword(email, password);

    }

    public User create(User user) {

        return usersRepository.save(user);

    }

    public Optional<User> getUserByPrincipalAndIsAdmin(Principal principal) {

        Optional<User> user = getPrincipalUser(principal);

        if (user.isPresent()) {

            if (user.get().getEmail().equals("root@streamnvr.com")) {

                return user;

            }

        }

        return Optional.empty();

    }

}
