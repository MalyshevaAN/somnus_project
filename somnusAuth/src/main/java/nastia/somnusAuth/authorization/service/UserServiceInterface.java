package nastia.somnusAuth.authorization.service;

import lombok.NonNull;
import nastia.somnusAuth.authorization.domain.User;
import nastia.somnusAuth.authorization.domain.UserInView;
import nastia.somnusAuth.authorization.domain.UserInViewTG;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;
import java.util.Set;

public interface UserServiceInterface {

    UserOutView getByEmailTG(@NonNull UserInViewTG userInViewTG) throws MyException;

    User getByEmail(@NonNull String email) throws MyException;

    UserOutView addUser(UserInView userIn) throws MyException;

    Optional<User> getUserById(long id);

    UserOutView saveFollow(long clientId, long userId) throws MyException;
    UserOutView deleteFollow(long clientId, long userId) throws  MyException;

    Set<UserOutView> getSubscriptions(long userId) throws MyException;
    Set<UserOutView> getSubscribers(long userId);

    UserOutView addAvatar(MultipartFile file, long userId) throws MyException;
}
