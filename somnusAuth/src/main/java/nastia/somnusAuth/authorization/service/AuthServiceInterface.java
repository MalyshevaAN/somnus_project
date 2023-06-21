package nastia.somnusAuth.authorization.service;


import lombok.NonNull;
import nastia.somnusAuth.authorization.domain.*;
import nastia.somnusAuth.authorization.exception.MyException;


public interface AuthServiceInterface {
    JwtResponse login(@NonNull JwtRequest authRequest) throws MyException;

    JwtAuthentication getAuthInfo();

    UserOutView registerUser(UserInView userIn) throws MyException;

}
