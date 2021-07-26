package botobo.core.ui;

import botobo.core.application.AdminService;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminCardResponse;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.dto.admin.AdminWorkbookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/workbooks")
    public ResponseEntity<AdminWorkbookResponse> createCategory(@Valid @RequestBody AdminWorkbookRequest adminWorkbookRequest) {
        AdminWorkbookResponse adminWorkbookResponse = adminService.createWorkbook(adminWorkbookRequest);
        return ResponseEntity.created(URI.create("/api/admin/workbooks/" + adminWorkbookResponse.getId())).body(adminWorkbookResponse);
    }

    @PostMapping("/cards")
    public ResponseEntity<AdminCardResponse> createCard(@Valid @RequestBody AdminCardRequest adminCardRequest) {
        AdminCardResponse adminCardResponse = adminService.createCard(adminCardRequest);
        return ResponseEntity.created(URI.create("/api/admin/cards/" + adminCardResponse.getId())).body(adminCardResponse);
    }
}
