package nastia.somnusAuth.authorization.repository;

import nastia.somnusAuth.authorization.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByUserId(long userId);

    Optional<Avatar> findByImagePath(String imagePath);

}
