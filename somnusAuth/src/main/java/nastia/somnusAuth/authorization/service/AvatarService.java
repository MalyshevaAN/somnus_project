package nastia.somnusAuth.authorization.service;


import nastia.somnusAuth.authorization.domain.Avatar;
import nastia.somnusAuth.authorization.exception.MyException;
import nastia.somnusAuth.authorization.exception.UploadException;
import nastia.somnusAuth.authorization.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AvatarService implements AvatarServiceInterface {

    @Autowired
    AvatarRepository avatarRepository;

    List<String> possibleAvatars = List.of("https://ltdfoto.ru/images/2023/06/21/ava3.jpg", "https://ltdfoto.ru/images/2023/06/21/ava5.jpg", "https://ltdfoto.ru/images/2023/06/21/ava2.jpg");

    private final String PATH = "/home/nastia/javaProjects/ava/";


    public String getRandomAvatar() {
        int randomAvatar = (int) (Math.random() * (possibleAvatars.size()));
        return possibleAvatars.get(randomAvatar);
    }


    public String uploadAvatar(MultipartFile file, long userId) throws UploadException {
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
            return avatarRepository.save(avatar).getImagePath();
        }
        throw new MyException(HttpStatus.BAD_REQUEST, "Такая фотография уже загружена");
//        String avatarPath = getAvatar();
//        String[] components = avatarPath.split("/");
//        String fileName = components[components.length-1];
//        String fileType = fileName.split(".")[1];
//        Avatar newUserAvatar = new Avatar();
//        newUserAvatar.setUserId(userId);
//        newUserAvatar.setImagePath(avatarPath);
//        newUserAvatar.setName(fileName);
//        newUserAvatar.setType(fileType);
//        return avatarRepository.save(newUserAvatar).getId;
    }

    public String downloadAvatar(long userId) {
        Optional<Avatar> userAvatar = avatarRepository.findByUserId(userId);
        return userAvatar.map(Avatar::getImagePath).orElse(null);
    }
}
