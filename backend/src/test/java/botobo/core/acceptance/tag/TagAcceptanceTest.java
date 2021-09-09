package botobo.core.acceptance.tag;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.tag.TagResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static botobo.core.utils.Fixture.ADMIN_WORKBOOK_REQUESTS_WITH_TAG;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class TagAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(ADMIN_WORKBOOK_REQUESTS_WITH_TAG);
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공")
    @Test
    void findAllTagsByWorkbookName() {
        // given
        final String workbookName = "Java";
        final HttpResponse response = request()
                .get("/api/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(5);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .containsExactly("자바", "자바스크립트", "리액트", "네트워크", "스프링");
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공, 문제집 명이 비어있는 경우 빈 응답")
    @Test
    void findAllTagsByWorkbookNameWhenEmpty() {
        // given
        final HttpResponse response = request()
                .get("/api/tags?workbook=")
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).isEmpty();
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 실패, 문제집 명이 30글자를 넘는 경우 예외")
    @Test
    void findAllTagsByWorkbookNameWhenInvalidLength() {
        // given
        final HttpResponse response = request()
                .get("/api/tags?workbook=" + stringGenerator(31))
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
