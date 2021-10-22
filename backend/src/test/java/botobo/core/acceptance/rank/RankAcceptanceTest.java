package botobo.core.acceptance.rank;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.application.rank.SearchRankService;
import botobo.core.dto.rank.SearchRankResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static botobo.core.acceptance.utils.Fixture.USER_BEAR;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_OZ;
import static botobo.core.acceptance.utils.Fixture.USER_PK;
import static org.assertj.core.api.Assertions.assertThat;

class RankAcceptanceTest extends DomainAcceptanceTest {

    @Autowired
    private SearchRankService searchRankService;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        initializeWorkbooks();
    }

    private void initializeWorkbooks() {
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바 기초 문제집", true, makeJavaTags(), USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_PK, USER_JOANNE)
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바 중급 문제집", true, makeJavaTags(), USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_PK, USER_BEAR, USER_JOANNE)
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 자바스크립트 고급 문제집", true, makeJSTags(), USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_OZ, USER_JOANNE)
        );
        CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(
                CREATE_WORKBOOK_WITH_PARAMS("중간곰의 순위권 밖 문제집", true, makeJSTags(), USER_BEAR),
                1,
                "질문",
                "답변",
                USER_BEAR,
                List.of(USER_OZ)
        );
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

    @Test
    @DisplayName("문제집의 순위를 가져온다 - 성공")
    void findWorkbookRanks() {
        // when
        RequestBuilder.HttpResponse response = request()
                .get("/ranks/workbooks")
                .build();
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
    @DisplayName("검색 키워드의 순위를 가져온다 - 성공")
    void findSearchKeywordRanks() {
        // given
        키워드로_여러번_검색_요청("java", 3);
        키워드로_여러번_검색_요청("react", 2);
        키워드로_여러번_검색_요청("spring", 1);
        searchRankService.updateSearchRanks(
                searchRankService.findSearchRanks(),
                searchRankService.findKeywordRanks()
        );

        // when
        final RequestBuilder.HttpResponse response = request()
                .get("/ranks/search")
                .build();

        // then
        List<SearchRankResponse> searchRankResponses = response.convertBodyToList(SearchRankResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("java", "react", "spring");
    }

    private void 키워드로_여러번_검색_요청(String keyword, int searchCount) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < searchCount; i++) {
            parameters.put("keyword", keyword);
            문제집_검색_요청(parameters);
        }
    }

    private RequestBuilder.HttpResponse 문제집_검색_요청(Map<String, Object> parameters) {
        return request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();
    }
}
