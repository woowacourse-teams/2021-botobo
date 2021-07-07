package botobo.core.category;

import botobo.core.AcceptanceTest;
import botobo.core.category.dto.CategoryIdsRequest;
import botobo.core.category.dto.QuizResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Quiz 인수 테스트")
@Sql("classpath:data.sql")
public class QuizAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        CategoryIdsRequest categoryIdsRequest =
                new CategoryIdsRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when
        final List<QuizResponse> quizResponses = response.body().jsonPath().getList(".", QuizResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(quizResponses.size()).isEqualTo(10);
    }
}
