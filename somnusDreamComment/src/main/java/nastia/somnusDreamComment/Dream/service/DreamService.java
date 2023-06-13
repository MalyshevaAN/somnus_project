package nastia.somnusDreamComment.Dream.service;


import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Dream.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Dream.exception.DreamNotFoundException;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamInViewTg;
import nastia.somnusDreamComment.Dream.model.DreamOutView;
import nastia.somnusDreamComment.Dream.repository.DreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DreamService implements DreamServiceInterface {

    @Autowired
    DreamRepository dreamRepository;

//    @Autowired
//    UserService userService;

    public Optional<Dream> getDreamById(long dreamId){
        return dreamRepository.findById(dreamId);
    }

    public Optional<DreamOutView> readDream(long dreamId) throws DreamNotFoundException {
        Optional<Dream> dream = getDreamById(dreamId);
        if (dream.isPresent()){
            return Optional.of(createDreamOutView(dream.get()));
        }
        throw new DreamNotFoundException("dream is not found");
    }

    public Optional<DreamOutView> addDream(DreamInView dreamInView, Long authorId){
        Dream newDream = new Dream(dreamInView.getDreamText(), authorId);
        return Optional.of(createDreamOutView(dreamRepository.save(newDream)));
    }

    @Override
    public Optional<DreamOutView> addDreamTg(DreamInViewTg dreamInViewTg) {
        Dream newDream = new Dream(dreamInViewTg.getText(), dreamInViewTg.getAuthorId());
        return Optional.of(createDreamOutView(dreamRepository.save(newDream)));
    }

    public Optional<DreamOutView> updateDream(DreamInView dreamUpdate, Long authorId, long dreamId) throws DreamNotFoundException, UserHaveNoRights {
        Optional<Dream> dream = dreamRepository.findById(dreamId);
        if (dream.isEmpty()){
            throw new DreamNotFoundException("dream is not found");
        }
        if (!Objects.equals(dream.get().getAuthorId(), authorId)){
            throw new UserHaveNoRights();
        }
        Dream updatedDream = dream.get();
        updatedDream.setDreamText(dreamUpdate.getDreamText());
        updatedDream = dreamRepository.save(updatedDream);
        DreamOutView dreamOutView = createDreamOutView(updatedDream);
        return Optional.of(dreamOutView);
    }

    public void deleteDream(long dreamId, long userId) throws DreamNotFoundException, UserHaveNoRights {
        Optional<Dream> dream  = dreamRepository.findById(dreamId);
        if (dream.isEmpty()){
            throw new DreamNotFoundException();
        }
        if (dream.get().getAuthorId() != userId){
            throw new UserHaveNoRights();
        }
        dreamRepository.delete(dream.get());
    }

    public List<DreamOutView> getAllDreams(){
        List<Dream> allDreams =  dreamRepository.findAll();
        return DreamOutList(allDreams);
    }


    public Optional<DreamOutView> getRandomDream(){
        if (dreamRepository.count() == 0){
            return Optional.empty();
        }
        Optional<Dream> lastDream = dreamRepository.findFirstByOrderByIdDesc();
        if (dreamRepository.count() == 1){
            return Optional.of(createDreamOutView(lastDream.get()));
        }
        if (lastDream.isPresent()) {
            long lastId = lastDream.get().getId();

            long randId = (long) (Math.random()*lastId + 1);
            while (dreamRepository.findById(randId).isEmpty()) {
                System.out.println(randId);
                randId = (long) (Math.random()*lastId + 1);
            }
            return Optional.of(createDreamOutView(dreamRepository.findById(randId).get()));
        }
        return Optional.empty();
    }

    public List<DreamOutView> getUserDreams(long authorId){
        Optional<List<Dream>> usersDreams =  dreamRepository.findDreamByAuthorId(authorId);
        if (usersDreams.isPresent()){
            return DreamOutList(usersDreams.get());
        }
        return new ArrayList<>();
    }

    public Optional<DreamOutView> likeDream(long dreamId, long userId, boolean like) throws DreamNotExistsException {
        Optional<Dream> dream = dreamRepository.findById(dreamId);
        if (dream.isEmpty()){
            throw new DreamNotExistsException();
        }

        Dream dreamLike = dream.get();
        Set likes = dreamLike.getLikes();
        if (likes.add(userId)){
            dreamLike.setLikes(likes);
            return Optional.of(createDreamOutView(dreamRepository.save(dreamLike)));
        }else{
            likes.remove(userId);
            dreamLike.setLikes(likes);
            return Optional.of(createDreamOutView(dreamRepository.save(dreamLike)));
        }
    }

    private DreamOutView createDreamOutView(Dream dream){
        DreamOutView dreamOutView = new DreamOutView();
        dreamOutView.setDreamText(dream.getDreamText());
        dreamOutView.setId(dream.getId());
        dreamOutView.setLikes(dream.getLikes());
        dreamOutView.setComments(dream.getComments());
        dreamOutView.setAuthor(dream.getAuthorId());
        return dreamOutView;
    }

    private List<DreamOutView> DreamOutList(List<Dream> dreams){
        return dreams.stream().map(this::createDreamOutView).collect(Collectors.toList());
    }
}
