package autobots.platform.api.users;

import autobots.platform.api.common.RequestResult;
import autobots.platform.api.common.Status;
import autobots.platform.api.logging.LogsService;
import autobots.platform.api.organizations.Organization;
import autobots.platform.api.organizations.OrganizationsRepository;
import autobots.platform.api.organizations.links.OrganizationLinksService;
import autobots.platform.api.roles.Role;
import autobots.platform.api.roles.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Value("${BASE_URL:http://localhost:4204}")
    private String baseUrl;

    private       UsersRepository          repository;
    private       OrganizationsRepository  organizationsRepository;
    private       BCryptPasswordEncoder    bCryptPasswordEncoder;
    private       UsersService             usersService;
    private       LogsService              logsService;
    private       RolesService             rolesService;
    private final OrganizationLinksService organizationLinksService;

    @Autowired
    public UsersController(final UsersRepository repository, final OrganizationsRepository organizationsRepository, final UsersService usersService, final LogsService logsService, final RolesService rolesService, final OrganizationLinksService organizationLinksService) {

        this.repository = repository;
        this.organizationsRepository = organizationsRepository;
        this.usersService = usersService;
        this.logsService = logsService;
        this.rolesService = rolesService;
        this.organizationLinksService = organizationLinksService;

    }

    @GetMapping("/byorg")
    public ResponseEntity<?> getAll(Principal principal, Pageable pageable) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            if (principalUser.get().getPermissionUsersManage() != null && principalUser.get().getPermissionUsersManage()) {

                return new ResponseEntity<>(repository.getByOrganization_id(principalUser.get().getOrganization().getId(), pageable), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }

        } else {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id, Principal principal) {

        Optional<User> user = usersService.getByPrincipalOrganizationAndId(principal, id);

        if (user.isPresent()) {

            return new ResponseEntity<>(user.get(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

//    @GetMapping(path = "/{id}/logs")
//    public ResponseEntity<?> getLogsById(@PathVariable("id") Long id, Pageable pageable, Principal principal) {
//
//        if (rolesService.userHasRole(principal, Role.ROLE_ADMIN)) {
//
//            try {
//
//                Optional<User> user = repository.findById(id);
//
//                if (user.isPresent()) {
//
//                    return new ResponseEntity<>(logsService.getByUser(user.get(), pageable), HttpStatus.OK);
//
//                } else {
//
//                    return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
//
//                }
//
//            } catch (EmptyResultDataAccessException ex) {
//
//                return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
//
//            }
//
//        } else {
//
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//
//        }
//
//    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {

        Optional<User> user = usersService.getByEmailAndPassword(userLogin.getEmail(), userLogin.getPassword());

        if (user.isPresent()) {

            if (user.get().getStatus().equals(Status.ACTIVE)) {

//                logsService.create(user.get().getUuid(), "LOGIN_SUCCESSFUL", "User logged in.");

                user.get().setStampLastLogin(LocalDateTime.now());

                repository.save(user.get());

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK, UserLogin.getJWT(userLogin.getEmail(), UserLogin.ONE_DAY_MILLIS)), HttpStatus.OK);

            } else if (user.get().getStatus().equals(Status.PENDING)) {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Your email address has not been confirmed. Please check your email or contact us.", Status.PENDING), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Invalid email address and/or password."), HttpStatus.OK);

            }

        } else {

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Invalid email address and/or password."), HttpStatus.OK);

        }

    }

    @PostMapping()
    public ResponseEntity<?> createByAdmin(@RequestBody User user, Principal principal) {

        if (rolesService.userHasRole(principal, Role.ROLE_ADMIN)) {

            if (repository.getByEmail(user.getEmail()).isPresent()) {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Email address already exists."), HttpStatus.OK);

            } else {

                user.setOrganization(organizationsRepository.findById(1L).get());
                user.setUsername(user.getEmail());
                user.setConfirmEmailToken(UUID.randomUUID().toString());
                user.setToken(UUID.randomUUID());

                User newUser = repository.save(user);

                logsService.create(newUser.getUuid(), "USER_CREATED_BY_ADMIN", "User \"" + user.getEmail() + "\" created with id " + newUser.getId() + " by admin " + principal.getName());

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK, newUser), HttpStatus.OK);

            }

        } else {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> createByRegistration(@RequestBody User user) {

        if (repository.getByEmail(user.getEmail()).isPresent()) {

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Email address already exists."), HttpStatus.OK);

        } else {

            Organization organization = organizationsRepository.save(new Organization());

            user.setOrganization(organization);
            user.setUsername(user.getEmail());
            user.setConfirmEmailToken(UUID.randomUUID().toString());
            user.setToken(UUID.randomUUID());
            user.setStatus(Status.PENDING);

            User newUser = repository.save(user);

            organizationLinksService.createLink(organization, organization);

//            SendgridUtil.sendSignupConfirmTemplate(user.getEmail(), "https://signup.streamnvr.com/confirm/" + user.getConfirmEmailToken());

            logsService.create(newUser.getUuid(), "USER_CREATED_BY_REGISTRATION", "User \"" + user.getEmail() + "\" created with id " + newUser.getId());

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK, newUser), HttpStatus.OK);

        }

    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createByUser(@RequestBody User user, Principal principal) {

        if (repository.getByEmail(user.getEmail()).isPresent()) {

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Email address already exists."), HttpStatus.OK);

        } else {

            Optional<User> principalUser = usersService.getPrincipalUser(principal);

            if (principalUser.isPresent()) {

                user.setOrganization(principalUser.get().getOrganization());
                user.setUsername(user.getEmail());
                user.setConfirmEmailToken(UUID.randomUUID().toString());
                user.setToken(UUID.randomUUID());
                user.setUuid(UUID.randomUUID());
                user.setStatus(Status.ACTIVE);

                User newUser = repository.save(user);

                logsService.create(newUser.getUuid(), "USER_CREATED_BY_USER", "User \"" + user.getEmail() + "\" created with id " + newUser.getId());

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK, newUser), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Could not locate principal."), HttpStatus.OK);

            }

        }

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Principal principal) {

        return new ResponseEntity<>(usersService.deleteByPrincipalOrganizationAndId(principal, id), HttpStatus.OK);

    }

    @GetMapping(path = "/my")
    public ResponseEntity<User> getCurrentUser(Principal principal) {

        return new ResponseEntity<>(repository.getByEmail(principal.getName()).get(), HttpStatus.OK);

    }

    @PostMapping(path = "/confirm/new")
    public ResponseEntity<?> confirmNewRequest(@RequestBody UserResetRequest userResetRequest) {

        Optional<User> user = repository.getByEmail(userResetRequest.email);

        if (user.isPresent()) {

            UUID uuid = UUID.randomUUID();

            user.get().setConfirmEmailToken(uuid.toString());

            repository.save(user.get());

            logsService.create(user.get().getUuid(), "USER_CONFIRM_REQUESTED", "User requested a confirm email for  " + user.get().getEmail());


//            MandrillUtil.sendConfirmEmail(userResetRequest.getEmail(), baseUrl + "/login/confirm/" + uuid.toString());

        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(path = "/confirm/submit")
    public ResponseEntity<?> emailConfirm(@RequestBody UserPasswordResetConfirm userPasswordResetConfirm) {

        Optional<User> user = repository.getByConfirmEmailToken(userPasswordResetConfirm.getToken());

        if (user.isPresent()) {

//            user.get().setConfirmEmailToken(null);
            user.get().setIsConfirmed(true);
            user.get().setStatus(Status.ACTIVE);

            repository.save(user.get());

            logsService.create(user.get().getUuid(), "USER_CONFIRM_SUBMITTED", "User confirmed email for  " + user.get().getEmail());

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR), HttpStatus.OK);

        }

    }

    @PostMapping(path = "/reset/new")
    public ResponseEntity<?> resetNewRequest(@RequestBody UserResetRequest userResetRequest) {

        Optional<User> user = repository.getByEmail(userResetRequest.email);

        if (user.isPresent()) {

            UUID uuid = UUID.randomUUID();

            user.get().setPasswordResetToken(uuid.toString());

            repository.save(user.get());

            logsService.create(user.get().getUuid(), "USER_RESET_REQUESTED", "User requested a password reset for  " + user.get().getEmail());

//            MandrillUtil.sendPasswordReset(userResetRequest.getEmail(), baseUrl + "/login/reset/" + uuid.toString());

        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(path = "/reset/submit")
    public ResponseEntity<?> resetConfirm(@RequestBody UserPasswordResetConfirm userPasswordResetConfirm) {

        Optional<User> user = repository.getByPasswordResetToken(userPasswordResetConfirm.getToken());

        if (user.isPresent()) {

            user.get().setPasswordResetToken("");
            user.get().setPassword(userPasswordResetConfirm.getPassword());
            user.get().setIsConfirmed(true);

            repository.save(user.get());

            logsService.create(user.get().getUuid(), "USER_RESET_SUBMITTED", "User completed a password reset for  " + user.get().getEmail());

            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping(path = "/my/password")
    public ResponseEntity<User> changePassword(@RequestBody UserPassword userPassword, Principal principal) {

        Optional<User> user = usersService.getPrincipalUser(principal);

        user.get().setPassword(userPassword.getPassword());

        logsService.create(user.get().getUuid(), "USER_PASSWORD_CHANGE", "User changed password");

        return new ResponseEntity<>(repository.save(user.get()), HttpStatus.OK);

    }

    @PutMapping(path = "/my")
    public ResponseEntity<User> update(@RequestBody User user, Principal principal) {

        return new ResponseEntity<>(usersService.updatePrincipalUser(user, principal).get(), HttpStatus.OK);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateByUserId(@PathVariable("id") Long id, @RequestBody User user, Principal principal) {

        return new ResponseEntity<>(usersService.updateUserByPrincipal(id, user, principal).get(), HttpStatus.OK);

    }

}
