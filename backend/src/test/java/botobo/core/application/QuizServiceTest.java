package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.exception.card.QuizEmptyException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.never;

@DisplayName("퀴즈 서비스 테스트")
@MockitoSettings
class QuizServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkbookRepository workbookRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private QuizService quizService;

    private Workbook workbook;
    private Workbook workbookWithOneCard;

    private List<Card> cards;
    private List<Card> twoCards;

    private AppUser appUser;
    private User user;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .name("name")
                .deleted(false)
                .build();

        workbookWithOneCard = Workbook.builder()
                .name("name")
                .deleted(false)
                .build();

        Card card1 = Card.builder()
                .id(1L)
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .build();

        Card card2 = Card.builder()
                .id(2L)
                .question("question")
                .answer("answer")
                .workbook(workbookWithOneCard)
                .build();

        cards = Arrays.asList(card1, card1, card1, card1, card1, card1, card1, card1, card1, card1);
        twoCards = Arrays.asList(card1, card2);
        appUser = AppUser.admin(1L);
        user = User.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        QuizRequest quizRequest = makeQuizRequest(ids, 10);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.existsById(1L)).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(ids)).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(quizRequest, appUser);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(ids);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .existsById(1L);
    }

    private QuizRequest makeQuizRequest(List<Long> ids, int count) {
        return QuizRequest.builder()
                .workbookIds(ids)
                .count(count)
                .build();
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 실패, 게스트는 사용할 수 없음.")
    void createQuizWhenGuest() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        QuizRequest quizRequest = makeQuizRequest(ids, 10);

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> quizService.createQuiz(quizRequest, appUser))
                .isInstanceOf(UserNotFoundException.class);

        // then
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(cardRepository)
                .should(never())
                .findCardsByWorkbookIds(ids);
        then(workbookRepository)
                .should(never())
                .existsById(1L);
    }

    @Test
    @DisplayName("문제집 id를 이용해 퀴즈 생성 시 마주친 횟수가 증가한다. - 성공")
    void createQuizIncrementEncounterCount() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        QuizRequest quizRequest = makeQuizRequest(ids, 10);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(any())).willReturn(twoCards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(quizRequest, appUser);

        // then
        assertThat(quizResponses.size()).isEqualTo(2);
        for (QuizResponse quizResponse : quizResponses) {
            assertThat(quizResponse.getEncounterCount()).isEqualTo(1);
        }

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(ids);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .existsById(1L);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 실패, 문제집 아이디가 존재하지 않음")
    void createQuizFailedWhenIdNotFound() {
        // given
        List<Long> ids = Collections.singletonList(100L);
        QuizRequest quizRequest = makeQuizRequest(ids, 10);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.existsById(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuiz(quizRequest, appUser))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(never())
                .findCardsByWorkbookIds(ids);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .existsById(100L);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 실패, 퀴즈에 문제가 존재하지 않음")
    void createQuizFailedWhenQuizIsEmpty() {
        // given
        List<Long> ids = Collections.singletonList(100L);
        QuizRequest quizRequest = makeQuizRequest(ids, 10);
        List<Card> emptyCards = new ArrayList<>();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(ids)).willReturn(emptyCards);

        // when
        assertThatThrownBy(() -> quizService.createQuiz(quizRequest, appUser))
                .isInstanceOf(QuizEmptyException.class);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(ids);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(never())
                .existsById(1L);
    }

    @Test
    @DisplayName("문제집 id(Long) 및 다음에 또보기 카드 10개를 포함한 퀴즈 생성 - 성공")
    void createQuizWithNextQuizCards() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        List<Card> cards = listOfNextQuizCards(5, true);
        cards.addAll(listOfNextQuizCards(5, false));

        QuizRequest quizRequest = makeQuizRequest(ids, 10);
        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(user));
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(any())).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(quizRequest, appUser);

        // then
        assertThat(cards).extracting("nextQuiz").doesNotContain(true);
        assertThat(quizResponses.size()).isEqualTo(10);
        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(ids);
        then(userRepository)
                .should(times(1))
                .findById(appUser.getId());
        then(workbookRepository)
                .should(times(1))
                .existsById(1L);
    }

    private List<Card> listOfNextQuizCards(int quantity, boolean nextQuiz) {
        return IntStream.range(0, quantity)
                .mapToObj(num -> card(nextQuiz))
                .collect(Collectors.toList());
    }

    private Card card(boolean nextQuiz) {
        Workbook workbook = Workbook.builder()
                .name("workbook")
                .build();
        return Card.builder()
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .nextQuiz(nextQuiz)
                .build();
    }

    @Test
    @DisplayName("첫 비회원용 퀴즈 생성 요청 - 성공")
    void createQuizForGuest() {
        // when
        List<QuizResponse> quizResponses = quizService.createQuizForGuest();

        //then
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 성공")
    void createQuizFromWorkbook() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuizFromWorkbook(workbookId);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(workbookId);
        then(workbookRepository)
                .should(times(1))
                .existsByIdAndOpenedTrue(workbookId);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 시 마주친 횟수는 증가하지 않는다. - 성공")
    void createQuizFromWorkbookNotIncrementEncounterCount() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuizFromWorkbook(workbookId);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);
        for (QuizResponse quizResponse : quizResponses) {
            assertThat(quizResponse.getEncounterCount()).isEqualTo(0);
        }

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(workbookId);
        then(workbookRepository)
                .should(times(1))
                .existsByIdAndOpenedTrue(workbookId);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집 아이디가 존재하지 않음.")
    void createQuizFromWorkbookFailed() {
        // given
        Long workbookId = 100L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(never())
                .findCardsByWorkbookId(workbookId);
        then(workbookRepository)
                .should(times(1))
                .existsByIdAndOpenedTrue(workbookId);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집이 Public 아님.")
    void createQuizFromWorkbookFailedWhenWorkbookIsNotPublic() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(never())
                .findCardsByWorkbookId(workbookId);
        then(workbookRepository)
                .should(times(1))
                .existsByIdAndOpenedTrue(workbookId);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 퀴즈에 문제가 존재하지 않음.")
    void createQuizFromWorkbookFailedWhenQuizIsEmpty() {
        // given
        Long workbookId = 1L;
        List<Card> emptyCards = new ArrayList<>();
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(emptyCards);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(QuizEmptyException.class);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(workbookId);
        then(workbookRepository)
                .should(times(1))
                .existsByIdAndOpenedTrue(workbookId);
    }
}
