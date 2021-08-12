package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.user.SimpleUserResponse;
import botobo.core.ui.search.SearchKeyword;
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
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("태그 검색 - 성공, 이름에 키워드가 들어가면 결과에 포함된다")
    void searchTags() {
        // given
        String java = "java";
        String javascript = "javascript";
        List<Tag> tags = List.of(Tag.of(java), Tag.of(javascript));
        given(tagRepository.findByKeyword(java)).willReturn(tags);

        // when
        SearchKeyword searchKeyword = SearchKeyword.of(java);
        List<TagResponse> tagResponses = searchService.searchTags(searchKeyword);

        // then
        then(tagRepository).should(times(1))
                .findByKeyword(searchKeyword.toLowercase());
        assertThat(tagResponses).extracting("name").containsExactly(java, javascript);
    }

    @Test
    @DisplayName("대문자로 태그 검색 - 성공, 태그 검색은 대소문자를 구별하지 않는다")
    void searchTagsWithUpperCaseIncluded() {
        // given
        String java = "java";
        String javascript = "javascript";
        List<Tag> tags = List.of(Tag.of(java), Tag.of(javascript));
        given(tagRepository.findByKeyword(java)).willReturn(tags);

        // when
        SearchKeyword searchKeyword = SearchKeyword.of(java.toUpperCase());
        List<TagResponse> tagResponses = searchService.searchTags(searchKeyword);

        // then
        then(tagRepository).should(times(1))
                .findByKeyword(searchKeyword.toLowercase());
        assertThat(tagResponses).extracting("name").containsExactly(java, javascript);
    }

    @Test
    @DisplayName("유저 검색 - 성공, 유저 검색은 대소문자를 구별하고, 이름에 키워드가 들어가면 결과에 포함된다")
    void searchUsers() {
        // given
        String sam = "sam";
        String samantha = "samantha";
        List<User> users = List.of(user(sam), user(samantha));
        given(userRepository.findByKeyword(sam)).willReturn(users);

        // when
        SearchKeyword searchKeyword = SearchKeyword.of(sam);
        List<SimpleUserResponse> simpleUserResponses = searchService.searchUsers(searchKeyword);

        // then
        then(userRepository).should(times(1))
                .findByKeyword(sam);
        assertThat(simpleUserResponses).extracting("name").containsExactly(sam, samantha);
    }

    private User user(String name) {
        return User.builder()
                .userName(name)
                .build();
    }
}