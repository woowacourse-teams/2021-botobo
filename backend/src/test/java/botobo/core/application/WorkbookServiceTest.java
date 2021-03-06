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
import botobo.core.domain.workbook.WorkbookQueryRepository;
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

@DisplayName("????????? ????????? ?????????")
@MockitoSettings
class WorkbookServiceTest {

    @Mock
    private WorkbookRepository workbookRepository;

    @Mock
    private WorkbookQueryRepository workbookQueryRepository;

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
                        Workbook.builder().id(1L).name("??????????????????").opened(true).user(adminUser).build(),
                        Workbook.builder().id(2L).name("??????").opened(true).user(normalUser).build(),
                        Workbook.builder().id(3L).name("??????????????????").opened(true).user(normalUser).build(),
                        Workbook.builder().id(4L).name("????????????").opened(true).user(normalUser).build()
                )
        );
    }

    @Test
    @DisplayName("????????? ????????? ?????? - ??????")
    void createWorkbookByUser() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("????????? Java")
                .opened(true)
                .tags(Arrays.asList(
                        TagRequest.builder().id(0L).name("??????").build(),
                        TagRequest.builder().id(0L).name("java").build()
                ))
                .build();

        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
                .opened(true)
                .deleted(false)
                .tags(Tags.of(Arrays.asList(
                        Tag.of("??????"), Tag.of("java")
                )))
                .user(normalUser)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(normalUser));
        given(tagService.convertTags(any())).willReturn(Tags.of(
                Arrays.asList(Tag.of("??????"), Tag.of("java"))
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
    @DisplayName("?????? ?????? ????????? ?????? ?????? - ??????")
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
    @DisplayName("?????? ????????? ???????????? - ??????")
    void findPublicWorkbookById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("???????????? ?????? ?????????")
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

        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.of(workbook));

        // when
        WorkbookCardResponse response = workbookService.findPublicWorkbookById(1L, normalUser.toAppUser());

        // then
        assertThat(response.getHeartCount()).isEqualTo(1);
        assertThat(response.getWorkbookOpened()).isTrue();
        assertThat(response.getHeart()).isTrue();

        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ???????????? - ??????")
    void findPublicWorkbookByIdWithAnonymousAppUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("???????????? ?????? ?????????")
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

        given(workbookRepository.findByIdAndOrderCardByNew(anyLong())).willReturn(Optional.of(workbook));

        // when
        WorkbookCardResponse response = workbookService.findPublicWorkbookById(1L, AppUser.anonymous());

        // then
        assertThat(response.getHeartCount()).isEqualTo(1);
        assertThat(response.getWorkbookOpened()).isTrue();
        assertThat(response.getHeart()).isFalse();

        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("?????? ????????? ???????????? - ??????, ???????????? ?????? ???????????? ?????? ??????")
    void findPublicWorkbookWithFalseOpenedById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("???????????? ?????? ?????????")
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
    @DisplayName("?????? ????????? ???????????? - ??????, ???????????? ???????????? ?????? ??????")
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
    @DisplayName("????????? ????????? ?????? ???????????? - ??????")
    void findWorkbookCardsById() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
        assertThat(workbookCardResponse.getWorkbookOpened()).isTrue();
        assertThat(workbookCardResponse.getCards()).hasSize(2);

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findByIdAndOrderCardByNew(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ?????? ???????????? - ??????, ????????? ???????????? ?????? ??????")
    void findWorkbookCardsByIdWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
    @DisplayName("????????? ????????? ?????? - ??????")
    void updateWorkbook() {
        // given
        Tags tas = Tags.of(
                Arrays.asList(Tag.of("javi"), Tag.of("??????"))
        );
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
                .opened(true)
                .deleted(false)
                .tags(tas)
                .user(normalUser)
                .build();

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("????????? Java??? ?????????")
                .opened(false)
                .cardCount(0)
                .tags(Arrays.asList(
                        TagRequest.builder().id(1L).name("java").build(),
                        TagRequest.builder().id(2L).name("??????").build()
                ))
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(normalUser));
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(tagService.convertTags(any())).willReturn(Tags.of(
                Arrays.asList(Tag.of("java"), Tag.of("??????"), Tag.of("??????"))
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
                .containsExactly("java", "??????", "??????");

        then(userRepository).should(times(1))
                .findById(anyLong());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(tagService).should(times(1))
                .convertTags(any());
    }

    @Test
    @DisplayName("????????? ????????? ?????? - ??????, ?????? ????????? ????????? ????????? ???")
    void updateWorkbookWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
                .name("????????? Java??? ?????????")
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
    @DisplayName("????????? ????????? ?????? - ??????")
    void deleteWorkbook() {
        // given
        Tags tas = Tags.of(
                Arrays.asList(Tag.of("java"), Tag.of("??????"))
        );
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
    @DisplayName("????????? ????????? ?????? - ??????, ?????? ????????? ????????? ????????? ???")
    void deleteWorkbookWithOtherUser() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
    @DisplayName("??????????????? ?????? ???????????? - ??????")
    void scrapSelectedCardsToWorkbook() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.user(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card = Card.builder()
                .id(1L)
                .question("??????")
                .answer("??????")
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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ?????? Card Id??? ????????? ??? ????????? ????????????.")
    void scrapSelectedCardsToWorkbookWhenDuplicateCardIds() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L, 1L))
                        .build();

        AppUser appUser = AppUser.user(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card = Card.builder()
                .id(1L)
                .question("??????")
                .answer("??????")
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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ???????????? ???????????? ??????.")
    void scrapSelectedCardsToWorkbookFailedWhenWorkbookNotFound() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.user(1L);

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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ????????? ???????????? ??????.")
    void scrapSelectedCardsToWorkbookFailedWhenUserNotFound() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.user(1000L);

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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ???????????? ????????? Card Id ????????? ???????????? ??????")
    void scrapSelectedCardsToWorkbookFailedWhenPartOfCardIDsNotExist() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L, 2L, 3L))
                        .build();

        AppUser appUser = AppUser.user(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
                .opened(true)
                .deleted(false)
                .user(adminUser)
                .build();

        Card card1 = Card.builder()
                .id(1L)
                .question("??????")
                .answer("??????")
                .build();

        Card card2 = Card.builder()
                .id(1L)
                .question("??????")
                .answer("??????")
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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ???????????? ????????? Card Id ????????? ???????????? ??????")
    void scrapSelectedCardsToWorkbookFailedWhenNotExistCardId() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(100L, 101L, 102L))
                        .build();

        AppUser appUser = AppUser.user(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
    @DisplayName("??????????????? ?????? ???????????? - ??????, ???????????? ???????????? ?????? ??????")
    void scrapSelectedCardsToWorkbookFailedWhenNotAuthor() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(1L))
                        .build();

        AppUser appUser = AppUser.user(1L);
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("????????? Java")
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
    @DisplayName("????????? ?????? - ??????")
    void toggleOnHeart() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("?????????")
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
    @DisplayName("????????? ?????? - ??????")
    void toggleOffHeart() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("?????????")
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
    @DisplayName("?????? ???????????? ????????????. - ??????")
    void findPublicWorkbooks() {
        // given
        given(workbookQueryRepository.findRandomPublicWorkbooks())
                .willReturn(workbooks);

        // when
        List<WorkbookResponse> findWorkbooks = workbookService.findPublicWorkbooks();

        // then
        assertThat(findWorkbooks).hasSize(4);
        then(workbookQueryRepository).should(times(1))
                .findRandomPublicWorkbooks();
    }

    @Test
    @DisplayName("?????? ???????????? ????????????. - ??????, ???????????? ?????? ?????? ????????? ????????????.")
    void findPublicWorkbooksWhenHasPrivate() {
        // given
        given(workbookQueryRepository.findRandomPublicWorkbooks())
                .willReturn(new ArrayList<>(workbooks));
        workbooks.add(Workbook.builder()
                .name("???????????????")
                .opened(false)
                .build());

        // when
        List<WorkbookResponse> findWorkbooks = workbookService.findPublicWorkbooks();

        // then
        assertThat(findWorkbooks).hasSize(4);
        then(workbookQueryRepository).should(times(1))
                .findRandomPublicWorkbooks();
    }
}
