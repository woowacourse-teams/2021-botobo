package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

@DisplayName("문제집 서비스 테스트")
@MockitoSettings
class WorkbookServiceTest {

    @InjectMocks
    private WorkbookService workbookService;

    @Mock
    private WorkbookRepository workbookRepository;

    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAll() {
        // given
        List<Workbook> workbooks = Arrays.asList(
                Workbook.builder().id(1L).name("workbook1").build(),
                Workbook.builder().id(2L).name("workbook2").build(),
                Workbook.builder().id(3L).name("workbook3").build()
        );
        given(workbookRepository.findAll()).willReturn(workbooks);

        // when
        List<WorkbookResponse> workbookResponses = workbookService.findAll();

        // then
        then(workbookRepository)
                .should(times(1))
                .findAll();

        assertThat(workbookResponses).extracting("id")
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("문제집 카드 모아보기 - 성공")
    void findWorkbookCardsById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("java")
                .build();
        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .build();

        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        WorkbookCardResponse workbookCardResponse = workbookService.findWorkbookCardsById(workbook.getId());

        // then
        assertThat(workbookCardResponse.getName()).isEqualTo("java");
        assertThat(workbookCardResponse.getCards().size()).isEqualTo(1);

        then(workbookRepository)
                .should(times(1))
                .findById(anyLong());
    }
}