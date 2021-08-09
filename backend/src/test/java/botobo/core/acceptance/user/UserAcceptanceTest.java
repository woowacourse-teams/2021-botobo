package botobo.core.acceptance.user;

import botobo.core.acceptance.AuthAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AuthAcceptanceTest {

    @Value("${aws.user-default-image}")
    private String userDefaultImage;

    @Value("${aws.cloudfront.url-format}")
    private String cloudFrontUrlFormat;

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(로그인되어_있음().getAccessToken())
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("githubUser");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
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
                .auth(로그인되어_있음().getAccessToken())
                .build();

        ProfileResponse profileResponse = response.convertBody(ProfileResponse.class);

        //then
        assertThat(profileResponse.getProfileUrl()).isNotNull();
        assertThat(profileResponse.getProfileUrl()).isEqualTo(defaultUserImageUrl);
    }
}
