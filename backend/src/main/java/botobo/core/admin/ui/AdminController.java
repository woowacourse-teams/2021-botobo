package botobo.core.admin.ui;

import botobo.core.admin.application.AdminService;
import botobo.core.admin.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/categories")
    public ResponseEntity<AdminCategoryResponse> createCategory(@Valid @RequestBody AdminCategoryRequest adminCategoryRequest) {
        AdminCategoryResponse adminCategoryResponse = adminService.createCategory(adminCategoryRequest);
        return ResponseEntity.created(URI.create("/admin/categories/" + adminCategoryResponse.getId())).body(adminCategoryResponse);
    }

    @PostMapping("/cards")
    public ResponseEntity<AdminCardResponse> createCard(@Valid @RequestBody AdminCardRequest adminCardRequest) {
        AdminCardResponse adminCardResponse = adminService.createCard(adminCardRequest);
        return ResponseEntity.created(URI.create("/admin/cards/" + adminCardResponse.getId())).body(adminCardResponse);
    }

    @PostMapping("/answers")
    public ResponseEntity<AdminAnswerResponse> createAnswer(@Valid @RequestBody AdminAnswerRequest adminAnswerRequest) {
        AdminAnswerResponse adminAnswerResponse = adminService.createAnswer(adminAnswerRequest);
        return ResponseEntity.created(URI.create("/admin/answers/" + adminAnswerResponse.getId())).body(adminAnswerResponse);
    }
}
