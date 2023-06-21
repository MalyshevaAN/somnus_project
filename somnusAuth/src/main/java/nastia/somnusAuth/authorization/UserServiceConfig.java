package nastia.somnusAuth.authorization;

import nastia.somnusAuth.authorization.service.UserService;
import nastia.somnusAuth.authorization.service.UserServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig {
    @Bean
    public UserServiceInterface getUserService() {
        return new UserService();
    }
}
