package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.infrastructure.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public UserService(UserRepository userRepository, S3Uploader s3Uploader) {
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
    }

    public UserResponse findById(Long id) {
        User user = findUserById(id);
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public ProfileResponse updateProfile(MultipartFile multipartFile, AppUser appUser) throws IOException {
        User user = findUserById(appUser.getId());
        String updateProfileUrl = s3Uploader.upload(multipartFile, user.getUserName());
        user.updateProfileUrl(updateProfileUrl);
        return ProfileResponse.builder()
                .profileUrl(updateProfileUrl)
                .build();
    }
}
