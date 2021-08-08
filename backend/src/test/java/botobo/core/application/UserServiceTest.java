package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.user.UserUpdateNotAllowedException;
import botobo.core.infrastructure.S3Uploader;
import botobo.core.utils.FileFactory;
import org.junit.jupiter.api.BeforeEach;
import botobo.core.dto.user.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("유저 서비스 테스트")
@MockitoSettings
public class UserServiceTest {

    private static final String CLOUDFRONT_URL_FORMAT = "https://d1mlkr1uzdb8as.cloudfront.net/%s";
    private static final String USER_DEFAULT_IMAGE = "botobo-default-profile.png";

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private UserService userService;

    private User user;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = AppUser.user(1L);
        user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
    }

    @Test
    @DisplayName("유저 id에 해당하는 유저를 조회한다. - 성공")
    void findById() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.findById(user.getId());

        // then
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getUserName()).isEqualTo(user.getUserName());
        assertThat(userResponse.getProfileUrl()).isEqualTo(user.getProfileUrl());

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 변경한다. - 성공")
    void updateProfileImage() throws IOException {
        // given
        String profileUrl = "https://botobo.com/users/user/botobo.png";
        MockMultipartFile mockMultipartFile = FileFactory.testFile("png");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(s3Uploader.upload(mockMultipartFile, user.getUserName())).willReturn(profileUrl);

        // when
        ProfileResponse profileResponse = userService.updateProfile(mockMultipartFile, appUser);

        // then
        assertThat(profileResponse.getProfileUrl()).isNotNull();

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
        then(s3Uploader)
                .should(times(1))
                .upload(mockMultipartFile, user.getUserName());
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 변경한다. - 성공, 이미지가 들어오지 않은 경우 디폴트 이미지로 대체")
    void updateProfileImageWhenEmpty() throws IOException {
        // given
        String defaultImageUrl = String.format(CLOUDFRONT_URL_FORMAT, USER_DEFAULT_IMAGE);
        MockMultipartFile mockMultipartFile = null;

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(s3Uploader.upload(mockMultipartFile, user.getUserName())).willReturn(defaultImageUrl);

        // when
        ProfileResponse profileResponse = userService.updateProfile(mockMultipartFile, appUser);

        // then
        assertThat(profileResponse.getProfileUrl()).isEqualTo(defaultImageUrl);
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 변경한다. - 실패, 유저가 존재하지 않음.")
    void updateProfileImageFailedWhenUserNotFound() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = FileFactory.testFile("png");
        given(userRepository.findById(anyLong())).willThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> userService.updateProfile(mockMultipartFile, appUser))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
        then(s3Uploader)
                .should(never())
                .upload(mockMultipartFile, user.getUserName());
    }

    @Test
    @DisplayName("유저의 정보를 변경한다. - 성공")
    void update() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("수정된 user")
                .profileUrl("profile.io")
                .bio("수정된 bio")
                .build();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.update(1L, userUpdateRequest, appUser);

        // then
        assertThat(userResponse.getProfileUrl()).isEqualTo("profile.io");
        assertThat(userResponse.getUserName()).isEqualTo(userUpdateRequest.getUserName());
        assertThat(userResponse.getBio()).isEqualTo(userUpdateRequest.getBio());

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @DisplayName("유저의 정보를 변경한다. - 실패, pathVariable의 id와 로그인한 유저의 id가 다름.")
    @Test
    void updateFailedWhenDifferentIds() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("수정된 user")
                .profileUrl("profile.io")
                .bio("수정된 bio")
                .build();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        assertThatThrownBy(() -> userService.update(2L, userUpdateRequest, appUser))
                .isInstanceOf(UserUpdateNotAllowedException.class);

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("유저의 정보를 변경한다. - 실패, profileUrl은 내 정보 수정에서 변경할 수 없다.")
    void updateFailedWhenDifferentProfileUrl() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("수정된 user")
                .profileUrl("수정된.profile.url")
                .bio("수정된 bio")
                .build();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        assertThatThrownBy(() -> userService.update(1L, userUpdateRequest, appUser))
                .isInstanceOf(ProfileUpdateNotAllowedException.class);

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }
}
