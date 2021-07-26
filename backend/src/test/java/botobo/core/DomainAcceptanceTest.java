package botobo.core;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.user.domain.Role;
import botobo.core.user.domain.User;
import botobo.core.user.domain.UserRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DomainAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User user;

    @BeforeEach
    void setUser() {
        user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("admin")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
    }

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest) {
        return request()
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build()
                .extract();
    }

    public void 여러개_문제집_생성_요청(List<AdminWorkbookRequest> adminRequests) {
        for (AdminWorkbookRequest adminRequest : adminRequests) {
            문제집_생성_요청(adminRequest);
        }
    }

    public void 여러개_카드_생성_요청(List<AdminCardRequest> adminCardRequests) {
        for (AdminCardRequest adminCardRequest : adminCardRequests) {
            request()
                    .post("/api/admin/cards", adminCardRequest)
                    .auth(jwtTokenProvider.createToken(user.getId()))
                    .build()
                    .extract();
        }
    }
}
