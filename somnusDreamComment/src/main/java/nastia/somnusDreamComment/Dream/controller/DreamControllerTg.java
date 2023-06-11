package nastia.somnusDreamComment.Dream.controller;


import nastia.somnusDreamComment.Dream.checkAuthorization.AuthCheckService;
import nastia.somnusDreamComment.Dream.checkAuthorization.JwtAuthenticationDreams;
import nastia.somnusDreamComment.Dream.model.DreamInView;
import nastia.somnusDreamComment.Dream.model.DreamInViewTg;
import nastia.somnusDreamComment.Dream.model.DreamOutView;
import nastia.somnusDreamComment.Dream.service.DreamServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DreamControllerTg {

    DreamServiceInterface dreamService;

    public DreamControllerTg( DreamServiceInterface dreamService){
        this.dreamService = dreamService;
    }

    @GetMapping("randomTG")
    public ResponseEntity<DreamOutView> getRandomDream(){
        Optional<DreamOutView> dream = dreamService.getRandomDream();
        return dream.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("addTG")
    public ResponseEntity<DreamOutView> postDreamFromTg(@RequestBody DreamInViewTg dreamInViewTg){
        Optional<DreamOutView> dream = dreamService.addDreamTg(dreamInViewTg);
        return dream.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
