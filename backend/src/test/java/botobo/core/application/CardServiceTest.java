package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardUpdateRequest;
import botobo.core.dto.card.NextQuizCardsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("카드 서비스 테스트")
@MockitoSettings
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private WorkbookRepository workbookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardService cardService;

    private Workbook workbook;
    private User user;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();
        workbook = workbook(1L, user);
        appUser = AppUser.user(1L);
    }

    @Test
    @DisplayName("카드 생성 - 성공")
    void createCard() {
        // given
        CardRequest cardRequest = cardRequest();
        Card card = card(1L, workbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.findById(cardRequest.getWorkbookId())).willReturn(Optional.of(workbook));
        given(cardRepository.save(any())).willReturn(card);

        // when
        cardService.createCard(cardRequest, appUser);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .save(any());
    }

    // TODO 카드 추가 테스트 추가

    @Test
    @DisplayName("카드 수정 - 성공")
    void updateCard() {
        // given
        Long cardId = 1L;
        CardUpdateRequest cardUpdateRequest = cardUpdateRequest(1L);
        Card card = card(cardId, workbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(cardRepository.findById(any())).willReturn(Optional.of(card));

        // when
        cardService.updateCard(1L, cardUpdateRequest, appUser);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .findById(any());
    }

    @Test
    @DisplayName("카드 삭제 - 성공")
    void deleteCard() {
        // given
        Card card = card(1L, workbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(cardRepository.findById(any())).willReturn(Optional.of(card));

        // when
        cardService.deleteCard(1L, appUser);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .deleteById(any());
    }

    @Test
    @DisplayName("다음에 또 보는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest requestWithThreeIds = nextQuizCardsRequestWithThreeIds();
        List<Card> threeCards = listOfThreeCards();
        given(cardRepository.findByIdIn(requestWithThreeIds.getCardIds())).willReturn(threeCards);

        // when
        cardService.selectNextQuizCards(requestWithThreeIds);

        // then
        assertThat(threeCards).extracting("nextQuiz")
                .containsExactly(true, true, true);
        then(cardRepository)
                .should(times(1))
                .findByIdIn(anyList());
    }

    private Workbook workbook(Long id, User user) {
        return Workbook.builder()
                .id(id)
                .name("workbook")
                .user(user)
                .build();
    }

    private Card card(Long id, Workbook workbook) {
        return Card.builder()
                .id(id)
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .build();
    }

    private CardRequest cardRequest() {
        return CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(1L)
                .build();
    }

    private CardUpdateRequest cardUpdateRequest(Long workbookId) {
        return CardUpdateRequest.builder()
                .question("changed question")
                .answer("changed answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();
    }

    private List<Card> listOfThreeCards() {
        return List.of(
                card(1L, workbook(1L, user)),
                card(2L, workbook(1L, user)),
                card(3L, workbook(1L, user))
        );
    }

    private NextQuizCardsRequest nextQuizCardsRequestWithThreeIds() {
        return NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();
    }
}