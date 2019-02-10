package autobots.platform.api.security;

import autobots.platform.api.users.UserLogin;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.util.Collections.emptyList;

public class TokenAuthenticationService {

    static final long   EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET         = "ThisIsASecret";

    static final String TOKEN_PREFIX  = "Bearer";
    static final String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse res, String username) {

        String jwt = UserLogin.getJWT(username, UserLogin.ONE_DAY_MILLIS);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);

        try {

            res.setContentType("application/json");

            PrintWriter out = res.getWriter();

            out.println("{");
            out.println("\"jwt\":" + "\"" + jwt + "\"");
            out.println("}");

            out.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    static Authentication getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null) {

            // parse the token.
            String user = Jwts.parser().setSigningKey(UserLogin.SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();

            return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;

        }

        return null;

    }

}
