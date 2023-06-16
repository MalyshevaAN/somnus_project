package nastia.somnusDreamComment.Dream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nastia.somnusDreamComment.Comment.model.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DreamOutView {

    private Long Id;
    private String dreamText;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    private Long author;
    private String authorUsername;

    private Set<Comment> comments = new HashSet<>();

    private Set<Long> likes = new HashSet<>();

}
