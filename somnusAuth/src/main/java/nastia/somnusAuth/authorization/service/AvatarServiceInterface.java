package nastia.somnusAuth.authorization.service;

import nastia.somnusAuth.authorization.exception.UploadException;
import nastia.somnusAuth.authorization.exception.UserHasNoAvatarException;
import org.springframework.web.multipart.MultipartFile;



public interface AvatarServiceInterface {
    Long uploadAvatar(MultipartFile file, long userId) throws UploadException;
    String downloadAvatar(long userId) throws UserHasNoAvatarException;
}
