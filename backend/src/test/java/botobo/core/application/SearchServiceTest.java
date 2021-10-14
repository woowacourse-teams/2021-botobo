package botobo.core.application;

import botobo.core.domain.rank.SearchScoreRepository;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookSearchRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.ui.search.SearchRelated;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.junit.jupiter.api.DisplayName;
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
    private TagSearchRepository tagRepository;

    @Mock
    private WorkbookSearchRepository workbookSearchRepository;

    @Mock
    private SearchScoreRepository searchScoreRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("태그 검색 - 성공, 이름에 키워드가 들어가면 결과에 포함된다")
    void searchTags() {
        // given
        String java = "java";
        String javascript = "javascript";
        List<Tag> tags = List.of(Tag.of(java), Tag.of(javascript));
        given(tagRepository.findAllTagContaining(java)).willReturn(tags);

        // when
        List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated(java));

        // then
        then(tagRepository).should(times(1))
                .findAllTagContaining(java);
        assertThat(tagResponses).extracting("name").containsExactly(java, javascript);
    }

    @Test
    @DisplayName("대문자로 태그 검색 - 성공, 태그 검색은 대소문자를 구별하지 않는다")
    void searchTagsWithUpperCaseIncluded() {
        // given
        String java = "java";
        String javascript = "javascript";
        List<Tag> tags = List.of(Tag.of(java), Tag.of(javascript));
        given(tagRepository.findAllTagContaining(java)).willReturn(tags);

        // when
        List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated("JAVA"));

        // then
        then(tagRepository).should(times(1))
                .findAllTagContaining(java);
        assertThat(tagResponses).extracting("name").containsExactly(java, javascript);
    }

    @Test
    @DisplayName("태그 검색 - 성공, 양 쪽 빈칸은 trim 된다.")
    void searchTagsWhenHasEmptySpace() {
        // given
        String keyword = " java ";
        String java = "java";
        String javascript = "javascript";
        List<Tag> tags = List.of(Tag.of(java), Tag.of(javascript));
        given(tagRepository.findAllTagContaining(java)).willReturn(tags);

        // when
        List<TagResponse> tagResponses = searchService.findTagsIn(new SearchRelated(keyword));

        // then
        then(tagRepository).should(times(1))
                .findAllTagContaining(java);
        assertThat(tagResponses).extracting("name").containsExactly(java, javascript);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 첫 페이지 검색의 경우 해당 키워드의 검색 점수가 올라간다.")
    void searchWorkbooksWithIncreaseScore() {
        // given
        String keyword = "java";
        WorkbookSearchParameter searchParameter = WorkbookSearchParameter.builder().searchKeyword(keyword).build();
        PageRequest pageRequest = searchParameter.toPageRequest();

        Page<Workbook> mockPage = mock(Page.class);
        given(mockPage.toList()).willReturn(Collections.emptyList());
        given(workbookSearchRepository.searchAll(
                any(WorkbookSearchParameter.class), anyList(), anyList(), eq(pageRequest))
        ).willReturn(mockPage);

        // when
        searchService.searchWorkbooks(searchParameter, Collections.emptyList(), Collections.emptyList());

        // then
        then(searchScoreRepository).should(times(1))
                .increaseScore(keyword);
    }

    @Test
    @DisplayName("문제집 검색 - 성공, 첫 페이지 검색이 아닌 경우 검색 점수가 올라가지 않는다.")
    void searchWorkbooksWithKeepScore() {
        // given
        String keyword = "java";
        WorkbookSearchParameter searchParameter = WorkbookSearchParameter.builder()
                .searchKeyword(keyword).start("2").build();
        PageRequest pageRequest = searchParameter.toPageRequest();

        Page<Workbook> mockPage = mock(Page.class);
        given(mockPage.toList()).willReturn(Collections.emptyList());
        given(workbookSearchRepository.searchAll(
                any(WorkbookSearchParameter.class), anyList(), anyList(), eq(pageRequest))
        ).willReturn(mockPage);

        // when
        searchService.searchWorkbooks(searchParameter, Collections.emptyList(), Collections.emptyList());

        // then
        then(searchScoreRepository).should(times(0))
                .increaseScore(keyword);
    }
}
