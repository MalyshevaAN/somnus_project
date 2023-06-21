package nastia.somnusAuth.authorization.controller;


import nastia.somnusAuth.authorization.domain.UserInViewTG;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.MyException;
import nastia.somnusAuth.authorization.service.UserServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTG {


    UserServiceInterface userService;

    public ControllerTG(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("idByEmail")
    public ResponseEntity<UserOutView> getUserByEmail(@RequestBody UserInViewTG userInViewTG) {
        try {
            UserOutView user = userService.getByEmailTG(userInViewTG);
            return ResponseEntity.ok().body(user);
        } catch (MyException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
