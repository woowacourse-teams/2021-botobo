package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import botobo.core.utils.RequestBuilder.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static botobo.core.utils.Fixture.CARD_REQUEST_1;
import static botobo.core.utils.Fixture.CARD_REQUEST_2;
import static botobo.core.utils.Fixture.CARD_REQUEST_3;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_2;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_3;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Workbook 인수 테스트")
public class WorkbookAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
    }

    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAllCategories() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks")
                .auth()
                .build();

        // then
        List<WorkbookResponse> workbookResponses = response.convertBodyToList(WorkbookResponse.class);
        assertThat(workbookResponses.get(0).getCardCount()).isEqualTo(3);
        assertThat(workbookResponses.get(1).getCardCount()).isZero();
        assertThat(workbookResponses.get(2).getCardCount()).isZero();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookResponses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 존재) - 성공")
    void findCategoryCardsById() {
        // when
        final HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", 1L)
                .auth()
                .build();
        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getName()).isEqualTo(WORKBOOK_REQUEST_1.getName());
        assertThat(workbookCardResponse.getCards()).hasSize(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 0개) - 성공")
    void findWorkbookCardsByIdWithNotExistsCard() {
        // when
        HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", 2L)
                .auth()
                .build();

        // then
        final WorkbookCardResponse workbookCardResponse = response.convertBody(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(workbookCardResponse.getName()).isEqualTo(WORKBOOK_REQUEST_2.getName());
        assertThat(workbookCardResponse.getCards()).isEmpty();
    }
}
