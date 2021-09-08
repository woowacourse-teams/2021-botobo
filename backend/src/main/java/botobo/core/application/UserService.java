package botobo.core.application;

import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagResponse;
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
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

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
        String oldProfileUrl = user.getProfileUrl();
        String newProfileUrl = s3Uploader.upload(multipartFile, user.getUserName());

        user.updateProfileUrl(newProfileUrl);

        s3Uploader.deleteFromS3(oldProfileUrl);
        return ProfileResponse.builder()
                .profileUrl(newProfileUrl)
                .build();
    }

    public void checkDuplicatedUserName(UserNameRequest userNameRequest, AppUser appUser) {
        validateDuplicatedUserName(userNameRequest.getUserName(), appUser);
    }

    private void validateDuplicatedUserName(String requestedName, AppUser me) {
        userRepository.findByUserName(requestedName).ifPresent(
                findUser -> {
                    if (!findUser.isSameId(me.getId())) {
                        throw new UserNameDuplicatedException();
                    }
                }
        );
    }

    public List<UserResponse> findAllUsersByWorkbookName(FilterCriteria filterCriteria) {
        if (filterCriteria.isEmpty()) {
            return UserResponse.listOf(emptyList());
        }
        List<User> users = userRepository.findAllByContainsWorkbookName(filterCriteria.getWorkbook());
        return UserResponse.listOf(users);
    }
}
