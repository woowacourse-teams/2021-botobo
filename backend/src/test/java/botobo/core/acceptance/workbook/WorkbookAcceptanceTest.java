package botobo.core.acceptance.workbook;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.User;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.heart.HeartResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_TAG_REQUEST;
import static botobo.core.acceptance.utils.Fixture.USER_BEAR;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_OZ;
import static botobo.core.acceptance.utils.Fixture.USER_PK;
import static botobo.core.utils.TestUtils.stringGenerator;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Workbook 인수 테스트")
class WorkbookAcceptanceTest extends DomainAcceptanceTest {

    @Test
    @DisplayName("유저가 문제집 추가 - 성공")
    void createWorkbookByUser() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        WorkbookResponse workbookResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(workbookResponse).extracting("id").isNotNull();
        assertThat(workbookResponse).extracting("name").isEqualTo(workbookRequest.getName());
        assertThat(workbookResponse).extracting("cardCount").isEqualTo(0);
        assertThat(workbookResponse).extracting("opened").isEqualTo(workbookRequest.isOpened());
        assertThat(workbookResponse.getTags()).hasSize(1);
        assertThat(workbookResponse.getTags().get(0).getId()).isNotZero();
        assertThat(workbookResponse.getTags().get(0).getName()).isEqualTo("자바");
        assertThat(workbookResponse.getHeartCount()).isZero();
    }

    @Test
    @DisplayName("문제집 추가 요청 - 성공, opened와 tags는 필수가 아니다 - 기본값 (opened = false, tags = empty list)")
    void createWorkbookByUserWithTags() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .tags(new ArrayList<>())
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        WorkbookResponse workbookResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(workbookResponse.getOpened()).isFalse();
        assertThat(workbookResponse.getTags()).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("유저가 문제집 추가 - 실패, name이 없을 때")
    void createWorkbookByUserWhenNameNotExist(String name) {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(true)
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("문제집 이름은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, name이 30자 초과")
    void createWorkbookByUserWhenNameLengthOver30() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(stringGenerator(31))
                .opened(true)
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("문제집 이름은 30자 이하여야 합니다.");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 아이디 없음")
    void createWorkbookByUserWhenTagIdNull() {
        // given
        TagRequest tagRequest = TagRequest.builder().name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 아이디 음수")
    void createWorkbookByUserWhenTagIdNegative() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(-1L).name("자바").build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 0이상의 숫자입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, Tag 이름 없음")
    void createWorkbookByUserWhenTagNameNull() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("이름은 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 추가 - 실패, 20자를 초과하는 Tag 이름")
    void createWorkbookByUserWhenTagNameLong() {
        // given
        TagRequest tagRequest = TagRequest.builder().id(0L).name(stringGenerator(21)).build();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("자바 문제집")
                .opened(true)
                .tags(Collections.singletonList(tagRequest))
                .build();

        // when
        final HttpResponse response = CREATE_WORKBOOK(workbookRequest, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그는 20자 이하여야 합니다.");
    }

    @Test
    @DisplayName("다양한 공개 문제집을 조회한다. - 성공, 최대 개수가 2개이면 2개를 보여준다.")
    void findPublicWorkbooksWhenMaxIsTwo() {
        // given
        CREATE_WORKBOOK_INCLUDE_CARD("Java 문제집", true, emptyList(), USER_OZ, "질문", "답변");
        CREATE_WORKBOOK_INCLUDE_CARD("Spring 문제집", true, emptyList(), USER_JOANNE, "질문", "답변");

        // when
        HttpResponse response = request()
                .get("/workbooks/public")
                .build();

        // then
        List<WorkbookResponse> publicWorkbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicWorkbookResponses).hasSize(2);
    }

    @Test
    @DisplayName("다양한 공개 문제집을 조회한다. - 성공")
    void findPublicWorkbooks() {
        // given
        for (int i = 0; i < 100; i++) {
            CREATE_WORKBOOK_INCLUDE_CARD("Java 문제집" + i, true, emptyList(), USER_OZ, "질문", "답변");
            CREATE_WORKBOOK_INCLUDE_CARD("Spring 문제집" + i, true, emptyList(), USER_JOANNE, "질문", "답변");
        }

        // when
        HttpResponse response = request()
                .get("/workbooks/public")
                .build();

        // then
        List<WorkbookResponse> publicWorkbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicWorkbookResponses).hasSize(100);
    }

    @Test
    @DisplayName("다양한 공개 문제집을 조회한다. - 성공, opened가 true인 문제집만 조회")
    void findPublicWorkbooksWhenOpenedIsFalse() {
        // given
        CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", false, emptyList(), USER_OZ);
        CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", false, emptyList(), USER_JOANNE);

        // when
        HttpResponse response = request()
                .get("/workbooks/public")
                .build();

        // then
        List<WorkbookResponse> publicWorkbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicWorkbookResponses).isEmpty();
    }

    @Test
    @DisplayName("다양한 공개 문제집을 조회한다. - 성공, 카드를 1개 이상 포함한 문제집만 조회")
    void findPublicWorkbooksWhenNonZeroCard() {
        // given
        // 카드를 포함하지 않은 문제집을 등록한다.
        CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", false, emptyList(), USER_OZ);
        CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", false, emptyList(), USER_JOANNE);

        // when
        HttpResponse response = request()
                .get("/workbooks/public")
                .build();

        // then
        List<WorkbookResponse> publicWorkbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicWorkbookResponses).isEmpty();
    }

    @Test
    @DisplayName("유저의 문제집 전체 조회 - 성공")
    void findWorkbooksByUser() {
        // given
        CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK);
        CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", true, emptyList(), USER_PK);
        CREATE_WORKBOOK_WITH_PARAMS("Database 문제집", true, emptyList(), USER_PK);

        // when
        final HttpResponse response = request()
                .get("/workbooks")
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses).hasSize(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 존재) - 성공")
    void findCategoryCardsById() {
        // given
        final WorkbookResponse workbookResponse = CREATE_WORKBOOK_WITH_PARAMS(
                "Java 문제집",
                true,
                List.of(MAKE_SINGLE_TAG_REQUEST(1L, "Java")),
                USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);

        // when
        final HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();

        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(workbookResponse.getName());
        assertThat(workbookCardResponse.getWorkbookOpened()).isTrue();
        assertThat(workbookCardResponse.getCards()).hasSize(3);
        assertThat(workbookCardResponse.getTags()).hasSize(1);
        assertThat(workbookCardResponse.getHeartCount()).isZero();
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 존재) - 성공, 비공개인 경우")
    void findCategoryCardsByIdWhenPrivate() {
        // given
        final WorkbookResponse workbookResponse = CREATE_WORKBOOK_WITH_PARAMS(
                "Java 문제집",
                false,
                List.of(MAKE_SINGLE_TAG_REQUEST(1L, "Java")),
                USER_BEAR);

        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);

        // when
        final HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();

        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(workbookResponse.getName());
        assertThat(workbookCardResponse.getWorkbookOpened()).isFalse();
        assertThat(workbookCardResponse.getCards()).hasSize(3);
        assertThat(workbookCardResponse.getTags()).hasSize(1);
        assertThat(workbookCardResponse.getHeartCount()).isZero();
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 0개) - 성공")
    void findWorkbookCardsByIdWithNotExistsCard() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);

        // when
        final HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();

        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(workbookResponse.getName());
        assertThat(workbookCardResponse.getWorkbookOpened()).isTrue();
        assertThat(workbookCardResponse.getCards()).isEmpty();
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 - 실패, 자신의 문제집이 아닌 경우")
    void findWorkbookCardsByIdWithOtherUser() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);

        // when
        final HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_JOANNE.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("공유 문제집 조회 - 성공")
    void findPublicWorkbookById() {
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);
        CREATE_CARD_WITH_PARAMS("question", "answer", workbookResponse.getId(), USER_BEAR);

        // when
        final HttpResponse response = request()
                .get("/workbooks/public/{id}", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();
        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(workbookResponse.getName());
        assertThat(workbookCardResponse.getCards()).hasSize(3);
        assertThat(workbookCardResponse.getWorkbookOpened()).isTrue();
        assertThat(workbookCardResponse.getHeart()).isNotNull();
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 성공")
    void updateWorkbook() {
        // given
        List<TagRequest> tagRequests = Collections.singletonList(
                TagRequest.builder().id(0L).name("잡아").build()
        );
        final WorkbookResponse workbookResponse = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, tagRequests, USER_BEAR);
        List<TagRequest> updatedTagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).name("자바").build()
        );
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(updatedTagRequests)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        WorkbookResponse updateResponse = response.convertBody(WorkbookResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse).extracting("id").isNotNull();
        assertThat(updateResponse).extracting("name").isEqualTo(workbookUpdateRequest.getName());
        assertThat(updateResponse).extracting("cardCount").isEqualTo(0);
        assertThat(updateResponse).extracting("opened").isEqualTo(workbookUpdateRequest.getOpened());
        assertThat(updateResponse.getTags()).hasSize(1);
        assertThat(updateResponse.getTags().get(0).getName()).isEqualTo("자바");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("유저가 문제집 수정 - 실패, name이 없을 때")
    void updateWorkbookWhenNameNotExist(String name) {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name(name)
                .opened(true)
                .cardCount(0)
                .heartCount(0)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("문제집 이름은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, name이 30자 초과")
    void updateWorkbookWhenNameLengthOver30() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name(stringGenerator(31))
                .opened(true)
                .cardCount(0)
                .heartCount(0)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("문제집 이름은 30자 이하여야 합니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, opened가 없을 때")
    void updateWorkbookWhenOpenedNotExist() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name(stringGenerator(30))
                .cardCount(0)
                .heartCount(0)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("문제집의 공개 여부는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, cardCount가 음수")
    void updateWorkbookWhenCardCountNegative() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(true)
                .cardCount(-5)
                .heartCount(0)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("카드 개수는 0이상 입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, heartCount가 음수")
    void updateWorkbookWhenHeartCountNegative() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(true)
                .cardCount(0)
                .heartCount(-5)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("좋아요 개수는 0이상 입니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, Tag 없음")
    void updateWorkbookByUserWhenTagNull() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("문제집을 수정하려면 태그가 필요합니다");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, Tag 아이디 없음")
    void updateWorkbookByUserWhenTagIdNull() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR);
        List<TagRequest> updatedTagRequests = Collections.singletonList(
                TagRequest.builder().name("자바").build()
        );
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(updatedTagRequests)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_BEAR);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, Tag 아이디 음수")
    void updateWorkbookByUserWhenTagIdNegative() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);
        List<TagRequest> updatedTagRequests = Collections.singletonList(
                TagRequest.builder().id(-1L).name("자바").build()
        );
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(updatedTagRequests)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_OZ);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그 아이디는 0이상의 숫자입니다");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, Tag 이름 없음")
    void updateWorkbookByUserWhenTagNameNull() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);
        List<TagRequest> updatedTagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).build()
        );
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(updatedTagRequests)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_OZ);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("이름은 필수 입력값입니다");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, 20자를 초과하는 Tag 이름")
    void updateWorkbookByUserWhenTagNameLong() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);
        List<TagRequest> updatedTagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).name(stringGenerator(21)).build()
        );
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(updatedTagRequests)
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_OZ);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("태그는 20자 이하여야 합니다.");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, 다른 유저가 수정을 시도할 때")
    void updateWorkbookWithOtherUser() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 비공개버전")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
                .tags(emptyList())
                .build();

        // when
        final HttpResponse response = UPDATE_WORKBOOK(workbookUpdateRequest, workbookResponse, USER_PK);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);

        // when
        final HttpResponse response = request()
                .delete("/workbooks/{id}", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_OZ.getId()))
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 실패, 다른 유저가 삭제를 시도할 때")
    void deleteWorkbookWithOtherUser() {
        // given
        final WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_OZ);


        // when
        final HttpResponse response = request()
                .delete("/workbooks/{id}", workbookResponse.getId())
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 성공")
    void scrapSelectedCardsToWorkbook() {
        // given
        final Long workbookId = CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", true, emptyList(), USER_PK).getId();
        final Long otherWorkbookId = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_BEAR).getId();
        CardResponse response1 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);
        CardResponse response2 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(response1.getId(), response2.getId()))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.header("Location")).isEqualTo(String.format("/workbooks/%d/cards", workbookId));
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집이 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenWorkbookNotExist() {
        // given
        final Long workbookId = 100L;

        final Long otherWorkbookId = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK).getId();
        CardResponse response1 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_PK);
        CardResponse response2 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_PK);

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(response1.getId(), response2.getId()))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_OZ.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 요청이 비어있음.")
    void scrapSelectedCardsToWorkbookFailedWhenRequestIsEmpty() {
        // given
        final Long workbookId = 100L;

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(List.of())
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 유저가 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenUserNotFound() {
        // given
        final Long workbookId = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK).getId();

        final Long otherWorkbookId
                = CREATE_WORKBOOK_WITH_PARAMS("유저가 존재하지 않는 문제집", true, emptyList(), USER_BEAR).getId();
        CardResponse response1 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);
        CardResponse response2 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(response1.getId(), response2.getId()))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(-100L))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집의 작성자가 아닌 유저")
    void scrapSelectedCardsToWorkbookFailedWhenNotAuthor() {
        // given
        final Long workbookId = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK).getId();

        final Long otherWorkbookId = CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", true, emptyList(), USER_BEAR).getId();
        CardResponse response1 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);
        CardResponse response2 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(response1.getId(), response2.getId()))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 카드 아이디가 비어있음.")
    void scrapSelectedCardsToWorkbookFailedWhenEmptyCardIds() {
        // given
        final long workbookId = 1L;
        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(List.of())
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 카드 아이디가 null")
    void scrapSelectedCardsToWorkbookFailedWhenNullCardIds() {
        // given
        final long workbookId = 1L;
        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(null)
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_BEAR.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, Card Id는 요청으로 들어왔으나 해당 ID의 카드가 모두 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenCardNotFound() {
        // given
        final Long workbookId
                = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK).getId();

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(100L, 101L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 카드를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, Card Id는 요청으로 들어왔으나 해당 ID의 카드가 일부 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenPartOfCardNotFound() {
        // given
        final Long workbookId = CREATE_WORKBOOK_WITH_PARAMS("Java 문제집", true, emptyList(), USER_PK).getId();

        final Long otherWorkbookId = CREATE_WORKBOOK_WITH_PARAMS("Spring 문제집", true, emptyList(), USER_BEAR).getId();
        CardResponse response1 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);
        CardResponse response2 = CREATE_CARD_WITH_PARAMS("question", "answer", otherWorkbookId, USER_BEAR);

        final ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(response1.getId(), response2.getId(), 100L))
                .build();

        // when
        final HttpResponse response = request()
                .post("/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 카드를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("유저가 하트를 토글 - 성공")
    void toggleOnHeart() {
        // given
        WorkbookResponse workbookResponse
                = CREATE_WORKBOOK_WITH_PARAMS("자바 문제집", true, emptyList(), USER_PK);

        Long workbookId = workbookResponse.getId();

        // when, then
        HttpResponse httpResponse = TOGGLE_HEARTS(workbookId, USER_OZ);
        HeartResponse heartResponse = httpResponse.convertBody(HeartResponse.class);
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(heartResponse.isHeart()).isTrue();

        httpResponse = TOGGLE_HEARTS(workbookId, USER_OZ);
        heartResponse = httpResponse.convertBody(HeartResponse.class);
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(heartResponse.isHeart()).isFalse();
    }

    private HttpResponse UPDATE_WORKBOOK(WorkbookUpdateRequest workbookUpdateRequest,
                                         WorkbookResponse workbookResponse,
                                         User user) {
        return request()
                .put("/workbooks/{id}", workbookUpdateRequest, workbookResponse.getId())
                .auth(CREATE_TOKEN(user.getId()))
                .build();
    }
}
