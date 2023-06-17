package nastia.somnusDreamComment.Comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nastia.somnusDreamComment.Dream.model.Dream;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;


    private Long authorId;

    private String authorUserName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dream_id", nullable = false)
    @JsonIgnore
    private Dream dream;

    private String commentText;
    
    @Basic
    private final LocalDateTime timeCreation = LocalDateTime.now();
}
