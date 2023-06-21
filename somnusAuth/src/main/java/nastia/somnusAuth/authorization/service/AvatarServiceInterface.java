package nastia.somnusAuth.authorization.service;

import nastia.somnusAuth.authorization.exception.UploadException;
import org.springframework.web.multipart.MultipartFile;


public interface AvatarServiceInterface {
    String uploadAvatar(MultipartFile file, long userId) throws UploadException;

    String downloadAvatar(long userId);

    String getRandomAvatar();
}


