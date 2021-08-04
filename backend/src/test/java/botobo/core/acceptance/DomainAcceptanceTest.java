package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
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
    private CardRepository cardRepository;

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

    protected User anyUser() {
        User anyUser = User.builder()
                .githubId(1L)
                .userName("joanne")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        return userRepository.save(anyUser);
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

    // TODO 카드 문서화 테스트 추가 이슈에서 카드 테스트 리팩토링 전체까지 진행할 예정!
    public CardResponse 카드_등록되어_있음(String question, String answer, Long workbookId, Long userId) {
        CardRequest cardRequest = CardRequest.builder()
                .question(question)
                .answer(answer)
                .workbookId(workbookId)
                .build();
        return 카드_등록되어있음(cardRequest, userId);
    }

    private CardResponse 카드_등록되어있음(CardRequest cardRequest, Long userId) {
        return 카드_생성_요청(cardRequest, userId).convertBody(CardResponse.class);
    }

    private RequestBuilder.HttpResponse 카드_생성_요청(CardRequest cardRequest, Long userId) {
        return request()
                .post("/api/cards", cardRequest)
                .auth(createToken(userId))
                .build();
    }

    protected WorkbookCardResponse 문제집의_카드_모아보기(Long workbookId) {
        RequestBuilder.HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", workbookId)
                .auth()
                .build();
        return response.convertBody(WorkbookCardResponse.class);
    }

    protected String createToken(Long id) {
        return jwtTokenProvider.createToken(id);
    }
}
