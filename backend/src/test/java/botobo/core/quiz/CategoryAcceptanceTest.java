package botobo.core.quiz;

import botobo.core.AcceptanceTest;
import botobo.core.quiz.dto.CategoryResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static botobo.core.Fixture.*;
import static botobo.core.admin.AdminAcceptanceTest.여러개_카테고리_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category 인수 테스트")
public class CategoryAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_카테고리_생성_요청(Arrays.asList(CATEGORY_REQUEST_1, CATEGORY_REQUEST_2, CATEGORY_REQUEST_3));
    }

    @Test
    @DisplayName("카레고리 전체 조회 - 성공")
    void findAllCategories() {
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
