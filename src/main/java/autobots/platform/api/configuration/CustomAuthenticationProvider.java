package autobots.platform.api.configuration;

import autobots.platform.api.logging.LogsService;
import autobots.platform.api.users.User;
import autobots.platform.api.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LogsService logsService;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();

        User user = usersRepository.getByUsername(username);

        if (user == null) {

            throw new BadCredentialsException("User doesn't exist");

        } else if (user.getPassword().equals(password)) {

            logsService.create(user.getUuid(), "LOGIN_SUCCESSFUL", "User logged in.");

            user.setStampLastLogin(LocalDateTime.now());

            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        } else {

            logsService.create(user.getUuid(), "LOGIN_BAD_PASSWORD", "The user tried to login with the password \"" + password + "\" and failed.");

            throw new BadCredentialsException("External system authentication failed");

        }

    }

    @Override
    public boolean supports(Class<?> auth) {

        return auth.equals(UsernamePasswordAuthenticationToken.class);

    }

}
