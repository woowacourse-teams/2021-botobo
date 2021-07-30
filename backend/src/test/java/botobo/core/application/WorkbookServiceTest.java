package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
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
    private TagService tagService;

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
                .tags(Tags.from(Arrays.asList(
                        Tag.from("자바"), Tag.from("java")
                )))
                .user(normalUser)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(normalUser));
        given(tagService.convertTags(any())).willReturn(Tags.from(
                Arrays.asList(Tag.from("자바"), Tag.from("java"))
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
        Tags tas = Tags.from(
                Arrays.asList(Tag.from("javi"), Tag.from("잡아"))
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

        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));
        given(tagService.convertTags(any())).willReturn(Tags.from(
                Arrays.asList(Tag.from("java"), Tag.from("자바"), Tag.from("중급"))
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

        then(workbookRepository).should(times(1))
                .findById(anyLong());
        then(tagService).should(times(1))
                .convertTags(any());
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        Tags tas = Tags.from(
                Arrays.asList(Tag.from("java"), Tag.from("자바"))
        );
        Workbook workbook = Workbook.builder()
                .id(1L)
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(tas)
                .user(normalUser)
                .build();

        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        workbookService.deleteWorkbook(normalUser.getId(), normalUser.toAppUser());

        //then
        assertThat(workbook.isDeleted()).isTrue();
        assertThat(workbook.getWorkbookTags()).isEmpty();
        then(workbookRepository).should(times(1))
                .findById(anyLong());
    }
}