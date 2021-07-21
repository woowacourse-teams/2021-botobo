package botobo.core.quiz.application;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.WorkbookCardResponse;
import botobo.core.quiz.dto.WorkbookResponse;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkbookService {

    private final WorkbookRepository workbookRepository;

    public WorkbookService(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    public List<WorkbookResponse> findAll() {
        List<Workbook> workbooks = workbookRepository.findAll();
        return WorkbookResponse.listOf(workbooks);
    }

    public WorkbookCardResponse findWorkbookCardsById(Long id) {
        Workbook workbook = workbookRepository.findById(id)
                .orElseThrow(WorkbookNotFoundException::new);
        return WorkbookCardResponse.of(workbook);
    }
}
