package nastia.somnusDreamComment.Dream.checkAuthorization;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {
    public JwtAuthenticationDreams getAuthInfo() {
        return (JwtAuthenticationDreams) SecurityContextHolder.getContext().getAuthentication();
    }
}
