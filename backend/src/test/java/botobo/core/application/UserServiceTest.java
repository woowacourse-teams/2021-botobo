package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserQueryRepository;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserFilterResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import botobo.core.exception.user.UserNameDuplicatedException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.infrastructure.s3.FileUploader;
import botobo.core.utils.FileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("?????? ????????? ?????????")
@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    private static final String CLOUDFRONT_URL_FORMAT = "https://d1mlkr1uzdb8as.cloudfront.net/%s";
    private static final String USER_DEFAULT_IMAGE = "imagesForS3Test/botobo-default-profile.png";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserQueryRepository userQueryRepository;

    @MockBean
    private FileUploader s3Uploader;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    private User userHasCard, user1, user2;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        Card card = Card.builder()
                .question("??????")
                .answer("??????")
                .build();

        Workbook workbook = Workbook.builder()
                .name("?????????")
                .cards(new Cards(List.of(card)))
                .build();

        appUser = AppUser.user(1L);
        userHasCard = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user1")
                .profileUrl("profile.io")
                .workbooks(List.of(workbook))
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        user1 = User.builder()
                .id(2L)
                .socialId("2")
                .userName("user2")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        user2 = User.builder()
                .id(3L)
                .socialId("3")
                .userName("user3")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();
    }

    @Test
    @DisplayName("?????? id??? ???????????? ????????? ????????????. - ??????")
    void findById() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));

        // when
        UserResponse userResponse = userService.findById(appUser);

        // then
        assertThat(userResponse.getId()).isEqualTo(userHasCard.getId());
        assertThat(userResponse.getUserName()).isEqualTo(userHasCard.getUserName());
        assertThat(userResponse.getProfileUrl()).isEqualTo(userHasCard.getProfileUrl());

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ????????????. - ??????")
    void updateProfileImage() throws IOException {
        // given
        String profileUrl = "https://botobo.com/users/user/botobo.png";
        MockMultipartFile mockMultipartFile = FileFactory.testFile("png");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));
        given(s3Uploader.upload(mockMultipartFile, userHasCard)).willReturn(profileUrl);

        // when
        ProfileResponse profileResponse = userService.updateProfile(mockMultipartFile, appUser);

        // then
        assertThat(profileResponse.getProfileUrl()).isNotNull();

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
        then(s3Uploader)
                .should(times(1))
                .upload(mockMultipartFile, userHasCard);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ????????????. - ??????, ???????????? ???????????? ?????? ?????? ????????? ???????????? ??????")
    void updateProfileImageWhenEmpty() throws IOException {
        // given
        String defaultImageUrl = String.format(CLOUDFRONT_URL_FORMAT, USER_DEFAULT_IMAGE);
        MockMultipartFile mockMultipartFile = null;

        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));
        given(s3Uploader.upload(mockMultipartFile, userHasCard)).willReturn(defaultImageUrl);

        // when
        ProfileResponse profileResponse = userService.updateProfile(mockMultipartFile, appUser);

        // then
        assertThat(profileResponse.getProfileUrl()).isEqualTo(defaultImageUrl);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ????????????. - ??????, ????????? ???????????? ??????.")
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
                .upload(mockMultipartFile, userHasCard);
    }

    @Test
    @DisplayName("????????? ????????? ????????????. - ??????, ??????????????? ????????? ????????? ???????????? ?????????.")
    void updateWithSameInfo() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("user")
                .profileUrl("profile.io")
                .bio("")
                .build();

        given(userRepository.findByUserName(userUpdateRequest.getUserName())).willReturn(Optional.of(userHasCard));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));

        // when
        UserResponse userResponse = userService.update(userUpdateRequest, appUser);

        // then
        assertThat(userResponse.getProfileUrl()).isEqualTo("profile.io");
        assertThat(userResponse.getUserName()).isEqualTo(userUpdateRequest.getUserName());
        assertThat(userResponse.getBio()).isEqualTo(userUpdateRequest.getBio());

        then(userRepository)
                .should(times(1))
                .findByUserName(userUpdateRequest.getUserName());
        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ????????????. - ??????")
    void update() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("????????? user")
                .profileUrl("profile.io")
                .bio("????????? bio")
                .build();

        given(userRepository.findByUserName(userUpdateRequest.getUserName())).willReturn(Optional.empty());
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));

        // when
        UserResponse userResponse = userService.update(userUpdateRequest, appUser);

        // then
        assertThat(userResponse.getProfileUrl()).isEqualTo("profile.io");
        assertThat(userResponse.getUserName()).isEqualTo(userUpdateRequest.getUserName());
        assertThat(userResponse.getBio()).isEqualTo(userUpdateRequest.getBio());

        then(userRepository)
                .should(times(1))
                .findByUserName(userUpdateRequest.getUserName());
        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ????????????. - ??????, profileUrl??? ??? ?????? ???????????? ????????? ??? ??????.")
    void updateFailedWhenDifferentProfileUrl() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("????????? user")
                .profileUrl("?????????.profile.url")
                .bio("????????? bio")
                .build();
        given(userRepository.findByUserName(userUpdateRequest.getUserName())).willReturn(Optional.empty());
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userHasCard));

        // when
        assertThatThrownBy(() -> userService.update(userUpdateRequest, appUser))
                .isInstanceOf(ProfileUpdateNotAllowedException.class);

        then(userRepository)
                .should(times(1))
                .findByUserName(userUpdateRequest.getUserName());
        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ????????????. - ??????, userName??? ????????? ??? ??????.")
    void updateFailedWhenDuplicatedUserName() {
        // given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("??????_????????????_??????")
                .profileUrl("?????????.profile.url")
                .bio("????????? bio")
                .build();

        given(userRepository.findByUserName(userUpdateRequest.getUserName())).willThrow(UserNameDuplicatedException.class);

        // when
        assertThatThrownBy(() -> userService.update(userUpdateRequest, appUser))
                .isInstanceOf(UserNameDuplicatedException.class);

        then(userRepository)
                .should(times(1))
                .findByUserName(userUpdateRequest.getUserName());
        then(userRepository)
                .should(never())
                .findById(anyLong());
    }

    @Test
    @DisplayName("???????????? ?????? ????????????. - ??????")
    void checkSameUserNameAlreadyExist() {
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("???????????????.")
                .build();

        given(userRepository.findByUserName(userNameRequest.getUserName())).willReturn(Optional.empty());

        assertThatCode(() -> userService.checkDuplicatedUserName(userNameRequest, appUser))
                .doesNotThrowAnyException();

        then(userRepository)
                .should(times(1))
                .findByUserName(userNameRequest.getUserName());
    }

    @Test
    @DisplayName("???????????? ?????? ????????????. - ??????, ????????? ????????? ????????? ??????")
    void checkSameUserNameAlreadyExistWithSameUser() {
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("user")
                .build();

        given(userRepository.findByUserName(userNameRequest.getUserName())).willReturn(Optional.of(userHasCard));

        assertThatCode(() -> userService.checkDuplicatedUserName(userNameRequest, appUser))
                .doesNotThrowAnyException();

        then(userRepository)
                .should(times(1))
                .findByUserName(userNameRequest.getUserName());
    }

    @Test
    @DisplayName("???????????? ?????? ????????????. - ??????, ???????????? ??????")
    void checkSameUserNameAlreadyExistFailed() {
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("??????_????????????_??????")
                .build();

        given(userRepository.findByUserName(userNameRequest.getUserName())).willThrow(UserNameDuplicatedException.class);

        assertThatThrownBy(() -> userService.checkDuplicatedUserName(userNameRequest, appUser))
                .isInstanceOf(UserNameDuplicatedException.class);

        then(userRepository)
                .should(times(1))
                .findByUserName(userNameRequest.getUserName());
    }

    @DisplayName("??????????????? ????????? ???????????? ????????? ?????? ????????????. - ??????, ?????? ????????? ?????? ?????? ????????? ??? ????????? ????????????.")
    @EmptySource
    @ParameterizedTest
    void findAllUsersByWorkbookNameEmpty(String workbook) {
        // given
        FilterCriteria filterCriteria = new FilterCriteria(workbook);

        // when
        List<UserFilterResponse> responses = userService.findAllUsersByWorkbookName(filterCriteria);

        // then
        assertThat(responses).isEmpty();
        then(userQueryRepository)
                .should(never())
                .findAllByContainsWorkbookName(filterCriteria.getWorkbook());
        clearCache();
    }

    @DisplayName("??????????????? ????????? ???????????? ????????? ?????? ????????????. - ??????")
    @Test
    void findAllUsersByWorkbookName() {
        // given
        FilterCriteria filterCriteria = new FilterCriteria("??????");
        given(userQueryRepository.findAllByContainsWorkbookName(filterCriteria.getWorkbook()))
                .willReturn(List.of(userHasCard, user1, user2));

        // when
        List<UserFilterResponse> responses = userService.findAllUsersByWorkbookName(filterCriteria);

        // then
        assertThat(responses).hasSize(3);
        then(userQueryRepository)
                .should(times(1))
                .findAllByContainsWorkbookName(filterCriteria.getWorkbook());
        clearCache();
    }

    @DisplayName("??????????????? ????????? ???????????? ????????? ?????? ????????????. - ??????, ????????? ????????? ????????? DB??? ???????????? ?????? ????????? ?????? ????????? ??????")
    @Test
    void findAllUsersByWorkbookNameCache() {
        // given
        FilterCriteria filterCriteria = new FilterCriteria("??????");
        given(userQueryRepository.findAllByContainsWorkbookName(filterCriteria.getWorkbook()))
                .willReturn(List.of(userHasCard, user1, user2));

        // when
        userService.findAllUsersByWorkbookName(filterCriteria);
        userService.findAllUsersByWorkbookName(filterCriteria);
        List<UserFilterResponse> responses = userService.findAllUsersByWorkbookName(filterCriteria);

        // then
        assertThat(responses).hasSize(3);
        then(userQueryRepository)
                .should(times(1))
                .findAllByContainsWorkbookName(filterCriteria.getWorkbook());
        clearCache();
    }

    private void clearCache() {
        Cache users = cacheManager.getCache("filterUsers");
        Objects.requireNonNull(users).clear();
    }
}
