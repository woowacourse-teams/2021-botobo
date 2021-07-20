package botobo.core.admin.application;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.admin.dto.AdminWorkbookResponse;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public AdminService(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public AdminWorkbookResponse createWorkbook(AdminWorkbookRequest adminWorkbookRequest) {
        Workbook workbook = adminWorkbookRequest.toWorkbook();
        Workbook savedWorkbook = workbookRepository.save(workbook);
        return AdminWorkbookResponse.of(savedWorkbook);
    }

    @Transactional
    public AdminCardResponse createCard(AdminCardRequest adminCardRequest) {
        Long workbookId = adminCardRequest.getWorkbookId();
        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);

        Card card = Card.builder()
                .question(adminCardRequest.getQuestion())
                .answer(adminCardRequest.getAnswer())
                .workbook(workbook)
                .build();
        Card savedCard = cardRepository.save(card);
        return AdminCardResponse.of(savedCard);
    }
}
