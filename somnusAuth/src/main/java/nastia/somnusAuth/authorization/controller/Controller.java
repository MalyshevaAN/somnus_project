package nastia.somnusAuth.authorization.controller;

import lombok.RequiredArgsConstructor;
import nastia.somnusAuth.authorization.domain.JwtAuthentication;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.*;
import nastia.somnusAuth.authorization.service.AuthServiceInterface;
import nastia.somnusAuth.authorization.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private final AuthServiceInterface authService;
    @Autowired
    private final UserServiceInterface userService;


    @PutMapping("follow/{userId}")
    public ResponseEntity<UserOutView> addFollow(@PathVariable long userId) {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        try {
            return ResponseEntity.ok().body(userService.saveFollow(authInfo.getCredentials(), userId));
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("unfollow/{userId}")
    public ResponseEntity<UserOutView> removeFollow(@PathVariable long userId) {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        try {
            return ResponseEntity.ok().body(userService.deleteFollow(authInfo.getCredentials(), userId));
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("subscriptions")
    public ResponseEntity<Set<UserOutView>> getMySubscriptions() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        try {
            return ResponseEntity.ok().body(userService.getSubscriptions(authInfo.getCredentials()));
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("subscribers")
    public ResponseEntity<Set<UserOutView>> getMySubscribers() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        try {
            return ResponseEntity.ok().body(userService.getSubscribers(authInfo.getCredentials()));
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping("avatar")
    public ResponseEntity<UserOutView> addAvatar(@RequestParam("userAvatar") MultipartFile file) {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        try {
            return ResponseEntity.ok().body(userService.addAvatar(file, authInfo.getCredentials()));
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
