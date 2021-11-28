package botobo.core.application;

import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookQueryRepository;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchCriteria;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class WorkbookRankService {

    private static final int START = 0;
    private static final int SIZE = 3;

    private final WorkbookQueryRepository workbookQueryRepository;

    public WorkbookRankService(WorkbookQueryRepository workbookQueryRepository) {
        this.workbookQueryRepository = workbookQueryRepository;
    }

    @Cacheable("workbookRanks")
    public List<WorkbookResponse> findWorkbookRanks() {
        WorkbookSearchParameter workbookSearchParameter = WorkbookSearchParameter.of(
                SearchCriteria.HEART,
                SearchKeyword.ofAll(),
                START,
                SIZE
        );
        List<Workbook> workbooks = workbookQueryRepository.searchAll(workbookSearchParameter);
        return WorkbookResponse.openedListOf(workbooks);
    }

    @CacheEvict("workbookRanks")
    public void removeWorkbookRanksCache() {
        log.info("cleared cache for workbook rankings request");
    }
}
