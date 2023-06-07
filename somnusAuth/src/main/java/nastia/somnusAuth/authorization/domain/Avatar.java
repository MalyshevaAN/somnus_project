package nastia.somnusAuth.authorization.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;
    @Column(unique = true)
    private String name;

    private String type;
    @Column(unique = true)
    private String imagePath;
}
