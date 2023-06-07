package nastia.somnusAuth.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "somnusUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private long avatarId;
    private Set<Role> roles = Set.of(Role.USER);

//    @JsonIgnore
//    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
//    private Set<Dream> dreams = new HashSet<>();


//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//    @JsonIgnore
//    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name="subscriber_id")}
    )
    private Set<User> subscribers = new HashSet<>();


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name="channel_id")}
    )
    private Set<User> subscribtions = new HashSet<>();

    public void addSubscription(User subscription){
        this.subscribtions.add(subscription);
    }

    public void deleteSubscription(User subscription){
        if (this.subscribtions.contains(subscription)){
            this.subscribtions.remove(subscription);
        }
    }
}
