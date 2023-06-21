package nastia.somnusAuth.authorization.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nastia.somnusAuth.authorization.config.ApplicationConfig;
import nastia.somnusAuth.authorization.domain.User;
import nastia.somnusAuth.authorization.domain.UserInView;
import nastia.somnusAuth.authorization.domain.UserInViewTG;
import nastia.somnusAuth.authorization.domain.UserOutView;
import nastia.somnusAuth.authorization.exception.*;
import nastia.somnusAuth.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicationConfig applicationConfig;

    @Autowired
    AvatarService avatarService;


    public UserOutView getByEmailTG(@NonNull UserInViewTG userInViewTG) {
        if (userRepository.existsByEmail(userInViewTG.getUserEmail())) {
            return createUserOutView(userRepository.findByEmail(userInViewTG.getUserEmail()));
        }
        throw new MyException(HttpStatus.NOT_FOUND, "Пользователь с такой почтой не найден");
    }


    public User getByEmail(@NonNull String email) throws MyException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        throw new MyException(HttpStatus.NOT_FOUND, "Пользователь с такой почтой не найден");
    }


    public UserOutView addUser(UserInView userIn) throws MyException {
        if (userRepository.existsByEmail(userIn.getEmail().toLowerCase())) {
            throw new MyException(HttpStatus.CONFLICT, "Пользователь с такой почтой уже зарегистрирован");
        }

        if (!Objects.equals(userIn.getPassword(), userIn.getPasswordConfirm())) {
            throw new MyException(HttpStatus.BAD_REQUEST, "Пароли не совпадают");
        }
        User newUser = createUserOfUserInView(userIn);
        return createUserOutView(userRepository.save(newUser));
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public UserOutView saveFollow(long clientId, long userId) {
        if (clientId != userId) {
            Optional<User> client = userRepository.findById(clientId);
            Optional<User> subscription = userRepository.findById(userId);

            if (client.isPresent() && subscription.isPresent()) {
                User currentClient = client.get();
                currentClient.addSubscription(subscription.get());
                return createUserOutView(userRepository.save(currentClient));
            }
            throw new MyException(HttpStatus.BAD_REQUEST, "Некорректный запрос");
        }
        throw new MyException(HttpStatus.CONFLICT, "Нельзя подписываться на самого себя");
    }

    public UserOutView deleteFollow(long clientId, long userId) {
        if (clientId != userId) {
            Optional<User> client = userRepository.findById(clientId);
            Optional<User> subscription = userRepository.findById(userId);

            if (client.isPresent() && subscription.isPresent()) {
                User currentClient = client.get();
                currentClient.deleteSubscription(subscription.get());
                return createUserOutView(userRepository.save(currentClient));
            }
            throw new MyException(HttpStatus.BAD_REQUEST, "Некорректный запрос");
        }
        throw new MyException(HttpStatus.CONFLICT, "Нельзя отписаться от самого себя");
    }

    public Set<UserOutView> getSubscriptions(long userId) {
        Set<User> mySubscriptions = userRepository.findById(userId).get().getSubscribtions();
        return UserOutSet(mySubscriptions);
    }

    public Set<UserOutView> getSubscribers(long userId) {
        Set<User> mySubscribers = userRepository.findById(userId).get().getSubscribers();
        return UserOutSet(mySubscribers);
    }

    public UserOutView addAvatar(MultipartFile file, long userId) throws MyException {
        Optional<User> user = getUserById(userId);
        if (user.isPresent()) {
            User userWithAva = user.get();
            try {
                String avatarPath = avatarService.uploadAvatar(file, userId);
                userWithAva.setAvatarPath(avatarPath);
                UserOutView userWithAvaSaved = createUserOutView(userRepository.save(userWithAva));
                userWithAvaSaved.setAvatarPath(avatarService.downloadAvatar(userId));
                return userWithAvaSaved;
            } catch (UploadException e) {
                throw new MyException(HttpStatus.BAD_GATEWAY, "Не удается загрузить фотографию");
            }
        }
        throw new MyException(HttpStatus.NOT_FOUND, "Пользователь не найден");
    }

    private UserOutView createUserOutView(User user) {
        UserOutView newUser = new UserOutView();
        newUser.setId(user.getId());
        newUser.setEmail(user.getEmail());
        newUser.setId(user.getId());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
//        newUser.setAvatarPath(avatarService.downloadAvatar(user.getId()));
        newUser.setAvatarPath(user.getAvatarPath());
        return newUser;
    }

    private User createUserOfUserInView(UserInView userInView) {
        User user = new User();
        user.setEmail(userInView.getEmail().toLowerCase());
        user.setPassword(applicationConfig.HashPassword(userInView.getPassword()));
        user.setFirstName(userInView.getFirstName());
        user.setLastName(userInView.getLastName());
        user.setAvatarPath(avatarService.getRandomAvatar());
        return user;
    }

    private Set<UserOutView> UserOutSet(Set<User> users) {
        return users.stream().map(this::createUserOutView).collect(Collectors.toSet());
    }
}