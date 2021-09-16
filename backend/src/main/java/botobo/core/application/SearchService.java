package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookSearchRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchRelated;
import botobo.core.ui.search.WorkbookSearchParameter;
import botobo.core.util.SimilarityChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchService {

    private static final int SIZE_LIMIT = 10;

    private final WorkbookSearchRepository workbookSearchRepository;
    private final TagRepository tagRepository;

    public SearchService(WorkbookSearchRepository workbookSearchRepository, TagRepository tagRepository) {
        this.workbookSearchRepository = workbookSearchRepository;
        this.tagRepository = tagRepository;
    }

    public List<WorkbookResponse> searchWorkbooks(WorkbookSearchParameter workbookSearchParameter,
                                                  List<Long> tags,
                                                  List<Long> users) {
        PageRequest pageRequest = workbookSearchParameter.toPageRequest();

        Page<Workbook> page = workbookSearchRepository.searchAll(workbookSearchParameter, tags, users, pageRequest);
        List<Workbook> workbooks = page.toList();
        return WorkbookResponse.openedListOf(workbooks);
    }

    public List<TagResponse> findTagsIn(SearchRelated keyword) {
        String target = keyword.get();
        if (target.isBlank()) {
            return TagResponse.listOf(Tags.empty());
        }
        List<Tag> tags = tagRepository.findAllTagContaining(target);
        List<Tag> sortedTags = SimilarityChecker.orderBySimilarity(target, tags, SIZE_LIMIT);
        return TagResponse.listOf(Tags.of(sortedTags));
    }
}
