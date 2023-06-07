package nastia.somnusDreamComment.Dream.controller;


import nastia.somnusDreamComment.Comment.exception.DreamNotExistsException;
import nastia.somnusDreamComment.Dream.checkAuthorization.AuthCheckService;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import nastia.somnusDreamComment.Dream.exception.DreamNotFoundException;
import nastia.somnusDreamComment.Dream.exception.UserHaveNoRights;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamOutView;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// NEED TO CREATE VALID CHECK WHEN SPLIT IT ON DIFFERENT SERVICES


import java.util.List;
import java.util.Optional;

@RestController
public class DreamController {

//    AuthService authService;

    AuthCheckService authService;

    DreamServiceInterface dreamService;

    public DreamController(AuthCheckService authService, DreamServiceInterface dreamService){
        this.authService = authService;
        this.dreamService = dreamService;
    }

    @GetMapping("read/{dreamId}")
    public ResponseEntity<DreamOutView> getDream(@PathVariable long dreamId){
        try {
            Optional<DreamOutView> dream = dreamService.readDream(dreamId);
            return ResponseEntity.ok().body(dream.get());
        } catch (DreamNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add")
    public ResponseEntity<DreamOutView>  addDream(@RequestBody DreamInView dreamInView){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        Optional<DreamOutView> dream = dreamService.addDream(dreamInView, authInfo.getCredentials());
        return dream.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DreamOutView> updateDream(@RequestBody DreamInView dreamUpdate, @PathVariable long id){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            Optional<DreamOutView> newDream = dreamService.updateDream(dreamUpdate, authInfo.getCredentials(), id);
            return ResponseEntity.ok().body(newDream.get());
        } catch (DreamNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserHaveNoRights e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteDream(@PathVariable Long id){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try{
            dreamService.deleteDream(id, authInfo.getCredentials());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(DreamNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (UserHaveNoRights e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<DreamOutView>> getAllDreams(){
        return ResponseEntity.ok().body(dreamService.getAllDreams());
    }

    @GetMapping("random")
    public ResponseEntity<DreamOutView> getRandomDream(){
        Optional<DreamOutView> dream = dreamService.getRandomDream();
        return dream.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<DreamOutView>> getUsersDreams(@PathVariable long userId){
        List<DreamOutView> usersDreams = dreamService.getUserDreams(userId);
        return ResponseEntity.ok().body(usersDreams);
    }

    @GetMapping("my")
    public ResponseEntity<List<DreamOutView>> getMyDreams(){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        List<DreamOutView> myDreams = dreamService.getUserDreams(authInfo.getCredentials());
        return ResponseEntity.ok().body(myDreams);
    }

    @PutMapping("/like/{dreamId}")
    public ResponseEntity<DreamOutView> likeDream(@PathVariable long dreamId){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            Optional<DreamOutView> likedDream = dreamService.likeDream(dreamId, authInfo.getCredentials(), true);
            return ResponseEntity.ok().body(likedDream.get());
        }catch (DreamNotExistsException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
