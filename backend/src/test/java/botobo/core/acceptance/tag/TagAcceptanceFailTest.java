package botobo.core.acceptance.tag;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_TAG_REQUEST;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_WORKBOOK_REQUEST;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tag 실패 케이스 인수 테스트")
public class TagAcceptanceFailTest extends DomainAcceptanceTest {
    @DisplayName("문제집명이 포함된 문제집의 태그들을 가져온다. - 성공, 카드의 개수가 0개이면 가져오지 않는다.")
    @Test
    void findAllTagsByWorkbookNameWhenCardCountsZero() {
        // given
        List<WorkbookRequest> workbookRequests = List.of(
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "Java",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "자바스크립트"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "리액트")
                        )
                ),
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "JAVAVA",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "리액트"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "네트워크")
                        )
                ),
                MAKE_SINGLE_WORKBOOK_REQUEST(
                        "Javascript",
                        true,
                        List.of(
                                MAKE_SINGLE_TAG_REQUEST(1L, "자바스크립트"),
                                MAKE_SINGLE_TAG_REQUEST(2L, "스프링"),
                                MAKE_SINGLE_TAG_REQUEST(3L, "네트워크")
                        )
                )
        );

        CREATE_WORKBOOKS_OF_USERS(workbookRequests, USERS);

        final String workbookName = "Java";
        final RequestBuilder.HttpResponse response = request()
                .get("/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(0);
    }


    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공, 문제집 명이 비어있는 경우 빈 응답")
    @Test
    void findAllTagsByWorkbookNameWhenEmpty() {
        // given
        final RequestBuilder.HttpResponse response = request()
                .get("/tags?workbook=")
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
        final RequestBuilder.HttpResponse response = request()
                .get("/tags?workbook=" + stringGenerator(31))
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("문제집명이 포함된 문제집의 태그들을 가져온다. - 성공, 비공개 문제집이면 가져오지 않는다.")
    @Test
    void findAllTagsByWorkbookNameWhenPrivateWorkbook() {
        // given
        final String workbookName = "비공개";
        final RequestBuilder.HttpResponse response = request()
                .get("/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(0);
    }
}
