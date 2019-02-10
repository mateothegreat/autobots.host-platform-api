package autobots.platform.api.roles;

import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class RolesService {

    private RolesRepository rolesRepository;
    private UsersRepository usersRepository;

    public RolesService(final RolesRepository rolesRepository, final UsersRepository usersRepository) {

        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;

    }

    public boolean userHasRole(User user, String roleName) {

//        List<Role> roles = rolesRepository.getByUser(user);

        if (user.getEmail().equals("matthew@matthewdavis.io")) {

            return true;

        } else {

            return false;

        }

    }

    public boolean userHasRole(Principal principal, String roleName) {

//        List<Role> roles = rolesRepository.getByUser(user);

        User user = usersRepository.getByUsername(principal.getName());

        if (user != null) {

            if (user.getEmail().equals("matthew@matthewdavis.io")) {

                return true;

            }

        }

        return false;

    }

}
