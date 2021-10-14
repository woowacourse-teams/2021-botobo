package botobo.core.application;

import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookSearchRepository;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchCriteria;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkbookRankService {

    private static final int INDEX = 0;
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
                INDEX,
                SIZE
        );
        Page<Workbook> workbookPages = workbookSearchRepository.searchAll(
                workbookSearchParameter,
                Collections.emptyList(),
                Collections.emptyList(),
                workbookSearchParameter.toPageRequest()
        );
        List<Workbook> workbooks = workbookPages.getContent();
        return WorkbookResponse.openedListOf(workbooks);
    }
}
