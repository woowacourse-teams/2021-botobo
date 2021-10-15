package botobo.core.application;

import botobo.core.application.rank.SearchRankService;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagSearchRepository;
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
    private final TagSearchRepository tagSearchRepository;
    private final SearchRankService searchRankService;

    public SearchService(WorkbookSearchRepository workbookSearchRepository, TagSearchRepository tagSearchRepository,
                         SearchRankService searchRankService) {

        this.workbookSearchRepository = workbookSearchRepository;
        this.tagSearchRepository = tagSearchRepository;
        this.searchRankService = searchRankService;
    }

    // searchRankService.increaseScore 때문에 readonly false로 바꿔주었음.
    // 메서드 실행 중 예외가 발생하면 키워드 점수가 올라가지 않는 것도 로컬에서 확인하였음.

    // 검색 메서드가 SLAVE 디비를 사용하지 않는 것이 상당히 안타까워
    // 이런 저런 시도를 해봤지만 방법을 찾지 못하였음
    // 그럴싸하다고 생각했던 방법 중
    // 1. 이 메서드는 readonly = true 로 하고
    // 2. searchRankService.increaseScore 를 propagation Requires-new 로 실행
    // 될 줄 알았는데 안 됨. 혹시 방법 아는 분 알려주세요.
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
        List<Tag> tags = tagSearchRepository.findAllTagContaining(target);
        List<Tag> sortedTags = SimilarityChecker.orderBySimilarity(target, tags, SIZE_LIMIT);
        return TagResponse.listOf(Tags.of(sortedTags));
    }
}
