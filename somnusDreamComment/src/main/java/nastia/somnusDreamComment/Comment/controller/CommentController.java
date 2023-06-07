package nastia.somnusDreamComment.Comment.controller;


import nastia.somnusDreamComment.Comment.exception.CommentNotFound;
import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Comment.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Comment.model.CommentInView;
import nastia.somnusDreamComment.Comment.model.CommentOutView;
import nastia.somnusDreamComment.Comment.service.CommentServiceInterface;
import nastia.somnusDreamComment.Dream.checkAuthorization.AuthCheckService;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("comment")
public class CommentController {


    CommentServiceInterface commentService;
    public CommentController(CommentServiceInterface commentService){
        this.commentService = commentService;
    }

    @Autowired
    AuthCheckService authService;

    @PostMapping("add/{dreamId}")
    public ResponseEntity<CommentOutView> addComment(@RequestBody CommentInView comment, @PathVariable long dreamId){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            Optional<CommentOutView> newComment = commentService.addComment(comment, dreamId, authInfo.getCredentials());
            return newComment.map(value -> ResponseEntity.ok(newComment.get())).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch(DreamNotExistsException | UsernameNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("read/{dreamId}")
    public ResponseEntity<List<CommentOutView>> readComment(@PathVariable long dreamId){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            Optional<List<CommentOutView>> dreamComments = commentService.readCommentForPost(dreamId);
            return dreamComments.map(value -> ResponseEntity.ok().body(dreamComments.get())).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (DreamNotExistsException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("edit/{commentId}")
    public ResponseEntity<CommentOutView> editComment(@PathVariable long commentId, @RequestBody CommentInView comment){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            Optional<CommentOutView> commentUpdated  = commentService.editComment(authInfo.getCredentials(), commentId, comment);
            return commentUpdated.map(value -> ResponseEntity.ok().body(commentUpdated.get())).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch(DreamNotExistsException e ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserHaveNoRights e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            commentService.deleteComment(authInfo.getCredentials(), commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CommentNotFound e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (UserHaveNoRights e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
