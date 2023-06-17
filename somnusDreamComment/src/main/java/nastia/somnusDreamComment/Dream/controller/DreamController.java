package nastia.somnusDreamComment.Dream.controller;


import nastia.somnusDreamComment.Dream.checkAuthorization.AuthCheckService;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import nastia.somnusDreamComment.Dream.exception.MyDreamException;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamOutView;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
public class DreamController {

    AuthCheckService authService;

    DreamServiceInterface dreamService;

    public DreamController(AuthCheckService authService, DreamServiceInterface dreamService){
        this.authService = authService;
        this.dreamService = dreamService;
    }

    @GetMapping("read/{dreamId}")
    public ResponseEntity<DreamOutView> getDream(@PathVariable long dreamId){
        try {
            DreamOutView dream = dreamService.readDream(dreamId);
            return ResponseEntity.ok().body(dream);
        } catch (MyDreamException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping("add")
    public ResponseEntity<DreamOutView>  addDream(@RequestBody DreamInView dreamInView){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        DreamOutView dream = dreamService.addDream(dreamInView, authInfo.getCredentials(), authInfo.getAuthorUserName());
        return ResponseEntity.ok().body(dream);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DreamOutView> updateDream(@RequestBody DreamInView dreamUpdate, @PathVariable long id){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try {
            DreamOutView newDream = dreamService.updateDream(dreamUpdate, authInfo.getCredentials(), id);
            return ResponseEntity.ok().body(newDream);
        } catch (MyDreamException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteDream(@PathVariable Long id){
        final JwtAuthenticationDreams authInfo = authService.getAuthInfo();
        try{
            dreamService.deleteDream(id, authInfo.getCredentials());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MyDreamException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<DreamOutView>> getAllDreams(){
        return ResponseEntity.ok().body(dreamService.getAllDreams());
    }

    @GetMapping("random")
    public ResponseEntity<DreamOutView> getRandomDream(){
        try {
            DreamOutView dream = dreamService.getRandomDream();
            return ResponseEntity.ok().body(dream);
        }catch (MyDreamException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
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
            DreamOutView likedDream = dreamService.likeDream(dreamId, authInfo.getCredentials(), true);
            return ResponseEntity.ok().body(likedDream);
        }catch (MyDreamException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
