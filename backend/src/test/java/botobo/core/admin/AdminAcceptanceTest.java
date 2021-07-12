package botobo.core.admin;

import botobo.core.AcceptanceTest;
import botobo.core.admin.dto.*;
import botobo.core.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static botobo.core.quiz.domain.CategoryRepositoryTest.longStringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin 인수 테스트")
public class AdminAcceptanceTest extends AcceptanceTest {

    private static final AdminCategoryRequest ADMIN_CATEGORY_REQUEST =
            new AdminCategoryRequest("Category", "botobo.io", "this is category.");

    public static ExtractableResponse<Response> 카테고리_생성_요청(AdminCategoryRequest adminCategoryRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminCategoryRequest)
                .when().post("/admin/categories")
                .then().log().all()
                .extract();
    }

    public static void 여러개_카테고리_생성_요청(List<AdminCategoryRequest> requests) {
        for (AdminCategoryRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/admin/categories")
                    .then().log().all()
                    .extract();
        }
    }

    public static ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminCardRequest)
                .when().post("/admin/cards")
                .then().log().all()
                .extract();
    }

    public static void 여러개_카드_생성_요청(List<AdminCardRequest> requests) {
        for (AdminCardRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/admin/cards")
                    .then().log().all()
                    .extract();
        }
    }

    public static ExtractableResponse<Response> 답변_생성_요청(AdminAnswerRequest adminAnswerRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminAnswerRequest)
                .when().post("/admin/answers")
                .then().log().all()
                .extract();
    }

    public static void 여러개_답변_생성_요청(List<AdminAnswerRequest> requests) {
        for (AdminAnswerRequest request : requests)
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/admin/answers")
                    .then().log().all()
                    .extract();
    }

    @Test
    @DisplayName("Category 생성 - 성공")
    void createCategory() {
        // given
        final ExtractableResponse<Response> response = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);

        // when
        final AdminCategoryResponse adminCategoryResponse = response.as(AdminCategoryResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminCategoryResponse.getId()).isNotNull();
        assertThat(adminCategoryResponse.getName()).isEqualTo("Category");
        assertThat(adminCategoryResponse.getLogoUrl()).isEqualTo("botobo.io");
        assertThat(adminCategoryResponse.getDescription()).isEqualTo("this is category.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, name이 null일 때")
    void createCategoryWithNullName() {
        //given
        AdminCategoryRequest adminCategoryRequestWithNullName = new AdminCategoryRequest(null, "botobo.io", "description");
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithNullName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, logoUrl null일 때")
    void createCategoryWithNullLogoUrl() {
        //given
        AdminCategoryRequest adminCategoryRequestWithNullLogoUrl = new AdminCategoryRequest("name", null, "description");
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithNullLogoUrl);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("로고 url은 필수 입력값입니다.");
    }


    @Test
    @DisplayName("Category 생성 - 실패, Description null일 때")
    void createCategoryWithNullDescription() {
        //given
        AdminCategoryRequest adminCategoryRequestWithNullDescription = new AdminCategoryRequest("name", "logoUrl", null);
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithNullDescription);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("설명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, name은 최소 1글자")
    void createCategoryWithInvalidLengthWithZero() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidName = new AdminCategoryRequest("", "logoUrl", "description");
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 최소 1글자, 최대 30글자만 가능합니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, name은 최대 30글자")
    void createCategoryWithInvalidLengthWith31() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidName = new AdminCategoryRequest(
                longStringGenerator(31),
                "logoUrl",
                "description"
        );
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 최소 1글자, 최대 30글자만 가능합니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, logoUrl은 최대 100글자")
    void createCategoryWithInvalidLogoLength() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidLogoUrl = new AdminCategoryRequest(
                "name",
                longStringGenerator(101),
                "description"
        );
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidLogoUrl);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("logo Url은 최대 100글자까지 가능합니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, description은 최대 255글자")
    void createCategoryWithInvalidDescription() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidLengthDescription = new AdminCategoryRequest(
                "name",
                "logoUrl",
                longStringGenerator(256)
        );
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidLengthDescription);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("설명은 최대 255글자까지 가능합니다.");
    }


    @Test
    @DisplayName("Card 생성 - 성공")
    void createCard() {
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", categoryId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final AdminCardResponse adminCardResponse = response.as(AdminCardResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminCardResponse.getId()).isNotNull();
        assertThat(adminCardResponse.getQuestion()).isEqualTo("Question");
        assertThat(adminCardResponse.getCategoryId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("Card 생성 - 실패, question이 null일 때")
    void createCardWithNullQuestion() {
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest(null, categoryId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Card 생성 - 실패, categoryId가 null일 때")
    void createCardWithNullCategoryId() {
        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", null);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리의 아이디는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Answer 생성 - 성공")
    void createAnswer() {
        //given
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", categoryId);
        ExtractableResponse<Response> cardResponse = 카드_생성_요청(adminCardRequest);
        final Long cardId = extractId(cardResponse);

        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest("Answer", cardId);
        final ExtractableResponse<Response> response = 답변_생성_요청(adminAnswerRequest);

        //when
        final AdminAnswerResponse adminAnswerResponse = response.as(AdminAnswerResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminAnswerResponse.getId()).isNotNull();
        assertThat(adminAnswerResponse.getContent()).isEqualTo("Answer");
        assertThat(adminAnswerResponse.getCardId()).isEqualTo(cardId);
    }

    @Test
    @DisplayName("Answer 생성 - 실패, content가 null일 때")
    void createAnswerWithNullContent() {
        //given
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", categoryId);
        ExtractableResponse<Response> cardResponse = 카드_생성_요청(adminCardRequest);
        final Long cardId = extractId(cardResponse);

        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest(null, cardId);
        final ExtractableResponse<Response> response = 답변_생성_요청(adminAnswerRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("답변 필수 입력값 입니다.");
    }

    @Test
    @DisplayName("Answer 생성 - 실패, cardId가 null일 때")
    void createAnswerWithNullCardId() {
        //given
        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest("Answer", null);
        final ExtractableResponse<Response> response = 답변_생성_요청(adminAnswerRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카드의 Id는 필수 입력값 입니다.");
    }


    private Long extractId(ExtractableResponse<Response> response) {
        final String location = response.header("Location");
        return Long.parseLong(location.split("/")[3]);
    }
}
