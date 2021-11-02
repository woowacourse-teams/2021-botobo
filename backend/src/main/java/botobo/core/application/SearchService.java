package botobo.core.application;

import botobo.core.application.rank.SearchRankService;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.tag.dto.TagDto;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookDocument;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.domain.workbook.WorkbookSearchRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchRelated;
import botobo.core.ui.search.WorkbookSearchParameter;
import botobo.core.util.SimilarityChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SearchService {

    private static final int SIZE_LIMIT = 10;

    private final WorkbookSearchRepository workbookSearchRepository;
    private final TagSearchRepository tagSearchRepository;
    private final SearchRankService searchRankService;

    // TODO
    // 엘라스틱서치 도입 후 사용할 서비스
    private final WorkbookDocumentService workbookDocumentService;
    private final WorkbookRepository workbookRepository;

    public SearchService(WorkbookSearchRepository workbookSearchRepository, TagSearchRepository tagSearchRepository, SearchRankService searchRankService, WorkbookDocumentService workbookDocumentService, WorkbookRepository workbookRepository) {
        this.workbookSearchRepository = workbookSearchRepository;
        this.tagSearchRepository = tagSearchRepository;
        this.searchRankService = searchRankService;
        this.workbookDocumentService = workbookDocumentService;
        this.workbookRepository = workbookRepository;
    }

    // TODO
    // 현재 searchWorkbooks에서 모든 필터 검색을 담당하고 있음.
    // 검색창에 들어오는 키워드 검색만 es를 통해 한다고 가정
    @Transactional
    public List<WorkbookResponse> search(WorkbookSearchParameter workbookSearchParameter) {

        Pageable pageable = workbookSearchParameter.toPageRequest();
        List<Long> workbookIds = workbookDocumentService.search(workbookSearchParameter, pageable);
        List<Workbook> workbooks = workbookRepository.findAllById(workbookIds);
        return WorkbookResponse.openedListOf(workbooks);
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
