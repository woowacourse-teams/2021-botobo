package botobo.core.acceptance.workbook;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
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

    private GithubUserInfoResponse userInfo, anotherUserInfo;

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
        userInfo = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .githubId(2L)
                .profileUrl("github.io")
                .build();
        anotherUserInfo = GithubUserInfoResponse.builder()
                .userName("anotherUser")
                .githubId(3L)
                .profileUrl("github.io")
                .build();
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
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

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
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

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
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최대 30자까지 입력 가능합니다.");
    }

    // TODO: 문제집 추가 API가 생기면 전체적으로 리팩터링 해야함
    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findWorkbooksByUser() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks")
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build();

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
    }

    @Test
    @DisplayName("비회원 문제집 조회시 비어있는 리스트를 반환 - 성공")
    void findWorkbooksByAnonymous() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks")
                .build();

        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).isEmpty();
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

    @Test
    @DisplayName("공유 문제집 검색 - 성공")
    void findPublicWorkbooksBySearch() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks/public")
                .queryParam("search", "1")
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build();

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(1);
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 성공")
    void updateWorkbook() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .build();

        // when
        final HttpResponse response = 유저_문제집_수정_요청(workbookUpdateRequest, workbookResponse, userInfo);

        // then
        WorkbookResponse updateResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse).extracting("id").isNotNull();
        assertThat(updateResponse).extracting("name").isEqualTo(workbookUpdateRequest.getName());
        assertThat(updateResponse).extracting("cardCount").isEqualTo(0);
        assertThat(updateResponse).extracting("opened").isEqualTo(workbookUpdateRequest.isOpened());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("유저가 문제집 수정 - 실패, name이 없을 때")
    void updateWorkbookWhenNameNotExist(String name) {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name(name)
                .opened(true)
                .cardCount(0)
                .build();

        // when
        final HttpResponse response = 유저_문제집_수정_요청(workbookUpdateRequest, workbookResponse, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, name이 30자 초과")
    void updateWorkbookWhenNameLengthOver30() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name(stringGenerator(31))
                .opened(true)
                .cardCount(0)
                .build();

        // when
        final HttpResponse response = 유저_문제집_수정_요청(workbookUpdateRequest, workbookResponse, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최대 30자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, cardCount가 음수")
    void updateWorkbookWhenCardCountNegative() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(true)
                .cardCount(-5)
                .build();

        // when
        final HttpResponse response = 유저_문제집_수정_요청(workbookUpdateRequest, workbookResponse, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("카드 개수는 0이상 입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, 다른 유저가 수정을 시도할 때")
    void updateWorkbookWithOtherUser() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .build();
        GithubUserInfoResponse otherUserInfo = GithubUserInfoResponse.builder()
                .userName("otherUser")
                .githubId(3L)
                .profileUrl("github.io")
                .build();

        // when
        final HttpResponse response = 유저_문제집_수정_요청(workbookUpdateRequest, workbookResponse, otherUserInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);

        // when
        final HttpResponse response = 유저_문제집_삭제_요청(workbookResponse, userInfo);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 실패, 다른 유저가 삭제를 시도할 때")
    void deleteWorkbookWithOtherUser() {
        // given
        WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        GithubUserInfoResponse otherUserInfo = GithubUserInfoResponse.builder()
                .userName("otherUser")
                .githubId(3L)
                .profileUrl("github.io")
                .build();

        // when
        final HttpResponse response = 유저_문제집_삭제_요청(workbookResponse, otherUserInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 성공")
    void scrapSelectedCardsToWorkbook() {
        // given
        final Long workbookId = 유저_문제집_등록되어_있음("Spring 문제집", true, userInfo).getId();

        유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        카드_등록되어_있음("question", "answer", 1L, 1L);
        카드_등록되어_있음("question", "answer", 1L, 1L);


        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(1L, 2L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.header("Location")).isEqualTo(String.format("/api/workbooks/%d/cards", workbookId));
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집이 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenWorkbookNotExist() {
        // given
        final Long workbookId = 100L;
        유저_문제집_등록되어_있음("Java 문제집", true, userInfo);
        카드_등록되어_있음("question", "answer", 1L, 1L);
        카드_등록되어_있음("question", "answer", 1L, 1L);


        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(1L, 2L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 유저가 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenUserNotFound() {
        // given
        유저_문제집_등록되어_있음("유저가 존재하지 않는 문제집", true, anotherUserInfo);
        final Long workbookId = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo).getId();
        카드_등록되어_있음("question", "answer", 1L, 1L);
        카드_등록되어_있음("question", "answer", 1L, 1L);


        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(1L, 2L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 존재하지 않는 Card Id")
    void scrapSelectedCardsToWorkbookFailedWhenCardNotFound() {
        // given
        유저_문제집_등록되어_있음("Spring 문제집", true, anotherUserInfo);
        final Long workbookId = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo).getId();
        카드_등록되어_있음("question", "answer", 1L, 1L);
        카드_등록되어_있음("question", "answer", 1L, 1L);


        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(100L, 101L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 카드를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집의 작성자가 아닌 유저")
    void scrapSelectedCardsToWorkbookFailedWhenNotAuthor() {
        // given
        유저_문제집_등록되어_있음("Spring 문제집", true, userInfo);
        final Long workbookId = 유저_문제집_등록되어_있음("Java 문제집", true, userInfo).getId();
        카드_등록되어_있음("question", "answer", 1L, 1L);
        카드_등록되어_있음("question", "answer", 1L, 1L);


        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(100L, 101L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(로그인되어_있음(anotherUserInfo).getAccessToken())
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    private WorkbookResponse 유저_문제집_등록되어_있음(String name, boolean opened, GithubUserInfoResponse userInfo) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .build();
        return 유저_문제집_등록되어_있음(workbookRequest, userInfo);
    }

    private WorkbookResponse 유저_문제집_등록되어_있음(WorkbookRequest workbookRequest, GithubUserInfoResponse userInfo) {
        return 유저_문제집_생성_요청(workbookRequest, userInfo).convertBody(WorkbookResponse.class);
    }

    private HttpResponse 유저_문제집_생성_요청(WorkbookRequest workbookRequest, GithubUserInfoResponse userInfo) {
        return request()
                .post("/api/workbooks", workbookRequest)
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();
    }

    private HttpResponse 유저_문제집_수정_요청(WorkbookUpdateRequest workbookUpdateRequest,
                                      WorkbookResponse workbookResponse,
                                      GithubUserInfoResponse userInfo) {
        return request()
                .put("/api/workbooks/{id}", workbookUpdateRequest, workbookResponse.getId())
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();
    }

    private HttpResponse 유저_문제집_삭제_요청(WorkbookResponse workbookResponse, GithubUserInfoResponse userInfo) {
        return request()
                .delete("/api/workbooks/{id}", workbookResponse.getId())
                .auth(로그인되어_있음(userInfo).getAccessToken())
                .build();
    }

    private TokenResponse 로그인되어_있음(GithubUserInfoResponse userInfo) {
        ExtractableResponse<Response> response = 로그인_요청(userInfo);
        return response.as(TokenResponse.class);
    }

    private ExtractableResponse<Response> 로그인_요청(GithubUserInfoResponse userInfo) {
        LoginRequest loginRequest = new LoginRequest("githubCode");

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(userInfo);

        return request()
                .post("/api/login", loginRequest)
                .build()
                .extract();
    }
}
