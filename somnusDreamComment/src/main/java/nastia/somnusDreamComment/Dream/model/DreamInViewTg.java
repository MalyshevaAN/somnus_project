package nastia.somnusDreamComment.Dream.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DreamInViewTg {
    private String text;
    private Long authorId;
    private String authorUsername;
}
