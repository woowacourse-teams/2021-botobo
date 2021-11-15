package botobo.core.acceptance.user;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.user.UserFilterResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_TAG_REQUEST;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_WORKBOOK_REQUEST;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_OZ;
import static botobo.core.utils.TestUtils.stringGenerator;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User의 Author 경우 인수 테스트")
public class AuthorAcceptanceTest extends DomainAcceptanceTest {
    List<WorkbookRequest> workbookRequests;

    @BeforeEach
    void setFixtures() {
        workbookRequests = List.of(
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
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 카드의 개수가 0개이면 가져오지 않는다.")
    @Test
    void findAllUsersByWorkbookNameWhenCardIsEmpty() {
        CREATE_WORKBOOKS_OF_USERS(workbookRequests, USERS);

        // given
        final String workbookName = "Java";
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).isEmpty();
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공")
    @Test
    void findAllUsersByWorkbookName() {
        CREATE_WORKBOOK_INCLUDE_CARD("Java 문제집", true, emptyList(), USER_OZ, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Spring 문제집", true, emptyList(), USER_JOANNE, "질문", "답변");

        // given
        final String workbookName = "Java";
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("oz");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 태그에 포함됨")
    @Test
    void findAllUsersByWorkbookNameEqualsTag() {
        List<TagRequest> jsTags = Arrays.asList(
                TagRequest.builder().id(0L).name("javascript").build(),
                TagRequest.builder().id(0L).name("js").build()
        );

        CREATE_WORKBOOK_INCLUDE_CARD("Js 문제집", true, jsTags, USER_JOANNE, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Java 문제집", true, emptyList(), USER_OZ, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Spring 문제집", true, emptyList(), USER_JOANNE, "질문", "답변");

        // given
        final String workbookName = "Js";
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("joanne");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 비공개 문제집의 경우 작성자를 가져오지 않는다.")
    @Test
    void findAllUsersByWorkbookNameWhenOpened() {
        CREATE_WORKBOOK_INCLUDE_CARD("Java 문제집", true, emptyList(), USER_OZ, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Spring 문제집", false, emptyList(), USER_JOANNE, "질문", "답변");

        // given
        final String workbookName = "문제집";
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses)
                .extracting(UserFilterResponse::getName)
                .containsExactlyInAnyOrder("oz");
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 성공, 문제집명이 비어있는 경우 빈 응답")
    @Test
    void findAllUsersByWorkbookNameIsEmpty() {
        CREATE_WORKBOOKS_OF_USERS(workbookRequests, USERS);

        // given
        final String workbookName = "";
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + workbookName)
                .build();

        // when
        final List<UserFilterResponse> userResponses = response.convertBodyToList(UserFilterResponse.class);

        // then
        assertThat(userResponses).isEmpty();
    }

    @DisplayName("문제집명이 포함된 문제집의 작성자들을 가져온다. - 실패, 문제집명이 30글자를 넘는 비정상적 경우")
    @Test
    void findAllUsersByWorkbookNameIsLong() {
        // given
        final RequestBuilder.HttpResponse response = request()
                .get("/users?workbook=" + stringGenerator(31))
                .build();

        // when - then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
