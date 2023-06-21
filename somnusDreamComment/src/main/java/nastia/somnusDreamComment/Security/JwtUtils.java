package nastia.somnusDreamComment.Security;

import io.jsonwebtoken.Claims;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import nastia.somnusDreamComment.Dream.checkAuthorization.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUtils {

    public static JwtAuthenticationDreams generate(Claims claims) {
        final JwtAuthenticationDreams jwtInfoToken = new JwtAuthenticationDreams();
        jwtInfoToken.setId(claims.get("id", Long.class));
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setAuthorUserName(claims.get("userName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }
}
