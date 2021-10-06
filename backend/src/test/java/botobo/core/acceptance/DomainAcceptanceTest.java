package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.infrastructure.auth.GithubOauthManager;
import botobo.core.infrastructure.auth.GoogleOauthManager;
import botobo.core.infrastructure.auth.OauthManagerFactory;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
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

    protected User admin, admin1, admin2, admin3;

    protected static final List<User> ADMINS = new ArrayList<>();

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
        admin1 = User.builder()
                .socialId("2")
                .userName("user1")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        admin2 = User.builder()
                .socialId("3")
                .userName("user2")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        admin3 = User.builder()
                .socialId("4")
                .userName("user3")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        userRepository.save(admin1);
        userRepository.save(admin2);
        userRepository.save(admin3);

        ADMINS.addAll(List.of(admin1, admin2, admin3));
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

    public ExtractableResponse<Response> 문제집_생성_요청(WorkbookRequest workbookRequest) {
        return request()
                .post("/workbooks", workbookRequest)
                .auth(jwtTokenProvider.createAccessToken(admin.getId()))
                .build()
                .extract();
    }

    public void 여러개_문제집_생성_요청(List<WorkbookRequest> workbookRequests) {
        for (WorkbookRequest workbookRequest : workbookRequests) {
            문제집_생성_요청(workbookRequest);
        }
    }

    public void 서로_다른_유저의_여러개_문제집_생성_요청(List<WorkbookRequest> workbookRequests, List<User> admins) {
        int userSize = admins.size();
        int i = 0;
        for (WorkbookRequest workbookRequest : workbookRequests) {
            문제집_생성_요청(workbookRequest, admins.get(i++ % userSize));
        }
    }

    private void 문제집_생성_요청(WorkbookRequest workbookRequest, User user) {
        request()
                .post("/workbooks", workbookRequest)
                .auth(jwtTokenProvider.createAccessToken(user.getId()))
                .build()
                .extract();
    }

    public void 여러개_카드_생성_요청(List<CardRequest> cardRequests) {
        for (CardRequest cardRequest : cardRequests) {
            request()
                    .post("/cards", cardRequest)
                    .auth(jwtTokenProvider.createAccessToken(admin.getId()))
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

    protected HttpResponse 유저_카드_생성_요청(CardRequest cardRequest, String accessToken) {
        return request()
                .post("/cards", cardRequest)
                .auth(accessToken)
                .build();
    }

    protected void 유저_카드_포함_문제집_등록되어_있음(String name, boolean opened, String accessToken) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .build();
        final WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음(workbookRequest, accessToken);
        유저_카드_등록되어_있음("질문", "답변", workbookResponse.getId(), accessToken);
    }


    protected WorkbookResponse 유저_태그포함_문제집_등록되어_있음(String name, boolean opened, List<TagRequest> tags, String accessToken) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
        return 유저_문제집_등록되어_있음(workbookRequest, accessToken);
    }

    protected void 유저_태그_카드_포함_문제집_등록되어_있음(String name, boolean opened, List<TagRequest> tags, String accessToken) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
        final WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음(workbookRequest, accessToken);
        유저_카드_등록되어_있음("질문", "답변", workbookResponse.getId(), accessToken);
    }

    protected WorkbookResponse 유저_문제집_등록되어_있음(WorkbookRequest workbookRequest, String accessToken) {
        return 유저_문제집_생성_요청(workbookRequest, accessToken).convertBody(WorkbookResponse.class);
    }

    protected HttpResponse 유저_문제집_생성_요청(WorkbookRequest workbookRequest, String accessToken) {
        return request()
                .post("/workbooks", workbookRequest)
                .auth(accessToken)
                .build();
    }

    protected WorkbookResponse 유저_태그_포함_문제집_등록되어_있음(String name, boolean opened, String accessToken) {
        List<TagRequest> tagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).name("자바").build()
        );
        return 유저_태그포함_문제집_등록되어_있음(name, opened, tagRequests, accessToken);
    }

    protected WorkbookCardResponse 문제집의_카드_모아보기(Long workbookId) {
        HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookId)
                .auth(createToken(1L))
                .build();
        return response.convertBody(WorkbookCardResponse.class);
    }

    protected String createToken(Long id) {
        return jwtTokenProvider.createAccessToken(id);
    }

    protected HttpResponse 하트_토글_요청(Long workbookId, String accessToken) {
        return request()
                .putWithoutBody("/workbooks/{workbookId}/hearts", workbookId)
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
                .post("/login/{socialType}", loginRequest, socialType)
                .build()
                .extract();
    }
}
