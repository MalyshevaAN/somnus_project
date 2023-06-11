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

    //    @Autowired
//    private final AvatarServiceConfig avatarService;
    @Autowired
    AvatarService avatarService;


    public UserOutView getByEmailTG(@NonNull UserInViewTG userInViewTG) throws UserIsNotFoundException {
        if (userRepository.existsByEmail(userInViewTG.getUserEmail())) {
            return createUserOutView(userRepository.findByEmail(userInViewTG.getUserEmail()));
        }
        throw new UserIsNotFoundException();
    }


    public Optional<User> getByEmail(@NonNull String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }


    public UserOutView addUser(UserInView userIn) throws UserAlreadyExists, PasswordDifferException {
        if (userRepository.existsByEmail(userIn.getEmail())) {
            throw new UserAlreadyExists();
        }

        if (!Objects.equals(userIn.getPassword(), userIn.getPasswordConfirm())) {
            throw new PasswordDifferException();
        }
        User newUser = createUserOfUserInView(userIn);
        return createUserOutView(userRepository.save(newUser));
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<UserOutView> saveFollow(long clientId, long userId) {
        if (clientId != userId) {
            Optional<User> client = userRepository.findById(clientId);
            Optional<User> subscription = userRepository.findById(userId);

            if (client.isPresent() && subscription.isPresent()) {
                User currentClient = client.get();
                currentClient.addSubscription(subscription.get());
                return Optional.of(createUserOutView(userRepository.save(currentClient)));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<UserOutView> deleteFollow(long clientId, long userId) {
        if (clientId != userId) {
            Optional<User> client = userRepository.findById(clientId);
            Optional<User> subscription = userRepository.findById(userId);

            if (client.isPresent() && subscription.isPresent()) {
                User currentClient = client.get();
                currentClient.deleteSubscription(subscription.get());
                return Optional.of(createUserOutView(userRepository.save(currentClient)));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Set<UserOutView> getSubscriptions(long userId) {
        Set<User> mySubscriptions = userRepository.findById(userId).get().getSubscribtions();
        return UserOutSet(mySubscriptions);
    }

    public Set<UserOutView> getSubscribers(long userId) {
        Set<User> mySubscribers = userRepository.findById(userId).get().getSubscribers();
        return UserOutSet(mySubscribers);
    }

    public UserOutView addAvatar(MultipartFile file, long userId) throws UserIsNotFoundException, UserHasNoAvatarException, UploadException {
        Optional<User> user = getUserById(userId);
        if (user.isPresent()) {
            User userWithAva = user.get();
            Long avatarId = avatarService.uploadAvatar(file, userId);
            userWithAva.setAvatarId(avatarId);
            UserOutView userWithAvaSaved = createUserOutView(userRepository.save(userWithAva));
            userWithAvaSaved.setAvatarPath(avatarService.downloadAvatar(avatarId));
            return userWithAvaSaved;
        }
        throw new UserIsNotFoundException();
    }

    private UserOutView createUserOutView(User user) {
        UserOutView newUser = new UserOutView();
        System.out.println(user.getId());
        newUser.setId(user.getId());
        newUser.setEmail(user.getEmail());
        newUser.setId(user.getId());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        try {
            newUser.setAvatarPath(avatarService.downloadAvatar(user.getId()));
        } catch (UserHasNoAvatarException e) {
            return newUser;
        }
        return newUser;
    }

    private User createUserOfUserInView(UserInView userInView) {
        User user = new User();
        user.setEmail(userInView.getEmail());
        user.setPassword(applicationConfig.HashPassword(userInView.getPassword()));
        user.setFirstName(userInView.getFirstName());
        user.setLastName(userInView.getLastName());
        return user;
    }

    private Set<UserOutView> UserOutSet(Set<User> users) {
        return users.stream().map(this::createUserOutView).collect(Collectors.toSet());
    }
}