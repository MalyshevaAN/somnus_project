package nastia.somnusDreamComment.Dream.service;



import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Dream.exception.DreamNotFoundException;
import nastia.somnusDreamComment.Dream.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamOutView;

import java.util.*;


public interface DreamServiceInterface {
    Optional<Dream> getDreamById(long dreamId);

    Optional<DreamOutView> readDream(long dreamId) throws DreamNotFoundException;

    Optional<DreamOutView> addDream(DreamInView dreamInView, Long authorId);

    Optional<DreamOutView> updateDream(DreamInView dreamUpdate, Long authorId, long dreamId) throws DreamNotFoundException, UserHaveNoRights;

    void deleteDream(long dreamId, long userId) throws DreamNotFoundException, UserHaveNoRights;

    List<DreamOutView> getAllDreams();
    Optional<DreamOutView> getRandomDream();

    List<DreamOutView> getUserDreams(long authorId);

    Optional<DreamOutView> likeDream(long dreamId, long userId, boolean like) throws DreamNotExistsException;
}
