package nastia.somnusDreamComment.Dream.repository;

import nastia.somnusDreamComment.Dream.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface DreamRepository extends JpaRepository<Dream, Long> {
    public Optional<Dream> findFirstByOrderByIdDesc();

    public Optional<List<Dream>> findDreamByAuthorId(long id);

}
