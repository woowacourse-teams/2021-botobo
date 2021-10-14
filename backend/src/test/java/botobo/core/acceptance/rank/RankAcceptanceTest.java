package botobo.core.acceptance.rank;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static botobo.core.utils.Fixture.bear;
import static botobo.core.utils.Fixture.joanne;
import static botobo.core.utils.Fixture.oz;
import static botobo.core.utils.Fixture.pk;
import static org.assertj.core.api.Assertions.assertThat;

class RankAcceptanceTest extends DomainAcceptanceTest {

    @Autowired
    CacheManager cacheManager;

    private String pkToken, bearToken, joanneToken, ozToken;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        initializeUsers();
        initializeWorkbooks();
    }

    @Override
    @AfterEach
    protected void tearDown() {
        super.tearDown();
        Cache workbookRanks = cacheManager.getCache("workbookRanks");
        Objects.requireNonNull(workbookRanks).clear();
    }

    private void initializeUsers() {
        pkToken = 소셜_로그인되어_있음(pk, SocialType.GITHUB);
        bearToken = 소셜_로그인되어_있음(bear, SocialType.GITHUB);
        joanneToken = 소셜_로그인되어_있음(joanne, SocialType.GITHUB);
        ozToken = 소셜_로그인되어_있음(oz, SocialType.GITHUB);
    }

    private void initializeWorkbooks() {
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바 기초 문제집", true, makeJavaTags(), bearToken),
                1,
                bearToken,
                List.of(pkToken, joanneToken)
        );
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바 중급 문제집", true, makeJavaTags(), bearToken),
                1,
                bearToken,
                List.of(pkToken, bearToken, joanneToken)
        );
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 자바스크립트 고급 문제집", true, makeJSTags(), bearToken),
                1,
                bearToken,
                List.of(ozToken, joanneToken)
        );
        카드와_좋아요도_함께_등록(
                유저_태그포함_문제집_등록되어_있음("중간곰의 순위권 밖 문제집", true, makeJSTags(), bearToken),
                1,
                bearToken,
                List.of(ozToken)
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
    @DisplayName("인기 문제집 검색 - 성공")
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
}
