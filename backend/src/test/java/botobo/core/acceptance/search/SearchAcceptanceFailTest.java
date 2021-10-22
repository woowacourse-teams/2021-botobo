package botobo.core.acceptance.search;

import botobo.core.acceptance.AcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchAcceptanceFailTest extends AcceptanceTest {
    @Test
    @DisplayName("문제집 검색 - 실패, 검색어 없음")
    void searchWithNoKeyword() {
        // given
        Map<String, Object> parameters = Map.of("type", "name");

        // when
        RequestBuilder.HttpResponse response = request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 null일 수 없습니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 30자를 초과하는 검색어")
    void searchWithLongKeyword() {
        // given
        Map<String, Object> parameters = Map.of("keyword", stringGenerator(31));

        // when
        RequestBuilder.HttpResponse response = request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("검색어는 30자 이하여야 합니다");
    }

    @Test
    @DisplayName("문제집 검색 - 실패, 지원하지 않는 정렬 기준")
    void searchWithInvalidCriteria() {
        // given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("criteria", "random");

        // when
        RequestBuilder.HttpResponse response = request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 정렬 조건입니다. 유효한 정렬 조건 : date, name, count, heart");
    }

    @Test
    @DisplayName("문제집 검색 - 올바르지 않은 시작 페이지")
    void searchWithInvalidStart() {
        // given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("start", "-1");

        // when
        RequestBuilder.HttpResponse response = request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("페이지의 시작 값은 음수가 될 수 없습니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "101"})
    @DisplayName("문제집 검색 - 올바르지 않은 페이지 크기")
    void searchWithInvalidSize(String size) {
        // given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "java");
        parameters.put("size", size);

        // when
        RequestBuilder.HttpResponse response = request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).contains("유효하지 않은 페이지 크기입니다. 유효한 크기 : 1 ~ 100");
    }
}
