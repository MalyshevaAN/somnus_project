package nastia.somnusAuth.authorization.controller;

import lombok.RequiredArgsConstructor;
import nastia.somnusAuth.authorization.domain.*;
import nastia.somnusAuth.authorization.exception.*;
import nastia.somnusAuth.authorization.service.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthServiceInterface authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "register")
    public ResponseEntity<UserOutView>  registerUser(@RequestBody UserInView userIn) throws UserAlreadyExists, PasswordDifferException, UploadException, UserHasNoAvatarException, ReadExeption {
//        if (file == null) {
//            UserOutView newUser = authService.registerUser(userIn);
//            return ResponseEntity.ok().body(newUser);
//        }
        UserOutView newUser = authService.registerUser(userIn);
        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
