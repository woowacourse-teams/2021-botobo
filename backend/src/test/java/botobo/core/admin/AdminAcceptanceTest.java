package botobo.core.admin;

import botobo.core.AcceptanceTest;
import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.admin.dto.AdminCategoryResponse;
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

    public static ExtractableResponse<Response> 카테고리_생성_요청(AdminCategoryRequest adminCategoryRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminCategoryRequest)
                .when().post("/admin/categories")
                .then().log().all()
                .extract();
    }
}
