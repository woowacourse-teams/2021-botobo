package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.quiz.dto.NextQuizCardsRequest;
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

import static botobo.core.Fixture.CARD_REQUEST_1;
import static botobo.core.Fixture.CARD_REQUEST_2;
import static botobo.core.Fixture.CARD_REQUEST_3;
import static botobo.core.Fixture.WORKBOOK_REQUEST_1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("카드 인수 테스트")
public class CardAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        문제집_생성_요청(WORKBOOK_REQUEST_1);
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(request)
                        .when().put("/api/cards/next-quiz")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
