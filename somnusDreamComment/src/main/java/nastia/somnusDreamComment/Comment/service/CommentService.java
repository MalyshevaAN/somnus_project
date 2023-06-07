package nastia.somnusDreamComment.Comment.service;

import nastia.somnusDreamComment.Comment.exception.CommentNotFound;
import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Comment.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Comment.model.Comment;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;
import nastia.somnusDreamComment.Comment.repository.CommentRepository;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService implements CommentServiceInterface{

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    DreamServiceInterface dreamService;

    public Optional<CommentOutView> addComment(CommentInView commentInView, long dreamId, long authId) throws DreamNotExistsException {

        Optional<Dream> dream = dreamService.getDreamById(dreamId);

        if (dream.isEmpty()){
            throw new DreamNotExistsException("dream with id " + dreamId + "does not exists");
        }

        Comment newComment = new Comment();
        newComment.setCommentText(commentInView.getCommentText());
        newComment.setUserId(authId);
        newComment.setDream(dream.get());
        return Optional.of(createCommentOutView(commentRepository.save(newComment)));
    }

    public Optional<List<CommentOutView>> readCommentForPost(long dreamId) throws DreamNotExistsException {
        Optional<Dream> dream = dreamService.getDreamById(dreamId);

        if (dream.isEmpty()){
            throw new DreamNotExistsException("dream with id " + dreamId + "does not exists");
        }
        List<Comment> comments = commentRepository.findByDreamId(dreamId);
        return Optional.of(CommentOutList(comments));


    }

    public Optional<CommentOutView> editComment(long userId, long commentId, CommentInView comment) throws UserHaveNoRights, DreamNotExistsException {
        Optional<Comment> oldComment = commentRepository.findById(commentId);
        if (oldComment.isPresent()){
            Comment old = oldComment.get();
            if (old.getUserId() == userId){
                old.setCommentText(comment.getCommentText());
                return Optional.of(createCommentOutView(commentRepository.save(old)));
            }
            throw new UserHaveNoRights();
        }
        throw new DreamNotExistsException();
    }

    public void deleteComment(long userId, long commentId) throws UserHaveNoRights, CommentNotFound {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()){
            Comment comment1 = comment.get();
            if (comment1.getUserId() == userId){
                commentRepository.delete(comment1);
            }
            throw new UserHaveNoRights();
        }
        throw new CommentNotFound();
    }

    private CommentOutView createCommentOutView(Comment comment){
        CommentOutView commentOutView = new CommentOutView();
        commentOutView.setCommentText(comment.getCommentText());
        commentOutView.setId(comment.getId());
        commentOutView.setUserId(comment.getUserId());
        return commentOutView;
    }
    private List<CommentOutView> CommentOutList(List<Comment> comments){
        return comments.stream().map(this::createCommentOutView).collect(Collectors.toList());
    }
}