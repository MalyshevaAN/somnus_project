package nastia.somnusAuth.authorization.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOutView {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarPath;
}