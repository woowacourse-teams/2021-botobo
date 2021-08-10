package botobo.core.acceptance.user;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends DomainAcceptanceTest {

    @Value("${aws.user-default-image}")
    private String userDefaultImage;

    @Value("${aws.cloudfront.url-format}")
    private String cloudFrontUrlFormat;

    @BeforeEach
    void setFixture() {
        userInfo = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl("social.io")
                .build();

    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        // given, when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(소셜_로그인되어_있음(userInfo, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("socialUser");
        assertThat(userResponse.getProfileUrl()).isEqualTo("social.io");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 실패, 토큰이 없을 경우")
    void findByUserOfMineWhenNotExistToken() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
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
        MockMultipartFile mockMultipartFile = null;
        String defaultUserImageUrl = String.format(cloudFrontUrlFormat, userDefaultImage);

        //when
        final HttpResponse response = request()
                .post("/api/users/profile", mockMultipartFile)
                .auth(소셜_로그인되어_있음(userInfo, SocialType.GITHUB))
                .build();

        ProfileResponse profileResponse = response.convertBody(ProfileResponse.class);

        //then
        assertThat(profileResponse.getProfileUrl()).isNotNull();
        assertThat(profileResponse.getProfileUrl()).isEqualTo(defaultUserImageUrl);
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공, 다른 유저와 회원명이 같을 경우 뒤에 숫자가 추가된다. (숫자는 차례대로 1씩 증가)")
    void findByUserOfMineWithSameUserName() {
        // given
        소셜_로그인_요청(userInfo, SocialType.GITHUB);
        UserInfoResponse otherUserInfo = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("3")
                .profileUrl("github.io")
                .build();
        소셜_로그인_요청(otherUserInfo, SocialType.GITHUB);

        // when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(소셜_로그인되어_있음(otherUserInfo, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("socialUser1");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
    }

    @Test
    @DisplayName("로그인 한 유저의 회원명을 비교한다. - 성공, 다른 유저와 회원명이 같을 경우 뒤에 숫자가 추가된다. (동일 회원명이 2개일 경우)")
    void findByUserOfMineWithTwoSameUserName() {
        // given
        소셜_로그인_요청(userInfo, SocialType.GITHUB);
        UserInfoResponse otherUserInfo1 = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("3")
                .profileUrl("github.io")
                .build();
        소셜_로그인_요청(otherUserInfo1, SocialType.GITHUB);
        UserInfoResponse otherUserInfo2 = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("4")
                .profileUrl("github.io")
                .build();


        // when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(소셜_로그인되어_있음(otherUserInfo2, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("socialUser2");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
    }

    @Test
    @DisplayName("로그인 한 유저의 회원명을 비교한다. - 성공, 다른 유저와 회원명이 다른 경우")
    void findByUserOfMineWithDifferentUserName() {
        // given
        소셜_로그인_요청(userInfo, SocialType.GITHUB);

        UserInfoResponse otherUserInfo = GithubUserInfoResponse.builder()
                .userName("oz")
                .socialId("3")
                .profileUrl("github.io")
                .build();

        // when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(소셜_로그인되어_있음(otherUserInfo, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("oz");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
    }
}
