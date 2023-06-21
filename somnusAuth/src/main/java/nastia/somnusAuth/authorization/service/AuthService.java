package nastia.somnusAuth.authorization.service;


import lombok.NonNull;
import nastia.somnusAuth.authorization.config.ApplicationConfig;
import nastia.somnusAuth.authorization.domain.*;
import nastia.somnusAuth.authorization.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    UserService userService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ApplicationConfig applicationConfig;

    public AuthService() {

    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws MyException {
        final User user = userService.getByEmail(authRequest.getEmail());
        if (user.getPassword().equals(applicationConfig.HashPassword(authRequest.getPassword()))) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken);
        } else {
            throw new MyException(HttpStatus.CONFLICT, "Неверный пароль или почта");
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public UserOutView registerUser(UserInView userIn) throws MyException {
        return userService.addUser(userIn);
    }
}