package nastia.somnusDreamComment.Comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentOutView {
    private long Id;
    private Long userId;
    private String authorUserName;
    private String commentText;
    private final LocalDateTime timeCreation = LocalDateTime.now();
}
