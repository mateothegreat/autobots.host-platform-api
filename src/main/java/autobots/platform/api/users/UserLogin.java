package autobots.platform.api.users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {

    private String email;
    private String password;

    public static final long   ONE_DAY_MILLIS = 864_000_000; // 1 day
    public static final String SECRET         = "53f6q5465f5v68af454v6a543v514va45651v0a.00.a052.a5.0v.3.5v5a3456va460406455464054v6543254v";

    public static String getJWT(String subject, Long secondsUntilExpire) {

        return Jwts.builder().setSubject(subject).setExpiration(new Date(System.currentTimeMillis() + secondsUntilExpire)).signWith(SignatureAlgorithm.HS512, SECRET).compact();

    }

}
