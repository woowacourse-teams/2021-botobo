package botobo.core.admin.ui;

import botobo.core.admin.application.AdminService;
import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.admin.dto.AdminCategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/categories")
    public ResponseEntity<AdminCategoryResponse> createCategory(@RequestBody AdminCategoryRequest adminCategoryRequest) {
        AdminCategoryResponse adminCategoryResponse = adminService.createCategory(adminCategoryRequest);
        return ResponseEntity.created(URI.create("/admin/categories/" + adminCategoryResponse.getId())).body(adminCategoryResponse);
    }
}
