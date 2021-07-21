package botobo.core.admin;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.admin.dto.AdminWorkbookResponse;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static botobo.core.TestUtils.extractId;
import static botobo.core.TestUtils.longStringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin 인수 테스트")
public class AdminAcceptanceTest extends AuthAcceptanceTest {

    private static final AdminWorkbookRequest ADMIN_CATEGORY_REQUEST =
            new AdminWorkbookRequest("관리자의 문제집");

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminWorkbookRequest)
                .when().post("/api/admin/workbooks")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminCardRequest)
                .when().post("/api/admin/cards")
                .then().log().all()
                .extract();
    }


    @Test
    @DisplayName("관리자 문제집 생성 - 성공")
    void createCategory() {
        // given
        final ExtractableResponse<Response> response = 문제집_생성_요청(ADMIN_CATEGORY_REQUEST);

        // when
        final AdminWorkbookResponse adminWorkbookResponse = response.as(AdminWorkbookResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminWorkbookResponse.getId()).isNotNull();
        assertThat(adminWorkbookResponse.getName()).isEqualTo("관리자의 문제집");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name이 null일 때")
    void createCategoryWithNullName() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithNullName = new AdminWorkbookRequest(null);
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithNullName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }


    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name은 최소 1글자")
    void createCategoryWithInvalidLengthWithZero() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest("");
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name에 공백만 들어가는 경우")
    void createCategoryWithOnlyWhiteSpace() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest("     ");
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name은 최대 30글자")
    void createCategoryWithInvalidLengthWith31() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest(
                longStringGenerator(31));
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 최소 1글자, 최대 30글자만 가능합니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 성공")
    void createCard() {
        ExtractableResponse<Response> workbookResponse = 문제집_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long workbookId = extractId(workbookResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final AdminCardResponse adminCardResponse = response.as(AdminCardResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminCardResponse.getId()).isNotNull();
        assertThat(adminCardResponse.getQuestion()).isEqualTo("Question");
        assertThat(adminCardResponse.getAnswer()).isEqualTo("Answer");
        assertThat(adminCardResponse.getWorkbookId()).isEqualTo(workbookId);
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, question이 공백일 때")
    void createCardWithBlankQuestion() {
        ExtractableResponse<Response> categoryResponse = 문제집_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long workbookId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("     ", "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, question이 null일 때")
    void createCardWithNullQuestion() {
        ExtractableResponse<Response> categoryResponse = 문제집_생성_요청(ADMIN_CATEGORY_REQUEST);
        final Long workbookId = extractId(categoryResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest(null, "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, workbookId가 null일 때")
    void createCardWithNullCategoryId() {
        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", "Answer", null);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카드가 포함될 문제집 아이디는 필수 입력값입니다.");
    }

}
