package botobo.core.acceptance.search;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static botobo.core.utils.Fixture.bear;
import static botobo.core.utils.Fixture.pk;
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
        pkToken = 소셜_로그인되어_있음(pk, SocialType.GITHUB);
        bearToken = 소셜_로그인되어_있음(bear, SocialType.GITHUB);
    }

    private void initializeWorkbooks() {
        // pk의 문제집 생성
        카드도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("가장 멋진 피케이의 자바 기초 문제집", true, makeJavaTags(), pkToken),
                2,
                pkToken
        );
        카드도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("나는 피케이의 자바 중급 문제집", true, makeJavaTags(), pkToken),
                5,
                pkToken
        );
        카드도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("다들 피케이의 자바 고급 문제집을 봐", true, makeJavaTags(), pkToken),
                10,
                pkToken
        );
        카드도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("피케이의 자바스크립트 문제집", true, makeJSTags(), pkToken),
                1,
                pkToken
        );

        // 중간곰의 문제집 생성
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바 기초 문제집", true, makeJavaTags(), bearToken),
                1,
                bearToken,
                List.of(pkToken)
        );
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바 중급 문제집", true, makeJavaTags(), bearToken),
                1,
                bearToken,
                List.of(pkToken, bearToken)
        );
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바스크립트 고급 문제집", true, makeJSTags(), bearToken),
                1,
                bearToken,
                List.of()
        );
        유저_태그포함_문제집_등록되어_있음("너도 중간곰과 함께 자바 할 수 있어", true, makeJavaTags(), bearToken);
    }

    private List<TagRequest> makeJavaTags() {
        return Arrays.asList(
                TagRequest.builder().id(0L).name("java").build(),
                TagRequest.builder().id(0L).name("자바").build()
        );
    }

    private List<TagRequest> makeJSTags() {
        return Arrays.asList(
                TagRequest.builder().id(0L).name("javascript").build(),
                TagRequest.builder().id(0L).name("js").build()
        );
    }

    private void 카드와_좋아요도_함께_등록(WorkbookResponse workbookResponse, int cardCount, String accessToken, List<String> heartUserTokens) {
        카드도_함께_등록(workbookResponse, cardCount, accessToken);
        좋아요도_함께_등록(workbookResponse, heartUserTokens);
    }

    private void 카드도_함께_등록(WorkbookResponse workbookResponse, int cardCount, String accessToken) {
        Long workbookId = workbookResponse.getId();
        IntStream.rangeClosed(1, cardCount)
                .forEach(number -> 유저_카드_등록되어_있음("질문", "정답", workbookId, accessToken));
    }

    private void 좋아요도_함께_등록(WorkbookResponse workbookResponse, List<String> heartUserTokens) {
        Long workbookId = workbookResponse.getId();
        heartUserTokens
                .forEach(token -> 하트_토글_요청(workbookId, token));
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
    void searchFromKeyword() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "기초");

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
    @DisplayName("문제집 검색 - 성공, 문제집 이름과 태그 이름에 전부 포함되어 있을 경우")
    void searchFromKeywordInWorkbookNameOrTagName() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(7);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바스크립트 고급 문제집",
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집",
                        "피케이의 자바스크립트 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 문제집 이름에 포함되어 있지는 않으나 태그 이름에는 포함되어 있을 경우")
    void searchFromKeywordInTagName() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(5);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }


    @Test
    @DisplayName("문제집 검색 - 성공, 시간 기준 최신 순 정렬")
    void searchFromDateDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바스크립트 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 사전순 정렬")
    void searchFromNameAsc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "name");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "가장 멋진 피케이의 자바 기초 문제집",
                        "나는 피케이의 자바 중급 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "피케이의 자바스크립트 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 좋아요 많은 순 정렬")
    void searchFromHeartDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");
        parameters.put("criteria", "heart");
        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집",
                        "중간곰의 자바스크립트 고급 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 카드 많은 순 정렬")
    void searchFromCountDesc() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "count");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집",
                        "피케이의 자바스크립트 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 요청한 크기보다 문제집을 조금 가진 경우 가지고 있는 문제집만 보여준다.")
    void searchWithBiggerSizeThanStored() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
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
                        "피케이의 자바스크립트 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 페이징 이용")
    void searchWithPaging() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
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
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 카드의 수가 0개인 문제집은 조회되지 않음")
    void searchExceptZeroCards() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getCardCount)
                .allMatch(cardCount -> cardCount > 0);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 최신순 및 원하는 태그로 필터링함.")
    void searchFromKeywordAndDateDescAndTagFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
        parameters.put("tags", "1");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 카드순 및 원하는 태그로 필터링함.")
    void searchFromKeywordAndCountDescAndTagFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "count");
        parameters.put("tags", "1");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 좋아요순 및 원하는 태그로 필터링함.")
    void searchFromKeywordAndHeartDescAndTagFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");
        parameters.put("criteria", "heart");
        parameters.put("tags", "2");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(2);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 최신순 및 원하는 유저로 필터링함.")
    void searchFromKeywordAndDateDescAndUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "date");
        parameters.put("users", "5");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "피케이의 자바스크립트 문제집",
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 카드순 및 원하는 유저로 필터링함.")
    void searchFromKeywordAndCountDescAndUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "count");
        parameters.put("users", "5");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(4);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집",
                        "피케이의 자바스크립트 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 좋아요순 및 원하는 유저로 필터링함.")
    void searchFromKeywordAndHeartDescAndUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "heart");
        parameters.put("users", "6");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집",
                        "중간곰의 자바스크립트 고급 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 최신순 및 원하는 태그, 유저로 필터링함.")
    void searchFromKeywordAndDateDescAndTagUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "date");
        parameters.put("tags", "1");
        parameters.put("users", "5");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 카드순 및 원하는 태그, 유저로 필터링함.")
    void searchFromKeywordAndCountDescAndTagUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "count");
        parameters.put("tags", "1");
        parameters.put("users", "5");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "다들 피케이의 자바 고급 문제집을 봐",
                        "나는 피케이의 자바 중급 문제집",
                        "가장 멋진 피케이의 자바 기초 문제집"
                );
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 키워드로 검색한 결과에 좋아요순 및 원하는 태그, 유저로 필터링함.")
    void searchFromKeywordAndHeartDescAndTagUserFiltering() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "heart");
        parameters.put("tags", "1");
        parameters.put("users", "6");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(2);
        assertThat(workbookResponses)
                .extracting(WorkbookResponse::getName)
                .containsExactly(
                        "중간곰의 자바 중급 문제집",
                        "중간곰의 자바 기초 문제집"
                );
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
    void recommendRelatedTags() {
        // given
        String keyword = "j";

        // when
        HttpResponse response = 추천_태그_검색_요청(keyword);

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).hasSize(3);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .allMatch(name -> name.contains(keyword));
    }

    @Test
    @DisplayName("태그 검색 - 성공")
    void recommendRelatedTagsWhenKeywordIsEmpty() {
        // given
        String keyword = "";

        // when
        HttpResponse response = 추천_태그_검색_요청(keyword);

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).hasSize(0);
    }

    private HttpResponse 문제집_검색_요청(Map<String, String> parameters) {
        return request()
                .get("/api/search/workbooks")
                .queryParams(parameters)
                .build();
    }

    private HttpResponse 추천_태그_검색_요청(String keyword) {
        return request()
                .get("/api/search/tags")
                .queryParam("keyword", keyword)
                .build();
    }
}
