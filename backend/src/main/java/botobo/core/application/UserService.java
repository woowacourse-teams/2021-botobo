package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.user.UserNameDuplicatedException;
import botobo.core.exception.user.UserUpdateNotAllowedException;
import botobo.core.infrastructure.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

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
    public UserResponse update(Long id, UserUpdateRequest userUpdateRequest, AppUser appUser) {
        User user = findUser(appUser);
        if (!user.isSameId(id)) {
            throw new UserUpdateNotAllowedException();
        }
        validateDuplicatedUserName(userUpdateRequest.getUserName(), user);
        user.update(userUpdateRequest.toUser());
        return UserResponse.of(user);
    }

    private void validateDuplicatedUserName(String requestedName, User user) {
        userRepository.findByUserName(requestedName).ifPresent(
                findUser -> {
                    if (!Objects.equals(user, findUser)) {
                        throw new UserNameDuplicatedException(requestedName);
                    }
                }
        );
    }

    @Transactional
    public ProfileResponse updateProfile(MultipartFile multipartFile, AppUser appUser) throws IOException {
        User user = findUser(appUser);
        String updateProfileUrl = s3Uploader.upload(multipartFile, user.getUserName());
        user.updateProfileUrl(updateProfileUrl);
        return ProfileResponse.builder()
                .profileUrl(updateProfileUrl)
                .build();
    }

    public void checkSameUserNameAlreadyExist(UserNameRequest userNameRequest, AppUser appUser) {
        validateDuplicatedUserName(userNameRequest.getUserName(), findUser(appUser));
    }
}
