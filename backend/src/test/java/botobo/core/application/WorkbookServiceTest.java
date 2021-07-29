package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.NotAuthorException;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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


    @InjectMocks
    private WorkbookService workbookService;

    private User adminUser, normalUser;

    private List<Workbook> workbooks;

    @BeforeEach
    void setUp() {
        adminUser = User.builder().id(1L).userName("botobo").role(Role.ADMIN).build();
        normalUser = User.builder().id(2L).userName("ggyool").role(Role.USER).build();

        workbooks = Arrays.asList(
                Workbook.builder().id(1L).name("데이터베이스").opened(true).user(adminUser).build(),
                Workbook.builder().id(2L).name("자바").opened(true).user(normalUser).build(),
                Workbook.builder().id(3L).name("자바스크립트").opened(true).user(normalUser).build(),
                Workbook.builder().id(4L).name("네트워크").opened(true).user(normalUser).build()
        );
    }

    @Test
    @DisplayName("유저가 문제집 생성 - 성공")
    void createWorkbookByUser() {
        // given
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("오즈의 Java")
                .opened(true)
                .build();

        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(normalUser));
        given(workbookRepository.save(any())).willReturn(workbook);

        // when
        WorkbookResponse workbookResponse = workbookService.createWorkbookByUser(workbookRequest, normalUser.toAppUser());

        //then
        assertThat(workbookResponse.getId()).isEqualTo(workbook.getId());
        assertThat(workbookResponse.getName()).isEqualTo(workbook.getName());
        assertThat(workbookResponse.getOpened()).isEqualTo(workbook.isOpened());
        assertThat(workbookResponse.getCardCount()).isEqualTo(workbook.cardCount());

        then(userRepository).should(times(1))
                .findById(anyLong());
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
    @DisplayName("비회원이 문제집을 조회하면 비어있는 리스트를 반환한다.")
    void findWorkbooksByAnonymousUser() {
        // when
        List<WorkbookResponse> workbooks = workbookService.findWorkbooksByUser(AppUser.anonymous());

        // then
        assertThat(workbooks).isEmpty();
    }

    @Test
    @DisplayName("검색어를 이용하여 공유 문제집을 조회한다.")
    void findPublicWorkbooksBySearch() {
        // given
        given(workbookRepository.findAll()).willReturn(workbooks);

        // when
        List<WorkbookResponse> workbooks = workbookService.findPublicWorkbooksBySearch("자바");

        // then
        assertThat(workbooks).extracting("name")
                .containsExactlyInAnyOrder("자바", "자바스크립트");

        then(workbookRepository).should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 성공")
    void updateWorkbook() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
                .build();

        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("오즈의 Java를 잡아라")
                .opened(false)
                .cardCount(0)
                .build();

        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        WorkbookResponse workbookResponse = workbookService.updateWorkbook(normalUser.getId(),
                workbookUpdateRequest, normalUser.toAppUser());

        //then
        assertThat(workbookResponse.getId()).isEqualTo(workbook.getId());
        assertThat(workbookResponse.getName()).isEqualTo(workbookUpdateRequest.getName());
        assertThat(workbookResponse.getOpened()).isEqualTo(workbookUpdateRequest.isOpened());
        assertThat(workbookResponse.getCardCount()).isEqualTo(workbookUpdateRequest.getCardCount());

        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(normalUser)
                .build();

        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        workbookService.deleteWorkbook(normalUser.getId(), normalUser.toAppUser());

        //then
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
        given(cardRepository.findById(anyLong())).willReturn(Optional.of(card));

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .doesNotThrowAnyException();

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findById(anyLong());
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
    }

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 실패, 존재하지 않는 Card Id")
    void scrapSelectedCardsToWorkbookFailedWhenNotExistCardId() {
        // given
        ScrapCardRequest scrapCardRequest =
                ScrapCardRequest.builder()
                        .cardIds(List.of(100L))
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
        given(cardRepository.findById(anyLong())).willThrow(CardNotFoundException.class);

        // when
        assertThatCode(() -> workbookService.scrapSelectedCardsToWorkbook(1L, scrapCardRequest, appUser))
                .isInstanceOf(CardNotFoundException.class);

        // then
        then(userRepository).should(times(1))
                .findById(appUser.getId());
        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(cardRepository).should(times(1))
                .findById(anyLong());
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
    }
}