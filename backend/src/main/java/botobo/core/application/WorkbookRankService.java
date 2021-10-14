package botobo.core.application;

import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookSearchRepository;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchCriteria;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkbookRankService {

    private static final Logger log = LoggerFactory.getLogger(WorkbookRankService.class);

    private static final int START = 0;
    private static final int SIZE = 3;

    private final WorkbookSearchRepository workbookSearchRepository;

    public WorkbookRankService(WorkbookSearchRepository workbookSearchRepository) {
        this.workbookSearchRepository = workbookSearchRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable("workbookRanks")
    public List<WorkbookResponse> findWorkbookRanks() {
        WorkbookSearchParameter workbookSearchParameter = WorkbookSearchParameter.of(
                SearchCriteria.HEART,
                SearchKeyword.ofAll(),
                START,
                SIZE
        );
        List<Workbook> workbooks = workbookSearchRepository.searchAll(workbookSearchParameter);
        return WorkbookResponse.openedListOf(workbooks);
    }

    @CacheEvict("workbookRanks")
    public void removeWorkbookRanksCache() {
        log.info("cleared cache for workbook rankings request");
    }
}
