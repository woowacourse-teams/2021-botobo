package botobo.core.category;

import static org.assertj.core.api.Assertions.assertThat;

import botobo.AcceptanceTest;
import botobo.core.category.dto.CategoryResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("Category 인수 테스트")
public class CategoryAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("카레고리 전체 조회 - 성공")
    void createStation() {
        // given
        final ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/categories")
                .then().log().all()
                .extract();

        // when
        final List<CategoryResponse> categoryResponses = response.body().jsonPath().getList(".", CategoryResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(categoryResponses.size()).isEqualTo(3);
    }
}
