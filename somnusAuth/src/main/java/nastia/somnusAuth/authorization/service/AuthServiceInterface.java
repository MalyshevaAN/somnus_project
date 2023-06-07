package nastia.somnusAuth.authorization.service;


import lombok.NonNull;
import nastia.somnusAuth.authorization.domain.*;
import nastia.somnusAuth.authorization.exception.PasswordDifferException;
import nastia.somnusAuth.authorization.exception.UserAlreadyExists;
public interface AuthServiceInterface {
    JwtResponse login(@NonNull JwtRequest authRequest);

    JwtResponse getAccessToken(@NonNull String refreshToken);

    JwtResponse refresh(@NonNull String refreshToken);

    JwtAuthentication getAuthInfo();
    UserOutView registerUser(UserInView userIn) throws UserAlreadyExists, PasswordDifferException;
//    UserOutView registerUser(UserInView userIn, MultipartFile file) throws UserAlreadyExists, PasswordDifferException, UploadException, UserHasNoAvatarException, ReadExeption;
}
