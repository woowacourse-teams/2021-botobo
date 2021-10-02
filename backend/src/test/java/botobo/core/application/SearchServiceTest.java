package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.ui.search.SearchRelated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("검색 서비스 테스트")
@MockitoSettings
class SearchServiceTest {

    @Mock
    private TagSearchRepository tagRepository;

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
}
