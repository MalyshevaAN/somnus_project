package nastia.somnusAuth.authorization;

import nastia.somnusAuth.authorization.service.AvatarService;
import nastia.somnusAuth.authorization.service.AvatarServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvatarServiceConfig {
    @Bean
    public AvatarServiceInterface getAvatarService() {
        return new AvatarService();
    }
}
