package nastia.somnusAuth.authorization.service;

import lombok.NonNull;
import nastia.somnusAuth.authorization.domain.User;
import nastia.somnusAuth.authorization.domain.UserInView;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;
import java.util.Set;

public interface UserServiceInterface {
    Optional<User> getByEmail(@NonNull String email);

    UserOutView addUser(UserInView userIn) throws UserAlreadyExists, PasswordDifferException;

//    UserOutView addUser(UserInView userIn, MultipartFile file) throws UserAlreadyExists, PasswordDifferException, UploadException, UserHasNoAvatarException, ReadExeption;
//


    Optional<User> getUserById(long id);

    Optional<UserOutView> saveFollow(long clientId, long userId);
    Optional<UserOutView> deleteFollow(long clientId, long userId);

    Set<UserOutView> getSubscriptions(long userId);
    Set<UserOutView> getSubscribers(long userId);
    UserOutView addAvatar(MultipartFile file, long userId) throws UserIsNotFoundException, UploadException, UserHasNoAvatarException, ReadExeption;
}
