package botobo.core.application;

import botobo.core.application.rank.SearchRankService;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.tag.dto.TagDto;
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
    private final TagSearchRepository tagSearchRepository;
    private final SearchRankService searchRankService;

    public SearchService(WorkbookSearchRepository workbookSearchRepository, TagSearchRepository tagSearchRepository,
                         SearchRankService searchRankService) {

        this.workbookSearchRepository = workbookSearchRepository;
        this.tagSearchRepository = tagSearchRepository;
        this.searchRankService = searchRankService;
    }

    @Transactional
    public List<WorkbookResponse> searchWorkbooks(WorkbookSearchParameter workbookSearchParameter,
                                                  List<Long> tags,
                                                  List<Long> users) {

        PageRequest pageRequest = workbookSearchParameter.toPageRequest();
        if (pageRequest.getPageNumber() == 0) {
            String searchKeywordValue = workbookSearchParameter.getSearchKeyword().getValue();
            searchRankService.increaseScore(searchKeywordValue);
        }
        Page<Workbook> page = workbookSearchRepository.searchAll(workbookSearchParameter, tags, users, pageRequest);
        List<Workbook> workbooks = page.toList();
        return WorkbookResponse.openedListOf(workbooks);
    }

    public List<TagResponse> findTagsIn(SearchRelated keyword) {
        String target = keyword.get();
        if (target.isBlank()) {
            return TagResponse.listOf(Tags.empty());
        }
        List<TagDto> tags = tagSearchRepository.findAllTagContaining(target);
        List<TagDto> sortedTags = SimilarityChecker.orderBySimilarity(target, tags, SIZE_LIMIT);
        return TagResponse.listOf(sortedTags);
    }

}
