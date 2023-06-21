package nastia.somnusDreamComment.Comment.service;

import nastia.somnusDreamComment.Comment.exception.MyCommentException;
import nastia.somnusDreamComment.Comment.model.Comment;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;
import nastia.somnusDreamComment.Comment.repository.CommentRepository;
import nastia.somnusDreamComment.Dream.model.Dream;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService implements CommentServiceInterface {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    DreamServiceInterface dreamService;

    public CommentOutView addComment(CommentInView commentInView, long dreamId, long authId, String authorUsername) throws MyCommentException {

        Optional<Dream> dream = dreamService.getDreamById(dreamId);

        if (dream.isEmpty()) {
            throw new MyCommentException(HttpStatus.NOT_FOUND, "Такой сон не найден");
        }

        Comment newComment = createCommentView(commentInView, authId, dream.get(), authorUsername);
        newComment.setCommentText(commentInView.getCommentText());
        newComment.setAuthorId(authId);
        newComment.setDream(dream.get());
        return createCommentOutView(commentRepository.save(newComment));
    }

    public List<CommentOutView> readCommentForPost(long dreamId) throws MyCommentException {
        Optional<Dream> dream = dreamService.getDreamById(dreamId);

        if (dream.isEmpty()) {
            throw new MyCommentException(HttpStatus.NOT_FOUND, "Такой сон не найден");
        }
        List<Comment> comments = commentRepository.findByDreamId(dreamId);
        return CommentOutList(comments);


    }

    public CommentOutView editComment(long userId, long commentId, CommentInView comment) throws MyCommentException {
        Optional<Comment> oldComment = commentRepository.findById(commentId);
        if (oldComment.isPresent()) {
            Comment old = oldComment.get();
            if (old.getAuthorId() == userId) {
                old.setCommentText(comment.getCommentText());
                return createCommentOutView(commentRepository.save(old));
            }
            throw new MyCommentException(HttpStatus.NOT_ACCEPTABLE, "Нельзя редактировать чужие комментарии");
        }
        throw new MyCommentException(HttpStatus.NOT_FOUND, "Комментарий не найден");
    }

    public void deleteComment(long userId, long commentId) throws MyCommentException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment comment1 = comment.get();
            if (comment1.getAuthorId() == userId) {
                commentRepository.delete(comment1);
            }
            throw new MyCommentException(HttpStatus.NOT_ACCEPTABLE, "Нельзя удалять чужие посты");
        }
        throw new MyCommentException(HttpStatus.NOT_FOUND, "Комментарий не найден");
    }

    private CommentOutView createCommentOutView(Comment comment) {
        CommentOutView commentOutView = new CommentOutView();
        commentOutView.setCommentText(comment.getCommentText());
        commentOutView.setId(comment.getId());
        commentOutView.setUserId(comment.getAuthorId());
        commentOutView.setAuthorUserName(comment.getAuthorUserName());
        return commentOutView;
    }

    private Comment createCommentView(CommentInView commentIn, long authorId, Dream dream, String authorUserName) {
        Comment comment = new Comment();
        comment.setAuthorId(authorId);
        comment.setCommentText(commentIn.getCommentText());
        comment.setAuthorUserName(authorUserName);
        comment.setDream(dream);
        return comment;
    }

    private List<CommentOutView> CommentOutList(List<Comment> comments) {
        return comments.stream().map(this::createCommentOutView).collect(Collectors.toList());
    }
}