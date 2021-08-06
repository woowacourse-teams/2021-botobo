package botobo.core.application;

import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.User;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchKeyword;
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
    private final TagRepository tagRepository;

    public SearchService(WorkbookRepository workbookRepository, TagRepository tagRepository) {
        this.workbookRepository = workbookRepository;
        this.tagRepository = tagRepository;
    }

    public List<WorkbookResponse> searchWorkbooks(WorkbookSearchParameter workbookSearchParameter) {
        List<Workbook> workbooks = workbookRepository.findAll();
        return WorkbookResponse.openedListOf(workbooks);
    }

    public List<TagResponse> searchTags(SearchKeyword keyword) {
        Tags tags = Tags.of(tagRepository.findByKeyword(keyword.getValue()));
        return TagResponse.listOf(tags);
    }

    public List<UserResponse> searchUsers() {
        return null;
    }
}
