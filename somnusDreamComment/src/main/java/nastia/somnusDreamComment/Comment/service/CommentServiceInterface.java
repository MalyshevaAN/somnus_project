package nastia.somnusDreamComment.Comment.service;


import nastia.somnusDreamComment.Comment.exception.MyCommentException;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;

import java.util.List;

public interface CommentServiceInterface {
    CommentOutView addComment(CommentInView commentInView, long dreamId, long authId, String authorUsername) throws MyCommentException;

    List<CommentOutView> readCommentForPost(long dreamId) throws MyCommentException;

    CommentOutView editComment(long userId, long commentId, CommentInView comment) throws MyCommentException;

    void deleteComment(long userId, long commentId) throws MyCommentException;
}
