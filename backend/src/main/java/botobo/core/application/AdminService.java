package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminCardResponse;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.dto.admin.AdminWorkbookResponse;
import botobo.core.exception.workbook.WorkbookNotFoundException;
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
