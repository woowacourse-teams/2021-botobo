package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.heart.HeartResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.user.NotAuthorException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.NotOpenedWorkbookException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("문제집 서비스 테스트")
@MockitoSettings
class WorkbookServiceTest {

    @Mock
    private WorkbookRepository workbookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private WorkbookService workbookService;

    private User adminUser, normalUser;

    private List<Workbook> workbooks;

    @BeforeEach
    void setUp() {
        adminUser = User.builder().id(1L).userName("botobo").role(Role.ADMIN).build();
        normalUser = User.builder().id(2L).userName("ggyool").role(Role.USER).build();

        workbooks = new ArrayList<>();
        workbooks.addAll(
                Arrays.asList(
                        Workbook.builder().id(1L).name("데이터베이스").opened(true).user(adminUser).build(),
                        Workbook.builder().id(2L).name("자바").opened(true).user(normalUser).build(),
                        Workbook.builder().id(3L).name("자바스크립트").opened(true).user(normalUser).build(),
                        Workbook.builder().id(4L).name("네트워크").opened(true).user(normalUser).build()
                )
        );
    }

    @Test
    @DisplayName("유저가 문제집 생성 - 성공")
    void createWorkbookByUser() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("오즈의 Java")
                .opened(true)
                .tags(Arrays.asList(
                        TagRequest.builder().id(0L).name("자바").build(),
                        TagRequest.builder().id(0L).name("java").build()
                ))
                .build();

        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(Tags.of(Arrays.asList(
                        Tag.of("자바"), Tag.of("java")
                )))
                .user(normalUser)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(normalUser));
        given(tagService.convertTags(any())).willReturn(Tags.of(
                Arrays.asList(Tag.of("자바"), Tag.of("java"))
        ));
        given(workbookRepository.save(any())).willReturn(workbook);

        // when
        WorkbookResponse workbookResponse = workbookService.createWorkbookByUser(workbookRequest, normalUser.toAppUser());

        //then
        assertThat(workbookResponse.getId()).isEqualTo(workbook.getId());
        assertThat(workbookResponse.getName()).isEqualTo(workbook.getName());
        assertThat(workbookResponse.getOpened()).isEqualTo(workbook.isOpened());
        assertThat(workbookResponse.getCardCount()).isEqualTo(workbook.cardCount());
        assertThat(workbookResponse.getTags()).hasSize(workbook.getWorkbookTags().size());

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(tagService).should(times(1))
                .convertTags(any());
        then(workbookRepository).should(times(1))
                .save(any());
    }

    @Test
    @DisplayName("일반 유저 문제집 전체 조회 - 성공")
    void findWorkbooksByUser() {
        // given
        given(workbookRepository.findAllByUserId(normalUser.getId())).willReturn(
                workbooks.stream().filter(Workbook::authorIsUser).collect(Collectors.toList())
        );

        // when
        List<WorkbookResponse> workbooks = workbookService.findWorkbooksByUser(normalUser.toAppUser());

        // then
        assertThat(workbooks).hasSize(3);

        then(workbookRepository).should(times(1))
                .findAllByUserId(normalUser.getId());
    }

    @Test
    @DisplayName("비회원 문제집을 조회 - 성공, 비어있는 리스트 반환")
    void findWorkbooksByAnonymousUser() {
        // when
        List<WorkbookResponse> workbooks = workbookService.findWorkbooksByUser(AppUser.anonymous());

        // then
        assertThat(workbooks).isEmpty();
    }

    @Test
    @DisplayName("공유 문제집 상세보기 - 성공")
    void findPublicWorkbookById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("피케이의 공유 문제집")
                .cards(new Cards(List.of(
                        Card.builder()
                                .id(1L)
                                .question("question")
                                .answer("answer")
                                .build())
                        )
                )
                .opened(true)
                .build();
        Long userId = normalUser.getId();
        Heart heart = Heart.builder().workbook(workbook).userId(userId).build();
        workbook.toggleHeart(heart);

        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.ofNullable(workbook));

        // when
        WorkbookCardResponse response = workbookService.findPublicWorkbookById(1L, normalUser.toAppUser());

        // then
        assertThat(response.getHeartCount()).isEqualTo(1);
        assertThat(response.getHeart()).isTrue();

        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("비회원 공유 문제집 상세보기 - 성공")
    void findPublicWorkbookByIdWithAnonymousAppUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("피케이의 공유 문제집")
                .cards(new Cards(List.of(
                        Card.builder()
                                .id(1L)
                                .question("question")
                                .answer("answer")
                                .build())
                        )
                )
                .opened(true)
                .build();
        Long userId = normalUser.getId();
        Heart heart = Heart.builder().workbook(workbook).userId(userId).build();
        workbook.toggleHeart(heart);

        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.ofNullable(workbook));

        // when
        WorkbookCardResponse response = workbookService.findPublicWorkbookById(1L, AppUser.anonymous());

        // then
        assertThat(response.getHeartCount()).isEqualTo(1);
        assertThat(response.getHeart()).isFalse();

        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("공유 문제집 상세보기 - 실패, 문제집이 공유 문제집이 아닌 경우")
    void findPublicWorkbookWithFalseOpenedById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("피케이의 공유 문제집")
                .cards(new Cards(List.of(
                        Card.builder()
                                .id(1L)
                                .question("question")
                                .answer("answer")
                                .build())
                        )
                )
                .opened(false)
                .build();
        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.ofNullable(workbook));

        // when
        assertThatThrownBy(() -> workbookService.findPublicWorkbookById(1L, normalUser.toAppUser()))
                .isInstanceOf(NotOpenedWorkbookException.class);

        // then
        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("공유 문제집 상세보기 - 실패, 문제집이 존재하지 않는 경우")
    void findPublicWorkbookByIdFailedWhenWorkbookNotFound() {
        // given
        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> workbookService.findPublicWorkbookById(1L, normalUser.toAppUser()))
                .isInstanceOf(WorkbookNotFoundException.class);

        // then
        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("유저가 문제집 카드 모아보기 - 성공")
    void findWorkbookCardsById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
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
                .workbook(workbook)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(normalUser));
        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.of(workbook));

        // when
        WorkbookCardResponse workbookCardResponse = workbookService.findWorkbookCardsById(workbook.getId(),
                normalUser.toAppUser());

        // then
        assertThat(workbookCardResponse.getWorkbookId()).isEqualTo(workbook.getId());
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(workbook.getName());
        assertThat(workbookCardResponse.getCards()).hasSize(2);

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("유저가 문제집 카드 모아보기 - 실패, 자신의 문제집이 아닌 경우")
    void findWorkbookCardsByIdWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
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
                .workbook(workbook)
                .build();

        User otherUser = User.builder()
                .id(3L)
                .socialId("7")
                .userName("pk")
                .profileUrl("github.io")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(otherUser));
        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.of(workbook));

        // when, then
        assertThatThrownBy(
                () -> workbookService.findWorkbookCardsById(workbook.getId(),
                        normalUser.toAppUser()))
                .isInstanceOf(NotAuthorException.class);

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 성공")
    void updateWorkbook() {
        // given
        Tags tas = Tags.of(
                Arrays.asList(Tag.of("javi"), Tag.of("잡아"))
        );
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(tas)
                .user(normalUser)
                .build();

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("오즈의 Java를 잡아라")
                .opened(false)
                .cardCount(0)
                .tags(Arrays.asList(
                        TagRequest.builder().id(1L).name("java").build(),
                        TagRequest.builder().id(2L).name("자바").build()
                ))
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(normalUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(tagService.convertTags(any())).willReturn(Tags.of(
                Arrays.asList(Tag.of("java"), Tag.of("자바"), Tag.of("중급"))
        ));

        // when
        WorkbookResponse workbookResponse = workbookService.updateWorkbook(normalUser.getId(),
                workbookUpdateRequest, normalUser.toAppUser());

        //then
        assertThat(workbookResponse.getId()).isEqualTo(workbook.getId());
        assertThat(workbookResponse.getName()).isEqualTo(workbookUpdateRequest.getName());
        assertThat(workbookResponse.getOpened()).isEqualTo(workbookUpdateRequest.getOpened());
        assertThat(workbookResponse.getCardCount()).isEqualTo(workbookUpdateRequest.getCardCount());
        assertThat(workbookResponse.getTags()).extracting("name")
                .containsExactly("java", "자바", "중급");

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(tagService).should(times(1))
                .convertTags(any());
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 실패, 다른 유저가 수정을 시도할 때")
    void updateWorkbookWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
                .build();

        User otherUser = User.builder()
                .id(3L)
                .socialId("7")
                .userName("pk")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("오즈의 Java를 잡아라")
                .opened(false)
                .cardCount(0)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(otherUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when, then
        assertThatThrownBy(
                () -> workbookService.updateWorkbook(workbook.getId(),
                        workbookUpdateRequest, otherUser.toAppUser()))
                .isInstanceOf(NotAuthorException.class);

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(tagService).should(never())
                .convertTags(any());
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        Tags tas = Tags.of(
                Arrays.asList(Tag.of("java"), Tag.of("자바"))
        );
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(tas)
                .user(normalUser)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(normalUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        workbookService.deleteWorkbook(normalUser.getId(), normalUser.toAppUser());

        //then
        assertThat(workbook.isDeleted()).isTrue();
        assertThat(workbook.getWorkbookTags()).extracting("deleted")
                .doesNotContain(false);
        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 실패, 다른 유저가 수정을 시도할 때")
    void deleteWorkbookWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
                .build();

        User otherUser = User.builder()
                .id(3L)
                .socialId("7")
                .userName("pk")
                .profileUrl("github.io")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(otherUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when, then
        assertThatThrownBy(
                () -> workbookService.deleteWorkbook(normalUser.getId(), normalUser.toAppUser()))
                .isInstanceOf(NotAuthorException.class);

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 성공")
    void scrapSelectedCardsToWorkbook() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.admin(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("응답")
                .build();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(adminUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(cardRepository.findByIdIn(Mockito.anyList())).willReturn(List.of(card));

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .doesNotThrowAnyException();

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 성공, 중복 Card Id가 존재할 땐 중복을 제거한다.")
    void scrapSelectedCardsToWorkbookWhenDuplicateCardIds() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L, 1L))
                        .build();

        AppUser appUser = AppUser.admin(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("응답")
                .build();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(adminUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(cardRepository.findByIdIn(Mockito.anyList())).willReturn(List.of(card));

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .doesNotThrowAnyException();

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findByIdIn(List.of(1L));
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집이 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenWorkbookNotFound() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.admin(1L);

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(adminUser));
        given(workbookRepository.findById(anyLong())).willThrow(WorkbookNotFoundException.class);

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(WorkbookNotFoundException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(never())
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 유저가 존재하지 않음.")
    void scrapSelectedCardsToWorkbookFailedWhenUserNotFound() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.admin(1000L);

        given(userRepository.findById(appUser.getId())).willThrow(UserNotFoundException.class);

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(UserNotFoundException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(never())
                .findById(anyLong());
        then(cardRepository).should(never())
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 요청으로 들어온 Card Id 일부가 존재하지 않음")
    void scrapSelectedCardsToWorkbookFailedWhenPartOfCardIDsNotExist() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L, 2L, 3L))
                        .build();

        AppUser appUser = AppUser.admin(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card1 = Card.builder()
                .id(1L)
                .question("질문")
                .answer("응답")
                .build();

        Card card2 = Card.builder()
                .id(1L)
                .question("질문")
                .answer("응답")
                .build();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(adminUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(cardRepository.findByIdIn(Mockito.anyList())).willReturn(List.of(card1, card2));

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(CardNotFoundException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 요청으로 들어온 Card Id 모두가 존재하지 않음")
    void scrapSelectedCardsToWorkbookFailedWhenNotExistCardId() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(100L, 101L, 102L))
                        .build();

        AppUser appUser = AppUser.admin(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(adminUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(cardRepository.findByIdIn(Mockito.anyList())).willReturn(List.of());

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(CardNotFoundException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 문제집의 작성자가 아닌 유저")
    void scrapSelectedCardsToWorkbookFailedWhenNotAuthor() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.admin(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        given(userRepository.findById(appUser.getId())).willReturn(Optional.of(normalUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(NotAuthorException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(never())
                .findByIdIn(Mockito.anyList());
    }

    @Test
    @DisplayName("좋아요 요청 - 성공")
    void toggleOnHeart() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("문제집")
                .user(adminUser)
                .build();
        AppUser appUser = normalUser.toAppUser();

        given(workbookRepository.findById(workbook.getId())).willReturn(Optional.of(workbook));

        // when
        HeartResponse heartResponse = workbookService.toggleHeart(workbook.getId(), appUser);

        // then
        assertThat(workbook.getHearts().getHearts()).hasSize(1);
        assertThat(heartResponse.isHeart()).isTrue();
        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("좋아요 취소 - 성공")
    void toggleOffHeart() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("문제집")
                .user(adminUser)
                .build();
        AppUser appUser = normalUser.toAppUser();
        Heart heart = Heart.builder().workbook(workbook).userId(appUser.getId()).build();
        workbook.toggleHeart(heart);

        given(workbookRepository.findById(workbook.getId())).willReturn(Optional.of(workbook));

        // when
        HeartResponse heartResponse = workbookService.toggleHeart(workbook.getId(), appUser);

        // then
        assertThat(workbook.getHearts().getHearts()).hasSize(0);
        assertThat(heartResponse.isHeart()).isFalse();
        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("공개 문제집을 조회한다. - 성공")
    void findPublicWorkbooks() {
        // given
        given(workbookRepository.findRandomPublicWorkbooks())
                .willReturn(workbooks);

        // when
        List<WorkbookResponse> findWorkbooks = workbookService.findPublicWorkbooks();

        // then
        assertThat(findWorkbooks).hasSize(4);
        then(workbookRepository).should(times(1))
                .findRandomPublicWorkbooks();
    }

    @Test
    @DisplayName("공개 문제집을 조회한다. - 성공, 비공개가 있을 경우 공개만 조회한다.")
    void findPublicWorkbooksWhenHasPrivate() {
        // given
        given(workbookRepository.findRandomPublicWorkbooks())
                .willReturn(new ArrayList<>(workbooks));
        workbooks.add(Workbook.builder()
                .name("비공개지롱")
                .opened(false)
                .build());

        // when
        List<WorkbookResponse> findWorkbooks = workbookService.findPublicWorkbooks();

        // then
        assertThat(findWorkbooks).hasSize(4);
        then(workbookRepository).should(times(1))
                .findRandomPublicWorkbooks();
    }
}
