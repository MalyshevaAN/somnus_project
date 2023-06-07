package nastia.somnusDreamComment.Comment.service;


import nastia.somnusDreamComment.Comment.exception.CommentNotFound;
import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Comment.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CommentServiceInterface {
     Optional<CommentOutView> addComment(CommentInView commentInView, long dreamId, long authId) throws DreamNotExistsException;

     Optional<List<CommentOutView>> readCommentForPost(long dreamId) throws DreamNotExistsException;

     Optional<CommentOutView> editComment(long userId, long commentId, CommentInView comment) throws UserHaveNoRights, DreamNotExistsException;

     void deleteComment(long userId, long commentId) throws UserHaveNoRights, CommentNotFound;
}
