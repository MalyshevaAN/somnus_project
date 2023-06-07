package nastia.somnusAuth.authorization.service;


import nastia.somnusAuth.authorization.domain.Avatar;
import nastia.somnusAuth.authorization.exception.UploadException;
import nastia.somnusAuth.authorization.exception.UserHasNoAvatarException;
import nastia.somnusAuth.authorization.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class AvatarService implements AvatarServiceInterface{

    @Autowired
    AvatarRepository avatarRepository;

    private final String PATH = "/home/nastia/Somnus/UserAvatars/";


    //по одной аватарке на юзера или же хранить историю аватарок? пока сделаю по одной на юзера, дальше посмотрим
    public Long uploadAvatar(MultipartFile file, long userId) throws UploadException {
        String filename = userId + "_" + file.getOriginalFilename();
        String fullPath = PATH + filename;
        Optional<Avatar> existingAvatar = avatarRepository.findByImagePath(fullPath);
        if (existingAvatar.isEmpty()) {
            Optional<Avatar> userAvatar = avatarRepository.findByUserId(userId);
            Avatar avatar;
            avatar = userAvatar.orElseGet(Avatar::new);
            avatar.setUserId(userId);
            avatar.setImagePath(fullPath);
            avatar.setName(filename);
            avatar.setType(file.getContentType());
            try {
                file.transferTo(new File(fullPath));
            } catch (IOException e) {
                throw new UploadException();
            }
            return avatarRepository.save(avatar).getId();
        }
        throw new UploadException();
    }

    public String downloadAvatar(long userId) throws UserHasNoAvatarException {
        Optional<Avatar>  userAvatar = avatarRepository.findByUserId(userId);
        System.out.println(userAvatar);
        if (userAvatar.isPresent()){
            return userAvatar.get().getImagePath();
        }
        throw new UserHasNoAvatarException();
    }
}
