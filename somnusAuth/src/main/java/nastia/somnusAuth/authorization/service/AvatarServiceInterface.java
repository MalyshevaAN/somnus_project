package nastia.somnusAuth.authorization.service;

import nastia.somnusAuth.authorization.domain.Avatar;
import nastia.somnusAuth.authorization.exception.UploadException;
import nastia.somnusAuth.authorization.exception.UserHasNoAvatarException;
import org.springframework.web.multipart.MultipartFile;



public interface AvatarServiceInterface {
//    String uploadAvatar(long userId) throws UploadException;
//    String downloadAvatar(long userId);
    String getRandomAvatar();
}


