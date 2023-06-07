package nastia.somnusAuth.authorization.controller;

import lombok.RequiredArgsConstructor;
import nastia.somnusAuth.authorization.domain.JwtAuthentication;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.ReadExeption;
import nastia.somnusAuth.authorization.exception.UploadException;
import nastia.somnusAuth.authorization.exception.UserHasNoAvatarException;
import nastia.somnusAuth.authorization.exception.UserIsNotFoundException;
import nastia.somnusAuth.authorization.service.AuthServiceInterface;
import nastia.somnusAuth.authorization.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private final AuthServiceInterface authService;
    @Autowired
    private final UserServiceInterface userService;



    @PutMapping("follow/{userId}")
    public ResponseEntity<UserOutView> addFollow(@PathVariable long userId){
        final JwtAuthentication authInfo = authService.getAuthInfo();
        Optional<UserOutView> userFollow = userService.saveFollow(authInfo.getCredentials(), userId);
        return userFollow.map(user -> ResponseEntity.ok().body(user)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));  //follow yourself
    }

    @PutMapping("unfollow/{userId}")
    public ResponseEntity<UserOutView> removeFollow(@PathVariable long userId){
        final JwtAuthentication authInfo = authService.getAuthInfo();
        Optional<UserOutView> userUnfollow = userService.deleteFollow(authInfo.getCredentials(), userId);
        return userUnfollow.map(user -> ResponseEntity.ok().body(user)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("subscriptions")
    public ResponseEntity<Set<UserOutView>> getMySubscriptions(){
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok().body(userService.getSubscriptions(authInfo.getCredentials()));
    }

    @GetMapping("subscribers")
    public ResponseEntity<Set<UserOutView>> getMySubscribers(){
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok().body(userService.getSubscribers(authInfo.getCredentials()));
    }

    @PostMapping("avatar")
    public ResponseEntity<UserOutView> addAvatar(@RequestParam("userAvatar") MultipartFile file) throws UserIsNotFoundException, UserHasNoAvatarException, ReadExeption, UploadException {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok().body(userService.addAvatar(file, authInfo.getCredentials()));
    }
}
