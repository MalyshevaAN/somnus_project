package nastia.somnusDreamComment.Comment.controller;


import nastia.somnusDreamComment.Comment.exception.MyCommentException;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;
import nastia.somnusDreamComment.Comment.service.CommentServiceInterface;
import nastia.somnusDreamComment.Dream.checkAuthorization.AuthCheckService;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {


    CommentServiceInterface commentService;

    public CommentController(CommentServiceInterface commentService) {
        this.commentService = commentService;
    }

    @Autowired
    AuthCheckService authService;

    @PostMapping("add/{dreamId}")
    public ResponseEntity<CommentOutView> addComment(@RequestBody CommentInView comment, @PathVariable long dreamId) {
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            CommentOutView newComment = commentService.addComment(comment, dreamId, authInfo.getCredentials(), authInfo.getAuthorUserName());
            return ResponseEntity.ok(newComment);
        } catch (MyCommentException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    @GetMapping("read/{dreamId}")
    public ResponseEntity<List<CommentOutView>> readComment(@PathVariable long dreamId) {
        try {
            List<CommentOutView> dreamComments = commentService.readCommentForPost(dreamId);
            return ResponseEntity.ok().body(dreamComments);
        } catch (MyCommentException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("edit/{commentId}")
    public ResponseEntity<CommentOutView> editComment(@PathVariable long commentId, @RequestBody CommentInView comment) {
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            CommentOutView commentUpdated = commentService.editComment(authInfo.getCredentials(), commentId, comment);
            return ResponseEntity.ok().body(commentUpdated);
        } catch (MyCommentException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId) {
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            commentService.deleteComment(authInfo.getCredentials(), commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MyCommentException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
