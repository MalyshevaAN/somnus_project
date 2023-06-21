package nastia.somnusDreamComment.Comment;


import nastia.somnusDreamComment.Comment.service.CommentService;
import nastia.somnusDreamComment.Comment.service.CommentServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentServiceConfig {
    @Bean
    public CommentServiceInterface getService() {
        return new CommentService();
    }
}
