package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookQueryRepository;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ActiveProfiles("test")
@SpringBootTest
class WorkbookRankServiceTest {

    @Autowired
    CacheManager cacheManager;

    @MockBean
    private WorkbookQueryRepository workbookQueryRepository;

    @Autowired
    private WorkbookRankService workbookRankService;

    private Workbook emptyWorkbook;
    private Workbook workbookWithTags;
    private Workbook workbookWithCards;

    @BeforeEach
    void setUp() {
        emptyWorkbook = Workbook.builder()
                .name("ozBook")
                .opened(true)
                .build();
        workbookWithTags = Workbook.builder()
                .name("joanneBook")
                .opened(true)
                .tags(Tags.of(List.of(Tag.of("java"), Tag.of("spring"))))
                .build();
        workbookWithCards = Workbook.builder()
                .name("middleBearBook")
                .opened(true)
                .build();
        workbookWithCards.addCards(generateTwoCards());
    }

    @AfterEach
    void tearDown() {
        Cache workbookRanks = cacheManager.getCache("workbookRanks");
        Objects.requireNonNull(workbookRanks).clear();
    }

    @Test
    @DisplayName("인기 문제집 세 개를 가지고 온다 - 성공, 캐싱된 정보가 있으면 DB 조회를 하지 않고 캐싱된 것을 가지고 온다")
    void findWorkbookRanks() {
        // given
        given(workbookQueryRepository.searchAll(
                any(WorkbookSearchParameter.class)
        )).willReturn(List.of(emptyWorkbook, workbookWithTags, workbookWithCards));

        // when
        workbookRankService.findWorkbookRanks();
        workbookRankService.findWorkbookRanks();
        workbookRankService.findWorkbookRanks();

        // then
        then(workbookQueryRepository).should(times(1)).searchAll(
                any(WorkbookSearchParameter.class)
        );
    }

    @Test
    @DisplayName("캐싱된 인기 문제집 제거 - 성공")
    void removeWorkbookRanksCache() {
        // given
        given(workbookQueryRepository.searchAll(
                any(WorkbookSearchParameter.class)
        )).willReturn(List.of(emptyWorkbook, workbookWithTags, workbookWithCards));
        workbookRankService.findWorkbookRanks();

        // when
        workbookRankService.removeWorkbookRanksCache();

        // then
        workbookRankService.findWorkbookRanks();
        then(workbookQueryRepository).should(times(2)).searchAll(
                any(WorkbookSearchParameter.class)
        );
    }

    private Cards generateTwoCards() {
        return new Cards(List.of(
                Card.builder()
                        .question("question1")
                        .answer("answer1")
                        .build(),
                Card.builder()
                        .question("question2")
                        .answer("answer2")
                        .build()
        ));
    }
}
