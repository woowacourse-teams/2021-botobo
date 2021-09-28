package botobo.core.acceptance.tag;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static botobo.core.utils.Fixture.ADMIN_WORKBOOK_REQUESTS_WITH_TAG;
import static botobo.core.utils.Fixture.joanne;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class TagAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        final String joanneToken = 소셜_로그인되어_있음(joanne, SocialType.GITHUB);
        List<TagRequest> jsTags = Arrays.asList(
                TagRequest.builder().id(0L).name("javascript").build(),
                TagRequest.builder().id(0L).name("js").build()
        );
        유저_태그_카드_포함_문제집_등록되어_있음("Js 문제집", true, jsTags, joanneToken);
    }

    @DisplayName("문제집명에 해당하는 태그를 모두 가져온다. - 성공")
    @Test
    void findAllTagsByWorkbookName() {
        // given
        final String workbookName = "Js";
        final HttpResponse response = request()
                .get("/api/tags?workbook=" + workbookName)
                .build();

        // when
        List<TagResponse> tagResponses = response.convertBodyToList(TagResponse.class);

        // then
        assertThat(tagResponses).hasSize(2);
        assertThat(tagResponses)
                .extracting(TagResponse::getName)
                .containsExactly("javascript", "js");
    }

    @DisplayName("문제집명이 포함된 문제집의 태그들을 가져온다. - 성공, 카드의 개수가 0개이면 가져오지 않는다.")
    @Test
    void findAllTagsByWorkbookNameWhenCardCountsZero() {
        // given
        서로_다른_관리자의_여러개_문제집_생성_요청(ADMIN_WORKBOOK_REQUESTS_WITH_TAG, ADMINS);

        final String workbookName = "Java";
        final HttpResponse response = request()
                .get("/api/tags?workbook=" + workbookName)
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
