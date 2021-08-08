package botobo.core.acceptance.search;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.user.SimpleUserResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchAcceptanceTest extends DomainAcceptanceTest {

    private String pkToken, bearToken;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        initializeUsers();
        initializeWorkbooks();
    }

    private void initializeUsers() {
        GithubUserInfoResponse pk = GithubUserInfoResponse.builder()
                .userName("pk")
                .githubId(10L)
                .profileUrl("pk.profile")
                .build();


        GithubUserInfoResponse bear = GithubUserInfoResponse.builder()
                .userName("bear")
                .githubId(20L)
                .profileUrl("bear.profile")
                .build();

        pkToken = 로그인되어_있음(pk);
        bearToken = 로그인되어_있음(bear);
    }

    private void initializeWorkbooks() {
        // pk의 문제집 생성
        카드도_함께_등록(
                유저_문제집_등록되어_있음("피케이의 자바 기초 문제집", true, makeJavaTags(), pkToken),
                2,
                pkToken
        );
        카드도_함께_등록(
                유저_문제집_등록되어_있음("피케이의 자바 중급 문제집", true, makeJavaTags(), pkToken),
                5,
                pkToken
        );
        카드도_함께_등록(
                유저_문제집_등록되어_있음("피케이의 자바 고급 문제집", true, makeJavaTags(), pkToken),
                10,
                pkToken
        );
        카드도_함께_등록(
                유저_문제집_등록되어_있음("네트워크는 나의 것", true, makeNetworkTags(), pkToken),
                1,
                pkToken
        );

        // 중간곰의 문제집 생성
        카드도_함께_등록(
                유저_문제집_등록되어_있음("중간곰의 자바 기초 문제집", true, makeJavaTags(), bearToken),
                1,
                bearToken
        );
        유저_문제집_등록되어_있음("중간곰의 자바 중급 문제집", true, makeJavaTags(), bearToken);
    }

    private List<TagRequest> makeJavaTags() {
        return Arrays.asList(
                TagRequest.builder().id(0L).name("java").build(),
                TagRequest.builder().id(0L).name("자바").build()
        );
    }

    private List<TagRequest> makeNetworkTags() {
        return Collections.singletonList(
                TagRequest.builder().id(0L).name("network").build()
        );
    }

    private void 카드도_함께_등록(WorkbookResponse workbookResponse, int cardCount, String accessToken) {
        Long workbookId = workbookResponse.getId();
        IntStream.rangeClosed(1, cardCount)
                .forEach(number -> 카드_등록되어_있음("질문", "정답", workbookId, accessToken));
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 검색어를 제외한 다른 인자는 생략 가능")
    void searchWithDefault() {
        // given
        Map<String, String> parameters = Map.of("keyword", "java");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 문제집 이름으로 검색")
    void searchFromNameType() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "기초");
        parameters.put("type", "name");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(2);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .allMatch(name -> name.contains("기초"));
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 태그 이름으로 검색")
    void searchFromTagType() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("type", "tag");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        workbookResponses.forEach(workbookResponse ->
                assertThat(workbookResponse.getTags()).extracting(TagResponse::getName).contains("java")
        );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 유저 이름으로 검색")
    void searchFromUserType() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses).extracting(WorkbookResponse::getAuthor)
                .allMatch(userName -> userName.equals("pk"));
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 시간 기준 최신 순 정렬")
    void searchFromDateDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "date");
        parameters.put("order", "desc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "네트워크는 나의 것",
                        "피케이의 자바 고급 문제집",
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 시간 기준 오랜 순 정렬")
    void searchFromDateAsc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "date");
        parameters.put("order", "asc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바 기초 문제집",
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 고급 문제집",
                        "네트워크는 나의 것"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 사전 역순 정렬")
    void searchFromNameDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "name");
        parameters.put("order", "desc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 기초 문제집",
                        "피케이의 자바 고급 문제집",
                        "네트워크는 나의 것"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 사전 순 정렬")
    void searchFromNameAsc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "name");
        parameters.put("order", "asc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "네트워크는 나의 것",
                        "피케이의 자바 고급 문제집",
                        "피케이의 자바 기초 문제집",
                        "피케이의 자바 중급 문제집"
                );
    }

    // TODO : 좋아요 기능 생기면 추가
    @Test
    @DisplayName("문제집 검색 - 성공, 좋아요 많은 순 정렬")
    void searchFromHeartDesc() {

    }

    @Test
    @DisplayName("문제집 검색 - 성공, 좋아요 적은 순 정렬")
    void searchFromHeartAsc() {

    }

    @Test
    @DisplayName("문제집 검색 - 성공, 카드 많은 순 정렬")
    void searchFromCountDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "count");
        parameters.put("order", "desc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바 고급 문제집",
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 기초 문제집",
                        "네트워크는 나의 것"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 카드 적은 순 정렬")
    void searchFromCountAsc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "count");
        parameters.put("order", "asc");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "네트워크는 나의 것",
                        "피케이의 자바 기초 문제집",
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 고급 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 요청한 크기보다 문제집을 조금 가진 경우 가지고 있는 문제집만 보여준다.")
    void searchWithBiggerSizeThanStored() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "date");
        parameters.put("order", "desc");
        parameters.put("size", "50");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "네트워크는 나의 것",
                        "피케이의 자바 고급 문제집",
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 페이징 이용")
    void searchWithPaging() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "pk");
        parameters.put("type", "user");
        parameters.put("criteria", "date");
        parameters.put("order", "desc");
        parameters.put("start", "1");
        parameters.put("size", "2");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(2);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바 중급 문제집",
                        "피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 카드의 수가 0개인 문제집은 조회되지 않음")
    void searchExceptZeroCards() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "bear");
        parameters.put("type", "user");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(1);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getCardCount)
                .allMatch(cardCount -> cardCount > 0);
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 검색어 없음")
    void searchWithNoKeyword() {
        // given
        Map<String, String> parameters = Map.of("type", "name");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 null일 수 없습니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 30자를 초과하는 검색어")
    void searchWithLongKeyword() {
        // given
        Map<String, String> parameters = Map.of("keyword", stringGenerator(31));

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 30자 이하여야 합니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 검색 타입")
    void searchWithInvalidType() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("type", "alphabet");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 검색 타입입니다. 유효한 검색 타임 : name, tag, user");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 정렬 기준")
    void searchWithInvalidCriteria() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("criteria", "random");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 정렬 조건입니다. 유효한 정렬 조건 : date, name, count, heart");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 정렬 방법")
    void searchWithInvalidOrder() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("order", "center");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 정렬 방향입니다. 유효한 정렬 방식 : ASC, DESC");
    }

    @Test
    @DisplayName("문제집 검색 - 올바르지 않은 시작 페이지")
    void searchWithInvalidStart() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("start", "-1");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("페이지의 시작 값은 음수가 될 수 없습니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "101"})
    @DisplayName("문제집 검색 - 올바르지 않은 페이지 크기")
    void searchWithInvalidSize(String size) {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("size", size);

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 페이지 크기입니다. 유효한 크기 : 1 ~ 100");
    }

    @Test
    @DisplayName("태그 검색 - 성공")
    void searchTagsWithKeyword() {
        // given
        String keyword = "ava";

        // when
        HttpResponse response = 태그_검색_요청(keyword);

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).hasSize(1);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .allMatch(name -> name.contains(keyword));
    }

    @Test
    @DisplayName("유저 검색 - 성공")
    void searchUsersWithKeyword() {
        // given
        String keyword = "ear";

        // when
        HttpResponse response = 유저_검색_요청(keyword);

        // then
        List<SimpleUserResponse> searchUserResponse = response.convertBodyToList(SimpleUserResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchUserResponse).hasSize(1);
        assertThat(searchUserResponse)
                .extracting(SimpleUserResponse::getName)
                .allMatch(name -> name.contains(keyword));
    }

    private HttpResponse 문제집_검색_요청(Map<String, String> parameters) {
        return request()
                .get("/api/search/workbooks")
                .queryParams(parameters)
                .auth(createToken(user.getId()))
                .build();
    }

    private HttpResponse 태그_검색_요청(String keyword) {
        return request()
                .get("/api/search/tags")
                .queryParam("keyword", keyword)
                .auth(createToken(user.getId()))
                .build();
    }

    private HttpResponse 유저_검색_요청(String keyword) {
        return request()
                .get("/api/search/users")
                .queryParam("keyword", keyword)
                .auth(createToken(user.getId()))
                .build();
    }
}
