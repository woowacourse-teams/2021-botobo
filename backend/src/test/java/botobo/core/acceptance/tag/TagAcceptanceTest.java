package botobo.core.acceptance.tag;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.tag.TagResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static botobo.core.acceptance.utils.Fixture.JS_TAG_REQUESTS;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tag 인수 테스트")
class TagAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        CREATE_WORKBOOK_INCLUDE_CARD("Js 문제집", true, JS_TAG_REQUESTS, USER_JOANNE, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Js 비공개 문제집", false, JS_TAG_REQUESTS, USER_JOANNE, "질문", "답변");
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공")
    @Test
    void findAllTagsByWorkbookName() {
        // given
        final String workbookName = "Js";
        final HttpResponse response = request()
                .get("/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(2);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .containsExactly("javascript", "js");
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공, 태그에만 포함된 문제집도 가져온다.")
    @Test
    void findAllTagsByWorkbookNameEqTag() {
        // given
        final String workbookName = "javascript";
        final HttpResponse response = request()
                .get("/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(2);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .containsExactly("javascript", "js");
    }

}
