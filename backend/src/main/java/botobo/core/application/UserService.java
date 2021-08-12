package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.user.UserNameDuplicatedException;
import botobo.core.infrastructure.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class UserService extends AbstractUserService {

    private final S3Uploader s3Uploader;

    public UserService(UserRepository userRepository, S3Uploader s3Uploader) {
        super(userRepository);
        this.s3Uploader = s3Uploader;
    }

    public UserResponse findById(AppUser appUser) {
        return UserResponse.of(findUser(appUser));
    }

    @Transactional
    public UserResponse update(UserUpdateRequest userUpdateRequest, AppUser appUser) {
        validateDuplicatedUserName(userUpdateRequest.getUserName(), appUser);
        User user = findUser(appUser);
        user.update(userUpdateRequest.toUser());
        return UserResponse.of(user);
    }

    @Transactional
    public ProfileResponse updateProfile(MultipartFile multipartFile, AppUser appUser) throws IOException {
        User user = findUser(appUser);
        String updateProfileUrl = s3Uploader.upload(multipartFile, user);
        user.updateProfileUrl(updateProfileUrl);
        return ProfileResponse.builder()
                .profileUrl(updateProfileUrl)
                .build();
    }

    public void checkDuplicatedUserName(UserNameRequest userNameRequest, AppUser appUser) {
        validateDuplicatedUserName(userNameRequest.getUserName(), appUser);
    }

    private void validateDuplicatedUserName(String requestedName, AppUser me) {
        userRepository.findByUserName(requestedName).ifPresent(
                findUser -> {
                    if (!findUser.isSameId(me.getId())) {
                        throw new UserNameDuplicatedException(requestedName);
                    }
                }
        );
    }
}
