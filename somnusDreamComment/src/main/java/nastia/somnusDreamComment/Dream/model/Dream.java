package nastia.somnusDreamComment.Dream.model;

import jakarta.persistence.*;
import lombok.*;
import nastia.somnusDreamComment.Comment.model.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dream")
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String dreamText;

    @Basic
    @Column(name = "DateCreation")
    private final LocalDateTime dateCreation = LocalDateTime.now();


    private Long authorId;

    private String authorUsername;


    @OneToMany(mappedBy = "dream", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    private Set<Long> likes = new HashSet<>();

}