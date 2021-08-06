package botobo.core.acceptance.search;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.exception.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchAcceptanceTest extends DomainAcceptanceTest {

    @Test
    @DisplayName("문제집 검색 - 성공, 검색어를 제외한 다른 인자는 생략 가능")
    void searchWithDefault() {
        // give
        Map<String, String> parameters = Map.of("keyword", "java");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 검색어 없음")
    void searchWithNoKeyword() {
        // give
        Map<String, String> parameters = Map.of("type", "name");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 null일 수 없습니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 30자를 초과하는 검색어")
    void searchWithLongKeyword() {
        // give
        Map<String, String> parameters = Map.of("keyword", stringGenerator(31));

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 30자 이하여야 합니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 검색 타입")
    void searchWithInvalidType() {
        // give
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("type", "alphabet");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 검색 타입입니다. 유효한 검색 타임 : name, tag, user");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 정렬 기준")
    void searchWithInvalidCriteria() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("criteria", "random");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 정렬 조건입니다. 유효한 정렬 조건 : date, name, count, like");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 정렬 방법")
    void searchWithInvalidOrder() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("order", "center");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 정렬 방향입니다. 유효한 정렬 방식 : ASC, DESC");
    }

    @Test
    @DisplayName("문제집 검색 - 올바르지 않은 시작 페이지")
    void searchWithInvalidStart() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("start", "-1");

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("페이지의 시작 값은 음수가 될 수 없습니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "101"})
    @DisplayName("문제집 검색 - 올바르지 않은 페이지 크기")
    void searchWithInvalidSize(String size) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("size", size);

        // when
        HttpResponse response = 문제집_검색_요청(parameters);

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 페이지 크기입니다. 유효한 크기 : 1 ~ 100");
    }

    private HttpResponse 문제집_검색_요청(Map<String, String> parameters) {
        return request()
                .get("/api/search/workbooks")
                .queryParams(parameters)
                .auth(createToken(user.getId()))
                .build();
    }
}
