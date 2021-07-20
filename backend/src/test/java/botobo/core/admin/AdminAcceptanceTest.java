package botobo.core.admin;

import botobo.core.admin.dto.AdminAnswerRequest;
import botobo.core.admin.dto.AdminAnswerResponse;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.admin.dto.AdminCategoryResponse;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static botobo.core.TestUtils.extractId;
import static botobo.core.TestUtils.longStringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin 인수 테스트")
public class AdminAcceptanceTest extends AuthAcceptanceTest {

    private static final AdminCategoryRequest ADMIN_CATEGORY_REQUEST =
            new AdminCategoryRequest("Category");

    public ExtractableResponse<Response> 카테고리_생성_요청(AdminCategoryRequest adminCategoryRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminCategoryRequest)
                .when().post("/admin/categories")
                .then().log().all()
                .extract();
    }

    public void 여러개_카테고리_생성_요청(List<AdminCategoryRequest> requests) {
        for (AdminCategoryRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/admin/categories")
                    .then().log().all()
                    .extract();
        }
    }

    public ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminCardRequest)
                .when().post("/admin/cards")
                .then().log().all()
                .extract();
    }

    public void 여러개_카드_생성_요청(List<AdminCardRequest> requests) {
        for (AdminCardRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/admin/cards")
                    .then().log().all()
                    .extract();
        }
    }

    public ExtractableResponse<Response> 답변_생성_요청(AdminAnswerRequest adminAnswerRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminAnswerRequest)
                .when().post("/admin/answers")
                .then().log().all()
                .extract();
    }

    public void 여러개_답변_생성_요청(List<AdminAnswerRequest> requests) {
        for (AdminAnswerRequest request : requests)
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
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
    }

    @Test
    @DisplayName("Category 생성 - 실패, name이 null일 때")
    void createCategoryWithNullName() {
        //given
        AdminCategoryRequest adminCategoryRequestWithNullName = new AdminCategoryRequest(null);
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithNullName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 필수 입력값입니다.");
    }


    @Test
    @DisplayName("Category 생성 - 실패, name은 최소 1글자")
    void createCategoryWithInvalidLengthWithZero() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidName = new AdminCategoryRequest("");
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, name에 공백만 들어가는 경우")
    void createCategoryWithOnlyWhiteSpace() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidName = new AdminCategoryRequest("     ");
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("Category 생성 - 실패, name은 최대 30글자")
    void createCategoryWithInvalidLengthWith31() {
        //given
        AdminCategoryRequest adminCategoryRequestWithInvalidName = new AdminCategoryRequest(
                longStringGenerator(31)
        );
        ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카테고리명은 최소 1글자, 최대 30글자만 가능합니다.");
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
    @DisplayName("Card 생성 - 실패, question이 공백일 때")
    void createCardWithBlankQuestion() {
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("     ", categoryId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
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
    @DisplayName("Answer 생성 - 실패, content가 공백일 때")
    void createAnswerWithBlankContent() {
        //given
        ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long categoryId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", categoryId);
        ExtractableResponse<Response> cardResponse = 카드_생성_요청(adminCardRequest);
        final Long cardId = extractId(cardResponse);

        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest("     ", cardId);
        final ExtractableResponse<Response> response = 답변_생성_요청(adminAnswerRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("답변 필수 입력값 입니다.");
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

}
