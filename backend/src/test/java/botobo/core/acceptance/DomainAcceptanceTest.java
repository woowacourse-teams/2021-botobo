package botobo.core.acceptance;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.infrastructure.JwtTokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DomainAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected User user;

    @BeforeEach
    void setUser() {
        user = User.builder()
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
