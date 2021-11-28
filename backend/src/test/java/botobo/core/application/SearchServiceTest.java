package botobo.core.application;

import botobo.core.application.rank.SearchRankService;
import botobo.core.domain.tag.TagQueryRepository;
import botobo.core.domain.tag.dto.TagDto;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookQueryRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.ui.search.SearchRelated;
import botobo.core.ui.search.WorkbookSearchParameter;
import botobo.core.utils.WorkbookSearchParameterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("검색 서비스 테스트")
@MockitoSettings
class SearchServiceTest {

    @Mock
    private TagQueryRepository tagRepository;

    @Mock
    private WorkbookQueryRepository workbookQueryRepository;

    @Mock
    private SearchRankService searchRankService;

    @InjectMocks
    private SearchService searchService;

    @Nested
    @DisplayName("태그 검색")
    class TagSearch {
        private static final String JAVA = "java";
        private static final String JAVASCRIPT = "javascript";
        private List<TagDto> tags;

        @BeforeEach
        void setUp() {
            tags = List.of(TagDto.of(1L, JAVA), TagDto.of(2L, JAVASCRIPT));
        }

        @Test
        @DisplayName("성공, 이름에 키워드가 들어가면 결과에 포함된다")
        void searchTags() {
            // given
            given(tagRepository.findAllTagContaining(JAVA)).willReturn(tags);

            // when
            List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated(JAVA));

            // then
            then(tagRepository).should(times(1))
                    .findAllTagContaining(JAVA);
            assertThat(tagResponses).extracting("name").containsExactly(JAVA, JAVASCRIPT);
        }

        @Test
        @DisplayName("성공, 태그 검색은 대소문자를 구별하지 않는다")
        void searchTagsWithUpperCaseIncluded() {
            // given
            given(tagRepository.findAllTagContaining(JAVA)).willReturn(tags);

            // when
            List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated("JAVA"));

            // then
            then(tagRepository).should(times(1))
                    .findAllTagContaining(JAVA);
            assertThat(tagResponses).extracting("name").containsExactly(JAVA, JAVASCRIPT);
        }

        @Test
        @DisplayName("성공, 양 쪽 빈칸은 trim 된다.")
        void searchTagsWhenHasEmptySpace() {
            // given
            String keyword = " java ";
            given(tagRepository.findAllTagContaining(JAVA)).willReturn(tags);

            // when
            List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated(keyword));

            // then
            then(tagRepository).should(times(1))
                    .findAllTagContaining(JAVA);
            assertThat(tagResponses).extracting("name").containsExactly(JAVA, JAVASCRIPT);
        }
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 첫 페이지 검색의 경우 해당 키워드의 검색 점수가 올라간다.")
    void searchWorkbooksWithIncreaseScore() {
        // given
        String keyword = "java";
        WorkbookSearchParameter searchParameter = WorkbookSearchParameterUtils.builder().searchKeyword(keyword).build();
        PageRequest pageRequest = searchParameter.toPageRequest();

        Page<Workbook> mockPage = mock(Page.class);
        given(mockPage.toList()).willReturn(Collections.emptyList());
        given(workbookQueryRepository.searchAll(
                any(WorkbookSearchParameter.class), anyList(), anyList(), eq(pageRequest))
        ).willReturn(mockPage);

        // when
        searchService.searchWorkbooks(searchParameter, Collections.emptyList(), Collections.emptyList());

        // then
        then(searchRankService).should(times(1))
                .increaseScore(keyword);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 첫 페이지 검색이 아닌 경우 검색 점수가 올라가지 않는다.")
    void searchWorkbooksWithKeepScore() {
        // given
        String keyword = "java";
        WorkbookSearchParameter searchParameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword(keyword).start("2").build();
        PageRequest pageRequest = searchParameter.toPageRequest();

        Page<Workbook> mockPage = mock(Page.class);
        given(mockPage.toList()).willReturn(Collections.emptyList());
        given(workbookQueryRepository.searchAll(
                any(WorkbookSearchParameter.class), anyList(), anyList(), eq(pageRequest))
        ).willReturn(mockPage);

        // when
        searchService.searchWorkbooks(searchParameter, Collections.emptyList(), Collections.emptyList());

        // then
        then(searchRankService).should(times(0))
                .increaseScore(keyword);
    }
}
