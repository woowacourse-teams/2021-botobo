package botobo.core.admin;

import botobo.core.AcceptanceTest;
import botobo.core.admin.dto.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin 인수 테스트")
public class AdminAcceptanceTest extends AcceptanceTest {

    private static AdminCategoryRequest ADMIN_CATEGORY_REQUEST =
            new AdminCategoryRequest("Category", "botobo.io", "this is category.");

    public static ExtractableResponse<Response> 카테고리_생성_요청(AdminCategoryRequest adminCategoryRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminCategoryRequest)
                .when().post("/admin/categories")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminCardRequest)
                .when().post("/admin/cards")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 답변_생성_요청(AdminAnswerRequest adminAnswerRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminAnswerRequest)
                .when().post("/admin/answers")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Category 생성 - 성공")
    void createCategory() {
        // given
        AdminCategoryRequest adminCategoryRequest = new AdminCategoryRequest("Java", "botobo.io", "자바입니다.");
        final ExtractableResponse<Response> response = 카테고리_생성_요청(adminCategoryRequest);

        // when
        final AdminCategoryResponse adminCategoryResponse = response.as(AdminCategoryResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminCategoryResponse.getId()).isNotNull();
        assertThat(adminCategoryResponse.getName()).isEqualTo("Java");
        assertThat(adminCategoryResponse.getLogoUrl()).isEqualTo("botobo.io");
        assertThat(adminCategoryResponse.getDescription()).isEqualTo("자바입니다.");
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

    private Long extractId(ExtractableResponse<Response> response) {
        final String location = response.header("Location");
        return Long.parseLong(location.split("/")[3]);
    }
}
