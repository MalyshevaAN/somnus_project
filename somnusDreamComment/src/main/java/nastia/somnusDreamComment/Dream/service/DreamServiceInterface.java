package nastia.somnusDreamComment.Dream.service;


import nastia.somnusDreamComment.Dream.exception.MyDreamException;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamInViewTg;
import nastia.somnusDreamComment.Dream.model.DreamOutView;

import java.util.*;


public interface DreamServiceInterface {
    Optional<Dream> getDreamById(long dreamId);

    DreamOutView readDream(long dreamId) throws MyDreamException;

    DreamOutView addDream(DreamInView dreamInView, Long authorId, String authorUsername);

    DreamOutView addDreamTg(DreamInViewTg dreamInViewTg);

    DreamOutView updateDream(DreamInView dreamUpdate, Long authorId, long dreamId) throws MyDreamException;

    void deleteDream(long dreamId, long userId) throws MyDreamException;

    List<DreamOutView> getAllDreams();

    DreamOutView getRandomDream() throws MyDreamException;

    List<DreamOutView> getUserDreams(long authorId);

    DreamOutView likeDream(long dreamId, long userId, boolean like) throws MyDreamException;

}
