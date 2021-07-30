package botobo.core.acceptance.workbook;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.tag.TagRequest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private GithubUserInfoResponse userInfo;

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
        userInfo = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .githubId(2L)
                .profileUrl("github.io")
                .build();
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 성공")
    void createWorkbookByUser() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
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
        assertThat(workbookResponse.getTags()).hasSize(1);
        assertThat(workbookResponse.getTags().get(0).getId()).isNotEqualTo(0L);
        assertThat(workbookResponse.getTags().get(0).getName()).isEqualTo("자바");
    }

    @Test
    @DisplayName("유저 문제집 추가시 opened와 tags는 필수가 아니다 - 기본값 (opened = false, tags = empty list)")
    void createWorkbookByUserWithTags() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .tags(new ArrayList<>())
                .build();

        // when
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        WorkbookResponse workbookResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(workbookResponse.getOpened()).isFalse();
        assertThat(workbookResponse.getTags()).isEmpty();
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

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 아이디 없음")
    void createWorkbookByUserWhenTagIdNull() {
        // given
        TagRequest tagRequest = TagRequest.builder().name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 아이디 음수")
    void createWorkbookByUserWhenTagIdNegative() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(-1L).name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 0이상의 숫자입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 이름 없음")
    void createWorkbookByUserWhenTagNameNull() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("이름은 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, 20자를 초과하는 Tag 이름")
    void createWorkbookByUserWhenTagNameLong() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).name(stringGenerator(21)).build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = 유저_문제집_생성_요청(workbookRequest, userInfo);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그는 최대 20자까지 입력 가능합니다");
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
        assertThat(updateResponse).extracting("opened").isEqualTo(workbookUpdateRequest.getOpened());
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
