package nastia.somnusDreamComment.Dream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DreamOutView {

    private Long Id;
    private String dreamText;
    private LocalDateTime dateCreation;
    private Long authorId;
    private String authorUsername;
    private Long comments;
    private Set<Long> likes = new HashSet<>();
}
