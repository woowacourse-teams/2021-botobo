package botobo.core.quiz.application;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import botobo.core.user.domain.AppUser;
import botobo.core.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("문제집 서비스 테스트")
@MockitoSettings
class WorkbookServiceTest {

    @Mock
    private WorkbookRepository workbookRepository;

    @InjectMocks
    private WorkbookService workbookService;

    private User normalUser;

    private List<Workbook> workbooks;

    @BeforeEach
    void setUp() {
        normalUser = User.builder().id(2L).userName("ggyool").build();

        workbooks = Arrays.asList(
                Workbook.builder().id(1L).name("데이터베이스").isPublic(true).user(normalUser).build(),
                Workbook.builder().id(2L).name("자바").isPublic(true).user(normalUser).build(),
                Workbook.builder().id(3L).name("자바스크립트").isPublic(true).user(normalUser).build(),
                Workbook.builder().id(4L).name("네트워크").isPublic(true).user(normalUser).build()
        );
    }

    @Test
    @DisplayName("일반 유저 문제집 전체 조회 - 성공")
    void findWorkbooksByUser() {
        // given
        given(workbookRepository.findAllByUserId(normalUser.getId())).willReturn(workbooks);

        // when
        List<WorkbookResponse> workbooks = workbookService.findWorkbooksByUser(normalUser.toAppUser());

        // then
        assertThat(workbooks).hasSize(4);

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
}