package botobo.core.application;

import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchService {

    private final WorkbookRepository workbookRepository;

    public SearchService(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    public List<WorkbookResponse> searchWorkbooks(WorkbookSearchParameter workbookSearchParameter) {
        List<Workbook> workbooks = workbookRepository.findAll();
        return WorkbookResponse.openedListOf(workbooks);
    }

    public List<TagResponse> searchTags() {
        return null;
    }

    public List<UserResponse> searchUsers() {
        return null;
    }
}
