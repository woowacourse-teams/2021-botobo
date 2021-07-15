package botobo.core.quiz;

import botobo.core.AcceptanceTest;
import botobo.core.quiz.dto.CategoryCardsResponse;
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

import static botobo.core.Fixture.ANSWER_REQUEST_1;
import static botobo.core.Fixture.ANSWER_REQUEST_2;
import static botobo.core.Fixture.ANSWER_REQUEST_3;
import static botobo.core.Fixture.CARD_REQUEST_1;
import static botobo.core.Fixture.CARD_REQUEST_2;
import static botobo.core.Fixture.CARD_REQUEST_3;
import static botobo.core.Fixture.CATEGORY_REQUEST_1;
import static botobo.core.Fixture.CATEGORY_REQUEST_2;
import static botobo.core.Fixture.CATEGORY_REQUEST_3;
import static botobo.core.admin.AdminAcceptanceTest.여러개_답변_생성_요청;
import static botobo.core.admin.AdminAcceptanceTest.여러개_카드_생성_요청;
import static botobo.core.admin.AdminAcceptanceTest.여러개_카테고리_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category 인수 테스트")
public class CategoryAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_카테고리_생성_요청(Arrays.asList(CATEGORY_REQUEST_1, CATEGORY_REQUEST_2, CATEGORY_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
        여러개_답변_생성_요청(Arrays.asList(ANSWER_REQUEST_1, ANSWER_REQUEST_2, ANSWER_REQUEST_3));
    }

    @Test
    @DisplayName("카레고리 전체 조회 - 성공")
    void findAllCategories() {
        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/categories")
                        .then().log().all()
                        .extract();

        // then
        final List<CategoryResponse> categoryResponses = response.body().jsonPath().getList(".", CategoryResponse.class);
        assertThat(categoryResponses.get(0).getCardCount()).isEqualTo(3);
        assertThat(categoryResponses.get(1).getCardCount()).isEqualTo(0);
        assertThat(categoryResponses.get(2).getCardCount()).isEqualTo(0);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(categoryResponses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리의 카드 모아보기 - 성공")
    void findCategoryCardsById() {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/categories/{id}/cards", 1L)
                        .then().log().all()
                        .extract();
        // then
        final CategoryCardsResponse categoryCardsResponse = response.as(CategoryCardsResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(categoryCardsResponse.getCategoryName()).isEqualTo(CATEGORY_REQUEST_1.getName());
        assertThat(categoryCardsResponse.getCards()).hasSize(3);
    }
}
