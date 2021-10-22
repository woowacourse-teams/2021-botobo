package botobo.core.acceptance.search;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static botobo.core.acceptance.utils.Fixture.JAVA_TAG_REQUESTS;
import static botobo.core.acceptance.utils.Fixture.JS_TAG_REQUESTS;
import static botobo.core.acceptance.utils.Fixture.USER_BEAR;
import static botobo.core.acceptance.utils.Fixture.USER_PK;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("검색 인수 테스트")
class SearchAcceptanceTest extends DomainAcceptanceTest {

    private static final List<TagRequest> failTags = List.of(
            TagRequest.builder().id(0L).name("실패").build()
    );

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        initializeWorkbooks();
    }

    private void initializeWorkbooks() {
        // pk의 문제집 생성
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("가장 멋진 피케이의 자바 기초 문제집", true, JAVA_TAG_REQUESTS, USER_PK),
                2,
                "질문",
                "답변",
                USER_PK,
                emptyList()
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("나는 피케이의 자바 중급 문제집", true, JAVA_TAG_REQUESTS, USER_PK),
                5,
                "질문",
                "답변",
                USER_PK,
                emptyList()
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("다들 피케이의 자바 고급 문제집을 봐", true, JAVA_TAG_REQUESTS, USER_PK),
                10,
                "질문",
                "답변",
                USER_PK,
                emptyList()
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("피케이의 자바스크립트 문제집", true, JS_TAG_REQUESTS, USER_PK),
                1,
                "질문",
                "답변",
                USER_PK,
                emptyList()
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("피케이의 자바스크립트 문제집", false, failTags, USER_PK),
                1,
                "질문",
                "답변",
                USER_PK,
                emptyList()
        );

        // 중간곰의 문제집 생성
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바 기초 문제집", true, JAVA_TAG_REQUESTS, USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_PK)
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바 중급 문제집", true, JAVA_TAG_REQUESTS, USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_PK, USER_BEAR)
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바스크립트 고급 문제집", true, JS_TAG_REQUESTS, USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of()
        );
        CREATE_WORKBOOK_WITH_PARAMS("너도 중간곰과 함께 자바 할 수 있어", true, failTags, USER_BEAR);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 검색어를 제외한 다른 인자는 생략 가능")
    void searchWithDefault() {
        // given
        Map<String, Object> parameters = Map.of("keyword", "java");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 문제집 이름으로 검색")
    void searchFromKeyword() {
        // given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "기초");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "java");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "name");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");
        parameters.put("criteria", "heart");
        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "count");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
        parameters.put("size", "50");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
        parameters.put("start", "1");
        parameters.put("size", "2");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "date");
        parameters.put("tags", "1");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "피케이");
        parameters.put("criteria", "count");
        parameters.put("tags", "1");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "중간곰");
        parameters.put("criteria", "heart");
        parameters.put("tags", "2");

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "date");
        parameters.put("users", USER_PK.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "count");
        parameters.put("users", USER_PK.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "heart");
        parameters.put("users", USER_BEAR.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "date");
        parameters.put("tags", "1");
        parameters.put("users", USER_PK.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "count");
        parameters.put("tags", "1");
        parameters.put("users", USER_PK.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "자바");
        parameters.put("criteria", "heart");
        parameters.put("tags", "1");
        parameters.put("users", USER_BEAR.getId());

        // when
        HttpResponse response = SEARCH_WORKBOOK(parameters);

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
    @DisplayName("연관 태그 검색 - 성공")
    void recommendRelatedTags() {
        // given
        String keyword = "j";

        // when
        HttpResponse response = request()
                .get("/search/tags")
                .queryParam("keyword", keyword)
                .build();

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).hasSize(3);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .allMatch(name -> name.contains(keyword));
    }

    @Test
    @DisplayName("연관 태그 검색 - 성공")
    void recommendRelatedTagsWhenKeywordIsEmpty() {
        // given
        String keyword = "";

        // when
        HttpResponse response = request()
                .get("/search/tags")
                .queryParam("keyword", keyword)
                .build();

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).isEmpty();
    }

    @Test
    @DisplayName("연관 태그 검색 - 성공, 문제집이 비공개이거나, 카드가 없을 땐 조회하지 않는다.")
    void recommendRelatedTagsWhenWorkbookIsNotValid() {
        // given
        String keyword = "실패";

        // when
        HttpResponse response = request()
                .get("/search/tags")
                .queryParam("keyword", keyword)
                .build();

        // then
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponses).isEmpty();
    }
}
