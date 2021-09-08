package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.infrastructure.GithubOauthManager;
import botobo.core.infrastructure.GoogleOauthManager;
import botobo.core.infrastructure.OauthManagerFactory;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class DomainAcceptanceTest extends AcceptanceTest {

    @MockBean
    protected GithubOauthManager githubOauthManager;

    @MockBean
    protected GoogleOauthManager googleOauthManager;

    @Autowired
    protected UserRepository userRepository;

    @MockBean
    private OauthManagerFactory oauthManagerFactory;

    protected User admin, user1, user2, user3;

    protected List<User> users;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        admin = User.builder()
                .socialId("1")
                .userName("admin")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        user1 = User.builder()
                .socialId("2")
                .userName("user1")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        user2 = User.builder()
                .socialId("3")
                .userName("user2")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        user3 = User.builder()
                .socialId("4")
                .userName("user3")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        users = List.of(user1, user2, user3);
    }

    protected User anyUser() {
        User anyUser = User.builder()
                .socialId("2")
                .userName("joanne")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        return userRepository.save(anyUser);
    }

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest) {
        return request()
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth(jwtTokenProvider.createToken(admin.getId()))
                .build()
                .extract();
    }

    public ExtractableResponse<Response> 서로_다른_유저의_문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest, User user) {
        return request()
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build()
                .extract();
    }

    public void 서로_다른_유저의_여러개_문제집_생성_요청(List<AdminWorkbookRequest> adminRequests) {
        int index = 0;
        for (AdminWorkbookRequest adminRequest : adminRequests) {
            if (index > users.size()) index = 0;
            서로_다른_유저의_문제집_생성_요청(adminRequest, users.get(index++));
        }
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
                    .auth(jwtTokenProvider.createToken(admin.getId()))
                    .build()
                    .extract();
        }
    }

    public CardResponse 유저_카드_등록되어_있음(String question, String answer, Long workbookId, String accessToken) {
        CardRequest cardRequest = CardRequest.builder()
                .question(question)
                .answer(answer)
                .workbookId(workbookId)
                .build();
        return 유저_카드_등록되어_있음(cardRequest, accessToken);
    }

    private CardResponse 유저_카드_등록되어_있음(CardRequest cardRequest, String accessToken) {
        return 유저_카드_생성_요청(cardRequest, accessToken).convertBody(CardResponse.class);
    }

    public HttpResponse 유저_카드_생성_요청(CardRequest cardRequest, String accessToken) {
        return request()
                .post("/api/cards", cardRequest)
                .auth(accessToken)
                .build();
    }

    protected WorkbookResponse 유저_문제집_등록되어_있음(String name, boolean opened, List<TagRequest> tags, String accessToken) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
        return 유저_문제집_등록되어_있음(workbookRequest, accessToken);
    }

    protected WorkbookResponse 유저_문제집_등록되어_있음(WorkbookRequest workbookRequest, String accessToken) {
        return 유저_문제집_생성_요청(workbookRequest, accessToken).convertBody(WorkbookResponse.class);
    }

    protected HttpResponse 유저_문제집_생성_요청(WorkbookRequest workbookRequest, String accessToken) {
        return request()
                .post("/api/workbooks", workbookRequest)
                .auth(accessToken)
                .build();
    }

    protected WorkbookResponse 유저_태그_포함_문제집_등록되어_있음(String name, boolean opened, String accessToken) {
        List<TagRequest> tagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).name("자바").build()
        );
        return 유저_문제집_등록되어_있음(name, opened, tagRequests, accessToken);
    }

    protected WorkbookCardResponse 문제집의_카드_모아보기(Long workbookId) {
        HttpResponse response = request()
                .get("/api/workbooks/{id}/cards", workbookId)
                .auth(createToken(1L))
                .build();
        return response.convertBody(WorkbookCardResponse.class);
    }

    protected String createToken(Long id) {
        return jwtTokenProvider.createToken(id);
    }

    protected HttpResponse 하트_토글_요청(Long workbookId, String accessToken) {
        return request()
                .putWithoutBody("/api/workbooks/{workbookId}/hearts", workbookId)
                .auth(accessToken)
                .build();
    }

    protected String 소셜_로그인되어_있음(UserInfoResponse userInfo, SocialType socialType) {
        ExtractableResponse<Response> response = 소셜_로그인_요청(userInfo, socialType);
        return response.as(TokenResponse.class).getAccessToken();
    }

    protected ExtractableResponse<Response> 소셜_로그인_요청(UserInfoResponse userInfo, SocialType socialType) {
        LoginRequest loginRequest = new LoginRequest("code");

        if (socialType == SocialType.GITHUB) {
            given(oauthManagerFactory.findOauthMangerBySocialType(socialType)).willReturn(githubOauthManager);
            given(githubOauthManager.getUserInfo(any())).willReturn(userInfo.toUser());
        } else {
            given(oauthManagerFactory.findOauthMangerBySocialType(socialType)).willReturn(googleOauthManager);
            given(googleOauthManager.getUserInfo(any())).willReturn(userInfo.toUser());
        }

        return request()
                .post("/api/login/{socialType}", loginRequest, socialType)
                .build()
                .extract();
    }
}
