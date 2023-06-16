package nastia.somnusAuth.authorization.controller;

import lombok.RequiredArgsConstructor;
import nastia.somnusAuth.authorization.domain.*;
import nastia.somnusAuth.authorization.exception.*;
import nastia.somnusAuth.authorization.service.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthServiceInterface authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest){
        try{
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (MyException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping(value = "register")
    public ResponseEntity<UserOutView>  registerUser(@RequestBody UserInView userIn) throws MyException {
        try {
            UserOutView newUser = authService.registerUser(userIn);
            return ResponseEntity.ok().body(newUser);
        } catch (MyException e){
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
