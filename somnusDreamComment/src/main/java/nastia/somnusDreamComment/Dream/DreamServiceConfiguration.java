package nastia.somnusDreamComment.Dream;

import nastia.somnusDreamComment.Dream.service.DreamService;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DreamServiceConfiguration {
    @Bean
    public DreamServiceInterface getDreamService() {
        return new DreamService();
    }
}
