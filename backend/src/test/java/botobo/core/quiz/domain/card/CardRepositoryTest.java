package botobo.core.quiz.domain.card;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Workbook workbook;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .name("java")
                .isDeleted(false)
                .build();
        workbookRepository.save(workbook);
    }

    @Test
    @DisplayName("Card 저장 - 성공")
    void save() {
        // given
        Card card = generateCard();

        // when
        Card savedCard = cardRepository.save(card);

        testEntityManager.flush();

        // then
        assertThat(savedCard.getId()).isNotNull();
        assertThat(savedCard).isSameAs(card);
        assertThat(savedCard.getCreatedAt()).isNotNull();
        assertThat(savedCard.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Id로 Card 찾기 - 성공")
    void findById() {
        // given
        Card card = generateCard();

        // when
        Card savedCard = cardRepository.save(card);

        // when
        Optional<Card> findCard = cardRepository.findById(savedCard.getId());
        assertThat(findCard).containsSame(savedCard);
    }

    @Test
    @DisplayName("Card 추가 시, 문제집도 함께 추가 - 성공")
    void checkCategoryIsSaved() {
        // given
        Card card = generateCard();

        Card savedCard = cardRepository.save(card);

        // when
        Optional<Card> findCard = cardRepository.findById(savedCard.getId());

        // then
        assertThat(findCard).isPresent();
        assertThat(findCard.get().getWorkbook()).isNotNull();
    }

    @Test
    @DisplayName("카드 id 리스트로 카드들 찾기 - 성공")
    void findByInventoryIds() {
        // given
        Card card1 = generateCard();
        Card card2 = generateCard();
        Card card3 = generateCard();

        Card savedCard1 = cardRepository.save(card1);
        Card savedCard2 = cardRepository.save(card2);
        Card savedCard3 = cardRepository.save(card3);

        testEntityManager.flush();
        testEntityManager.clear();

        // when
        List<Card> findCards = cardRepository.findByIdIn(Arrays.asList(
                savedCard1.getId(),
                savedCard2.getId(),
                savedCard3.getId()
        ));

        // then
        assertThat(findCards).hasSize(3);
    }

    @Test
    @DisplayName("존재하지 않는 카드 아이디 포함하여 카드 id 리스트로 카드들 찾기 - 성공")
    void findByInventoryIdsWithNonExistingId() {
        // given
        Card card1 = generateCard();
        Card card2 = generateCard();

        Card savedCard1 = cardRepository.save(card1);
        Card savedCard2 = cardRepository.save(card2);

        testEntityManager.flush();
        testEntityManager.clear();

        // when
        List<Card> findCards = cardRepository.findByIdIn(Arrays.asList(
                savedCard1.getId(),
                savedCard2.getId(),
                savedCard2.getId() + 1
        ));

        // then
        assertThat(findCards).hasSize(2);
    }

    private Card generateCard() {
        return Card.builder()
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .build();
    }
}