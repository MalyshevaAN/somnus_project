package nastia.somnusAuth.authorization;


import nastia.somnusAuth.authorization.service.AuthService;
import nastia.somnusAuth.authorization.service.AuthServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceConfig {
    @Bean
    public AuthServiceInterface getAuthService() {
        return new AuthService();
    }

}
