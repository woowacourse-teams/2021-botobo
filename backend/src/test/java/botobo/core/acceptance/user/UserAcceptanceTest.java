package botobo.core.acceptance.user;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserFilterResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_OZ;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_TAG_REQUEST;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_WORKBOOK_REQUEST;
import static botobo.core.acceptance.utils.Fixture.USER_RESPONSE_OF_PK;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

class UserAcceptanceTest extends DomainAcceptanceTest {

    @Value("${aws.user-default-image}")
    private String userDefaultImage;

    @Value("${aws.cloudfront.url-format}")
    private String cloudfrontUrlFormat;

    List<WorkbookRequest> workbookRequests;

    @BeforeEach
    void setFixtures() {
        workbookRequests = List.of(
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "Java",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "자바스크립트"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "리액트")
                        )
                ),
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "JAVAVA",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "리액트"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "네트워크")
                        )
                ),
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "Javascript",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바스크립트"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "스프링"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "네트워크")
                        )
                )
        );
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        // given, when
        final HttpResponse response = request()
                .get("/users/me")
                .auth(소셜_로그인되어_있음(USER_RESPONSE_OF_PK, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("pk");
        assertThat(userResponse.getProfileUrl()).isEqualTo("pk.profile");
        assertThat(userResponse.getBio()).isEmpty();
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 실패, 토큰이 없을 경우")
    void findByUserOfMineWhenNotExistToken() {
        //when
        final HttpResponse response = request()
                .get("/users/me")
                .build();
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getMessage()).isEqualTo("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("프로필 이미지를 수정한다. - 성공, multipartFile이 비어있는 경우 디폴트 유저 이미지로 대체")
    void updateWhenDefaultProfile() {
        //given
        String defaultUserImageUrl = String.format(cloudfrontUrlFormat, userDefaultImage);
        UserInfoResponse userInfoResponse = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl(defaultUserImageUrl)
                .build();
        MockMultipartFile mockMultipartFile = null;

        //when
        final HttpResponse response = request()
                .post("/users/profile", mockMultipartFile)
                .auth(소셜_로그인되어_있음(userInfoResponse, SocialType.GITHUB))
                .build();

        ProfileResponse profileResponse = response.convertBody(ProfileResponse.class);

        //then
        assertThat(profileResponse.getProfileUrl()).isNotNull();
        assertThat(profileResponse.getProfileUrl()).isEqualTo(defaultUserImageUrl);
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 성공")
    void update() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("new조앤");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
        assertThat(userResponse.getBio()).isEqualTo("new 소개글");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, profileUrl이 다름.")
    void updateFailedDifferentProfileUrl() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("another.profile.url")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("프로필 이미지 수정은 불가합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 null이 될 수 없다.")
    void updateFailedUserNameIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName(null)
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 최소 한 글자 이상")
    void updateFailedEmptyUserName() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("")
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 최대 20자")
    void updateFailedLongUserName() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName(stringGenerator(21))
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 프로필 사진 필드는 null이 될 수 없다.")
    void updateFailedProfileUrlIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl(null)
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 프로필 사진이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 프로필 사진 필드는 ''이 될 수 없다.")
    void updateFailedProfileUrlIsEmpty() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 프로필 사진이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 소개글은 null이 될 수 없다.")
    void updateFailedBioIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("github.io")
                .bio(null)
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 소개글은 최소 0자 이상이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 소개글은 최대 255자까지만 가능하다.")
    void updateFailedLongBio() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("github.io")
                .bio(stringGenerator(256))
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("소개글은 최대 255자까지 가능합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 회원명은 중복될 수 없다.")
    void updateFailedDuplicateUserName() {
        //given
        Long id = USER_JOANNE.getId();// 유저 2번을 save한다. {이름 : "joanne"}
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("pk") // 기존에 존재하는 USER_PK의 이름으로 변경한다.
                .profileUrl("github.io")
                .bio("안녕하세요~")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(id))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(errorResponse.getMessage()).isEqualTo("이미 존재하는 회원 이름입니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 성공, 요청으로 들어온 이름이 기존 이름인 경우에는 수정 가능")
    void updateFailedDuplicateUserNameButItsMe() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("admin") // 기존에 존재하는 1번 유저가 이름 변경없이 admin 이름으로 변경한다.
                .profileUrl("github.io")
                .bio("안녕하세요~")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        // then
        UserResponse userResponse = response.convertBody(UserResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("admin");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
        assertThat(userResponse.getBio()).isEqualTo("안녕하세요~");
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "카일 안녕", "나는 조앤 하이", "  ", "    ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 회원명에는 공백이 포함될 수 없다.")
    void updateFailedWhiteSpaceUserName(String name) {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName(name)
                .profileUrl("github.io")
                .bio("안녕하세요~")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원명에 공백은 포함될 수 없습니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 성공, 변경사항이 없어도 요청에서는 실패하지 않는다.")
    void updateWhenRequestIsSame() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("admin") // 기존에 존재하는 1번 유저의 admin 이름으로 변경한다.
                .profileUrl("github.io")
                .bio("")
                .build();
        //when
        final HttpResponse response = request()
                .put("/users/me", userUpdateRequest)
                .auth(createToken(1L))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("admin");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
        assertThat(userResponse.getBio()).isEmpty();
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 성공, 중복되지 않은 이름")
    void checkSameUserNameAlreadyExist() {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("클러치박")
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 성공, 현재 로그인한 회원의 이름과 동일할 때에도 OK를 보낸다.")
    void checkSameUserNameAlreadyExistWhenSameUser() {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("admin")
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 실패, 중복된 이름")
    void checkSameUserNameAlreadyExistFailed() {
        //given
        Long id = USER_JOANNE.getId();// 유저 2번을 save한다. {이름 : "joanne"}
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("pk")
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(id))
                .build();

        ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(errorResponse.getMessage()).isEqualTo("이미 존재하는 회원 이름입니다.");
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 실패, 요청 회원 명은 null이 될 수 없다.")
    void checkSameUserNameAlreadyExistFailedWhenNameIsNull() {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName(null)
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 실패, 요청 회원 명은 최소 한글자 이상이어야한다.")
    void checkSameUserNameAlreadyExistFailedWhenNameIsEmpty() {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("")
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("회원명을 중복 조회한다. - 실패, 요청 회원 명은 최대 20글자까지 가능하다.")
    void checkSameUserNameAlreadyExistFailedWhenNameIsLong() {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName(stringGenerator(21))
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "카일 안녕", "나는 조앤 하이", "  ", "    ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName에는 공백이 포함될 수 없다.")
    void updateFailedWithWhiteSpace(String name) {
        //given
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName(name)
                .build();
        //when
        final HttpResponse response = request()
                .post("/users/name-check", userNameRequest)
                .auth(createToken(1L))
                .build();

        //then
        ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원명에 공백은 포함될 수 없습니다.");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 카드의 개수가 0개이면 가져오지 않는다.")
    @Test
    void findAllUsersByWorkbookNameWhenCardIsEmpty() {
        서로_다른_유저의_여러개_문제집_생성_요청(workbookRequests, USERS);

        // given
        final String workbookName = "Java";
        final HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).isEmpty();
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공")
    @Test
    void findAllUsersByWorkbookName() {
        유저_카드_포함_문제집_등록되어_있음_1("Java 문제집", true, USER_OZ);
        유저_카드_포함_문제집_등록되어_있음_1("Spring 문제집", true, USER_JOANNE);

        // given
        final String workbookName = "Java";
        final HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("oz");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 태그에 포함됨")
    @Test
    void findAllUsersByWorkbookNameEqualsTag() {
        List<TagRequest> jsTags = Arrays.asList(
                TagRequest.builder().id(0L).name("javascript").build(),
                TagRequest.builder().id(0L).name("js").build()
        );

        유저_태그_카드_포함_문제집_등록되어_있음_1("Js 문제집", true, jsTags, USER_JOANNE);
        유저_카드_포함_문제집_등록되어_있음_1("Java 문제집", true, USER_OZ);
        유저_카드_포함_문제집_등록되어_있음_1("Spring 문제집", true, USER_JOANNE);

        // given
        final String workbookName = "Js";
        final HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("joanne");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 비공개 문제집의 경우 작성자를 가져오지 않는다.")
    @Test
    void findAllUsersByWorkbookNameWhenOpened() {
        유저_카드_포함_문제집_등록되어_있음_1("Java 문제집", true, USER_OZ);
        유저_카드_포함_문제집_등록되어_있음_1("Spring 문제집", false, USER_JOANNE);

        // given
        final String workbookName = "문제집";
        final HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("oz");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 문제집명이 비어있는 경우 빈 응답")
    @Test
    void findAllUsersByWorkbookNameIsEmpty() {
        서로_다른_유저의_여러개_문제집_생성_요청(workbookRequests, USERS);

        // given
        final String workbookName = "";
        final HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).isEmpty();
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 실패, 문제집명이 30글자를 넘는 비정상적 경우")
    @Test
    void findAllUsersByWorkbookNameIsLong() {
        // given
        final HttpResponse response = request()
                .get("/users?workbook=" + stringGenerator(31))
                .build();

        // when - then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
