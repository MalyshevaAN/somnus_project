package nastia.somnusDreamComment.Comment.repository;

import nastia.somnusDreamComment.Comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDreamId(long id);
}
