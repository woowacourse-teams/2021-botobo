package botobo.core.domain.workbook;

import botobo.core.ui.search.WorkbookSearchParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkbookRepositoryCustom {
    Page<Workbook> searchAll(WorkbookSearchParameter parameter, List<Long> tags,
                             List<Long> users, Pageable pageable);
}
