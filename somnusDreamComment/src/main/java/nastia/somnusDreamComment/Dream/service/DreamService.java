package nastia.somnusDreamComment.Dream.service;


import nastia.somnusDreamComment.Dream.exception.MyDreamException;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamInViewTg;
import nastia.somnusDreamComment.Dream.model.DreamOutView;
import nastia.somnusDreamComment.Dream.repository.DreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class DreamService implements DreamServiceInterface {

    @Autowired
    DreamRepository dreamRepository;

    public Optional<Dream> getDreamById(long dreamId) {

        return dreamRepository.findById(dreamId);
    }

    public DreamOutView readDream(long dreamId) throws MyDreamException {
        Optional<Dream> dream = getDreamById(dreamId);
        if (dream.isPresent()) {
            return createDreamOutView(dream.get());
        }
        throw new MyDreamException(HttpStatus.NOT_FOUND, "Сон с таким id не найден");
    }

    public DreamOutView addDream(DreamInView dreamInView, Long authorId, String authorUsername) {
        Dream newDream = createDreamView(dreamInView.getDreamText(), authorId, authorUsername);
        return createDreamOutView(dreamRepository.save(newDream));
    }

    @Override
    public DreamOutView addDreamTg(DreamInViewTg dreamInViewTg) {
        Dream newDream = createDreamView(dreamInViewTg.getText(), dreamInViewTg.getAuthorId(), dreamInViewTg.getAuthorUsername());
        return createDreamOutView(dreamRepository.save(newDream));
    }

    public DreamOutView updateDream(DreamInView dreamUpdate, Long authorId, long dreamId) throws MyDreamException {
        Optional<Dream> dream = dreamRepository.findById(dreamId);
        if (dream.isEmpty()) {
            throw new MyDreamException(HttpStatus.NOT_FOUND, "Сон с таким id не найден");
        }
        if (!Objects.equals(dream.get().getAuthorId(), authorId)) {
            throw new MyDreamException(HttpStatus.NOT_ACCEPTABLE, "Нельзя редактировать чужой сон");
        }
        Dream updatedDream = dream.get();
        updatedDream.setDreamText(dreamUpdate.getDreamText());
        updatedDream = dreamRepository.save(updatedDream);
        return createDreamOutView(updatedDream);
    }

    public void deleteDream(long dreamId, long userId) throws MyDreamException {
        Optional<Dream> dream = dreamRepository.findById(dreamId);
        if (dream.isEmpty()) {
            throw new MyDreamException(HttpStatus.NOT_FOUND, "Сон с таким id не найден");
        }
        if (dream.get().getAuthorId() != userId) {
            throw new MyDreamException(HttpStatus.NOT_ACCEPTABLE, "Нельзя удалять чужие сны");
        }
        dreamRepository.delete(dream.get());
    }

    public List<DreamOutView> getAllDreams() {
        List<Dream> allDreams = dreamRepository.findAll();
        return DreamOutList(allDreams);
    }


    public DreamOutView getRandomDream() {
        if (dreamRepository.count() == 0) {
            throw new MyDreamException(HttpStatus.NOT_FOUND, "Еще ни один сон не записан");
        }
        Optional<Dream> lastDream = dreamRepository.findFirstByOrderByIdDesc();
        if (dreamRepository.count() == 1) {
            return createDreamOutView(lastDream.get());
        }

        long lastId = lastDream.get().getId();

        long randId = (long) (Math.random() * lastId + 1);
        while (dreamRepository.findById(randId).isEmpty()) {
            System.out.println(randId);
            randId = (long) (Math.random() * lastId + 1);
        }
        return createDreamOutView(dreamRepository.findById(randId).get());
    }

    public List<DreamOutView> getUserDreams(long authorId) {
        Optional<List<Dream>> usersDreams = dreamRepository.findDreamByAuthorId(authorId);
        if (usersDreams.isPresent()) {
            return DreamOutList(usersDreams.get());
        }
        return new ArrayList<>();
    }

    public DreamOutView likeDream(long dreamId, long userId, boolean like) throws MyDreamException {
        Optional<Dream> dream = dreamRepository.findById(dreamId);
        if (dream.isEmpty()) {
            throw new MyDreamException(HttpStatus.NOT_FOUND, "Сон с таким id не найден");
        }
        Dream dreamLike = dream.get();
        Set<Long> likes = dreamLike.getLikes();
        if (likes.add(userId)) {
            dreamLike.setLikes(likes);
            return createDreamOutView(dreamRepository.save(dreamLike));
        } else {
            likes.remove(userId);
            dreamLike.setLikes(likes);
            return createDreamOutView(dreamRepository.save(dreamLike));
        }
    }

    private DreamOutView createDreamOutView(Dream dream) {
        DreamOutView dreamOutView = new DreamOutView();
        dreamOutView.setDreamText(dream.getDreamText());
        dreamOutView.setId(dream.getId());
        dreamOutView.setDateCreation(dream.getDateCreation());
        dreamOutView.setLikes(dream.getLikes());
        dreamOutView.setComments((long) dream.getComments().size());
        dreamOutView.setAuthorId(dream.getAuthorId());
        dreamOutView.setAuthorUsername(dream.getAuthorUsername());
        return dreamOutView;
    }

    private Dream createDreamView(String dream, long authorId, String authorUsername) {
        Dream newDream = new Dream();
        newDream.setDreamText(dream);
        newDream.setAuthorId(authorId);
        newDream.setAuthorUsername(authorUsername);
        return newDream;
    }

    private List<DreamOutView> DreamOutList(List<Dream> dreams) {
        return dreams.stream().map(this::createDreamOutView).collect(Collectors.toList());
    }
}
