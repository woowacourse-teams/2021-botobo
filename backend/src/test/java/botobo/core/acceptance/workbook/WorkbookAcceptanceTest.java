package botobo.core.acceptance.workbook;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.exception.ErrorResponse;
import botobo.core.infrastructure.GithubOauthManager;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static botobo.core.utils.Fixture.CARD_REQUEST_1;
import static botobo.core.utils.Fixture.CARD_REQUEST_2;
import static botobo.core.utils.Fixture.CARD_REQUEST_3;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_2;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_3;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Workbook 인수 테스트")
public class WorkbookAcceptanceTest extends DomainAcceptanceTest {

    @MockBean
    private GithubOauthManager githubOauthManager;

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 성공")
    void createWorkbookByUser() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks", workbookRequest)
                .auth(로그인되어_있음().getAccessToken())
                .build();

        // then
        WorkbookResponse workbookResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(workbookResponse).extracting("id").isNotNull();
        assertThat(workbookResponse).extracting("name").isEqualTo(workbookRequest.getName());
        assertThat(workbookResponse).extracting("cardCount").isEqualTo(0);
        assertThat(workbookResponse).extracting("opened").isEqualTo(workbookRequest.isOpened());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("유저가 문제집 추가 - 실패, name이 없을 때")
    void createWorkbookByUserWhenNameNotExist(String name) {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(true)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks", workbookRequest)
                .auth(로그인되어_있음().getAccessToken())
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, name이 30자 초과")
    void createWorkbookByUserWhenNameLengthOver30() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(stringGenerator(31))
                .opened(true)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks", workbookRequest)
                .auth(로그인되어_있음().getAccessToken())
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최대 30자까지 입력 가능합니다.");
    }

    // TODO: 유저가 추가한 문제집을 조회하는걸로 바꿔야함
    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAllWorkbooks() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks")
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build();

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses.size()).isEqualTo(3);
    }
    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 존재) - 성공")
    void findCategoryCardsById() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", 1L)
                .auth()
                .build();
        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(WORKBOOK_REQUEST_1.getName());
        assertThat(workbookCardResponse.getCards()).hasSize(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 0개) - 성공")
    void findWorkbookCardsByIdWithNotExistsCard() {
        // when
        HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", 2L)
                .auth()
                .build();

        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(WORKBOOK_REQUEST_2.getName());
        assertThat(workbookCardResponse.getCards()).isEmpty();
    }

    private TokenResponse 로그인되어_있음() {
        ExtractableResponse<Response> response = 로그인_요청();
        return response.as(TokenResponse.class);
    }

    private ExtractableResponse<Response> 로그인_요청() {
        LoginRequest loginRequest = new LoginRequest("githubCode");
        GithubUserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .githubId(1L)
                .profileUrl("github.io")
                .build();

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);

        return request()
                .post("/api/login", loginRequest)
                .build()
                .extract();
    }
}
