package botobo.core.application;

import botobo.core.domain.tag.FilterTags;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagCacheRepository;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("태그 캐시 서비스 테스트")
@ActiveProfiles("test")
@SpringBootTest
public class FilterTagServiceTest {

    @MockBean
    private TagSearchRepository tagSearchRepository;

    @MockBean
    private TagCacheRepository tagCacheRepository;

    @Autowired
    private TagService tagService;

    @DisplayName("문제집명이 포함된 문제집의 모든 태그를 가져온다. - 성공, 캐싱되었으면 캐싱된 결과를 응답한다.")
    @Test
    void findAllTagsByWorkbookNameKorCache() {
        FilterCriteria filterCriteria = new FilterCriteria("문제집");
        given(tagCacheRepository.findById(filterCriteria.getWorkbook()))
                .willReturn(Optional.of(FilterTags.of(filterCriteria.getWorkbook(),
                        List.of(
                                TagResponse.of(Tag.of(TagName.of("java"))),
                                TagResponse.of(Tag.of(TagName.of("자바")))
                        )))
                );

        // when - then
        List<TagResponse> tagResponses = tagService.findAllTagsByWorkbookName(filterCriteria);

        assertThat(tagResponses).extracting("name")
                .containsExactly("java", "자바");
        assertThat(tagResponses).hasSize(2);
        then(tagSearchRepository).should(never())
                .findAllByContainsWorkbookName(filterCriteria.getWorkbook());
    }
}
