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
import botobo.core.exception.NotAuthorException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
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

    private Workbook workbook, temporaryWorkbook;

    private User user, anotherUser;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();
        anotherUser = User.builder().id(2L).build();
        workbook = workbook(1L, user);
        temporaryWorkbook = Workbook.builder().name("temporary").build();
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

    @Test
    @DisplayName("카드 생성 - 실패, 유저가 존재하지 않음.")
    void createCardFailedWhenUserNotExist() {
        // given
        CardRequest cardRequest = cardRequest();
        Card card = card(1L, workbook);
        AppUser appUser = AppUser.user(100L);

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> cardService.createCard(cardRequest, appUser))
                .isInstanceOf(UserNotFoundException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(never())
                .findById(anyLong());
        then(cardRepository)
                .should(never())
                .save(any(Card.class));
    }

    @Test
    @DisplayName("카드 생성 - 실패, 작성자가 아닌 유저")
    void createCardFailedWhenNotAuthor() {
        // given
        CardRequest cardRequest = cardRequest();
        Card card = card(1L, workbook);
        AppUser appUser = AppUser.user(2L);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(anotherUser));
        given(workbookRepository.findById(cardRequest.getWorkbookId())).willReturn(Optional.of(workbook));

        // when
        assertThatThrownBy(() -> cardService.createCard(cardRequest, appUser))
                .isInstanceOf(NotAuthorException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .findById(cardRequest.getWorkbookId());
        then(cardRepository)
                .should(never())
                .save(any(Card.class));
    }

    @Test
    @DisplayName("카드 생성 - 실패, 문제집이 존재하지 않음.")
    void createCardFailedWhenWorkbookNotExist() {
        // given
        CardRequest cardRequest = cardRequest();
        Card card = card(1L, temporaryWorkbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.findById(cardRequest.getWorkbookId())).willThrow(WorkbookNotFoundException.class);

        // when
        assertThatThrownBy(() -> cardService.createCard(cardRequest, appUser))
                .isInstanceOf(WorkbookNotFoundException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .findById(cardRequest.getWorkbookId());
        then(cardRepository)
                .should(never())
                .save(any(Card.class));
    }

    @Test
    @DisplayName("카드 수정 - 성공")
    void updateCard() {
        // given
        long cardId = 1L;
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
    @DisplayName("카드 수정 - 실패, 존재하지 않는 유저")
    void updateCardFailedWhenNotExistUser() {
        // given
        long cardId = 1L;
        CardUpdateRequest cardUpdateRequest = cardUpdateRequest(1L);
        Card card = card(cardId, workbook);
        AppUser appUser = AppUser.user(100L);

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> cardService.updateCard(1L, cardUpdateRequest, appUser))
                .isInstanceOf(UserNotFoundException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(never())
                .findById(anyLong());
    }

    @Test
    @DisplayName("카드 수정 - 실패, 작성자가 아닌 유저")
    void updateCardFailedWhenNotAuthor() {
        // given
        long cardId = 1L;
        CardUpdateRequest cardUpdateRequest = cardUpdateRequest(1L);
        Card card = card(cardId, workbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(anotherUser));
        given(cardRepository.findById(any())).willReturn(Optional.of(card));

        // when
        assertThatThrownBy(() -> cardService.updateCard(1L, cardUpdateRequest, appUser))
                .isInstanceOf(NotAuthorException.class);

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
                .findById(any());
    }

    @Test
    @DisplayName("카드 삭제 - 실패, 존재하지 않는 유저")
    void deleteCardFailedWhenNotExistUser() {
        // given
        Card card = card(1L, workbook);
        AppUser appUser = AppUser.user(100L);

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> cardService.deleteCard(1L, appUser))
                .isInstanceOf(UserNotFoundException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(never())
                .findById(anyLong());
    }

    @Test
    @DisplayName("카드 삭제 - 실패, 작성자가 아닌 유저")
    void deleteCardFailedWhenNotAuthor() {
        // given
        Card card = card(1L, workbook);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(anotherUser));
        given(cardRepository.findById(any())).willReturn(Optional.of(card));

        // when
        assertThatThrownBy(() -> cardService.deleteCard(1L, appUser))
                .isInstanceOf(NotAuthorException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .findById(any());
    }

    @Test
    @DisplayName("다음에 또 보는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest requestWithThreeIds = nextQuizCardsRequestWithThreeIds();
        List<Card> threeCards = listOfThreeCards();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(cardRepository.findByIdIn(requestWithThreeIds.getCardIds())).willReturn(threeCards);

        // when
        cardService.selectNextQuizCards(requestWithThreeIds, appUser);

        // then
        assertThat(threeCards).extracting("nextQuiz")
                .containsExactly(true, true, true);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .findByIdIn(anyList());
    }

    @Test
    @DisplayName("다음에 또 보는 카드 선택 - 실패, 존재하지 않는 유저")
    void selectNextQuizCardsWhenUserNotFound() {
        // given
        NextQuizCardsRequest requestWithThreeIds = nextQuizCardsRequestWithThreeIds();
        List<Card> threeCards = listOfThreeCards();

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when - then
        assertThatThrownBy(() -> cardService.selectNextQuizCards(requestWithThreeIds, appUser))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(never())
                .findByIdIn(anyList());
    }

    @Test
    @DisplayName("다음에 또 보는 카드 선택 - 실패, author가 아닌 유저")
    void selectNextQuizCardsWhenUserNotAuthor() {
        // given
        NextQuizCardsRequest requestWithThreeIds = nextQuizCardsRequestWithThreeIds();
        List<Card> threeCards = listOfThreeCards();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(anotherUser));
        given(cardRepository.findByIdIn(requestWithThreeIds.getCardIds())).willReturn(threeCards);

        // when - then
        assertThatThrownBy(() -> cardService.selectNextQuizCards(requestWithThreeIds, appUser))
                .isInstanceOf(NotAuthorException.class);

        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(times(1))
                .findByIdIn(anyList());
    }

    private Workbook workbook(long id, User user) {
        return Workbook.builder()
                .id(id)
                .name("workbook")
                .user(user)
                .build();
    }

    private Card card(long id, Workbook workbook) {
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

    private CardUpdateRequest cardUpdateRequest(long workbookId) {
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
