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
import java.util.stream.IntStream;

import static botobo.core.acceptance.utils.Fixture.USER_BEAR;
import static botobo.core.acceptance.utils.Fixture.USER_DITTO;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_KYLE;
import static botobo.core.acceptance.utils.Fixture.USER_OZ;
import static botobo.core.acceptance.utils.Fixture.USER_PK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class DomainAcceptanceTest extends AcceptanceTest {

    @MockBean
    protected GithubOauthManager githubOauthManager;

    @MockBean
    protected GoogleOauthManager googleOauthManager;

    @MockBean
    private OauthManagerFactory oauthManagerFactory;

    @Autowired
    protected UserRepository userRepository;

    protected User admin1, admin2, admin3;

    protected static final List<User> ADMINS = new ArrayList<>();

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
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
        userRepository.save(admin1);
        userRepository.save(admin2);
        userRepository.save(admin3);
        userRepository.save(USER_PK);
        userRepository.save(USER_JOANNE);
        userRepository.save(USER_BEAR);
        userRepository.save(USER_OZ);
        userRepository.save(USER_KYLE);
        userRepository.save(USER_DITTO);

        ADMINS.addAll(List.of(admin1, admin2, admin3));
    }

    public List<Long> 여러개_문제집_생성_요청(List<WorkbookRequest> workbookRequests) {
        List<Long> ids = new ArrayList<>();
        for (WorkbookRequest workbookRequest : workbookRequests) {
            ids.add(생성된_문제집의_ID를_반환(문제집_생성_요청(workbookRequest)));
        }
        return ids;
    }

    private HttpResponse 문제집_생성_요청(WorkbookRequest workbookRequest) {
        return request()
                .post("/workbooks", workbookRequest)
                .auth(createToken(USER_PK.getId()))
                .build();
    }

    private Long 생성된_문제집의_ID를_반환(HttpResponse response) {
        WorkbookResponse workbookResponse = response.convertBody(WorkbookResponse.class);
        return workbookResponse.getId();
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
                .auth(createToken(user.getId()))
                .build()
                .extract();
    }

    public void 여러개_카드_생성_요청(List<CardRequest> cardRequests) {
        for (CardRequest cardRequest : cardRequests) {
            request()
                    .post("/cards", cardRequest)
                    .auth(createToken(USER_PK.getId()))
                    .build()
                    .extract();
        }
    }

    protected WorkbookResponse 유저_태그포함_문제집_등록되어_있음_1(String name, boolean opened, List<TagRequest> tags, User user) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
        return 유저_문제집_등록되어_있음_1(workbookRequest, user);
    }

    protected WorkbookResponse 유저_문제집_등록되어_있음_1(WorkbookRequest workbookRequest, User user) {
        return 유저_문제집_생성_요청_1(workbookRequest, user).convertBody(WorkbookResponse.class);
    }

    protected HttpResponse 유저_문제집_생성_요청_1(WorkbookRequest workbookRequest, User user) {
        return request()
                .post("/workbooks", workbookRequest)
                .auth(createToken(user.getId()))
                .build();
    }

    public CardResponse 유저_카드_등록되어_있음_1(String question, String answer, Long workbookId, User user) {
        CardRequest cardRequest = CardRequest.builder()
                .question(question)
                .answer(answer)
                .workbookId(workbookId)
                .build();
        return 유저_카드_등록되어_있음_1(cardRequest, user);
    }

    private CardResponse 유저_카드_등록되어_있음_1(CardRequest cardRequest, User user) {
        return 유저_카드_생성_요청_1(cardRequest, user).convertBody(CardResponse.class);
    }

    protected HttpResponse 유저_카드_생성_요청_1(CardRequest cardRequest, User user) {
        return request()
                .post("/cards", cardRequest)
                .auth(createToken(user.getId()))
                .build();
    }

    protected void 유저_카드_포함_문제집_등록되어_있음_1(String name, boolean opened, User user) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .build();
        final WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음_1(workbookRequest, user);
        유저_카드_등록되어_있음_1("질문", "답변", workbookResponse.getId(), user);
    }

    protected void 유저_태그_카드_포함_문제집_등록되어_있음_1(String name, boolean opened, List<TagRequest> tags, User user) {
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
        final WorkbookResponse workbookResponse = 유저_문제집_등록되어_있음_1(workbookRequest, user);
        유저_카드_등록되어_있음_1("질문", "답변", workbookResponse.getId(), user);
    }

    protected void 카드와_좋아요도_함께_등록_1(WorkbookResponse workbookResponse, int cardCount, User user, List<User> heartUsers) {
        카드도_함께_등록_12(workbookResponse, cardCount, user);
        좋아요도_함께_등록_12(workbookResponse, heartUsers);
    }

    protected void 카드도_함께_등록_12(WorkbookResponse workbookResponse, int cardCount, User user) {
        Long workbookId = workbookResponse.getId();
        IntStream.rangeClosed(1, cardCount)
                .forEach(number -> 유저_카드_등록되어_있음_1("질문", "정답", workbookId, user));
    }

    protected void 좋아요도_함께_등록_12(WorkbookResponse workbookResponse, List<User> heartUsers) {
        Long workbookId = workbookResponse.getId();
        heartUsers
                .forEach(user -> 하트_토글_요청_12(workbookId, user));
    }

    protected HttpResponse 하트_토글_요청_12(Long workbookId, User user) {
        return request()
                .putWithoutBody("/workbooks/{workbookId}/hearts", workbookId)
                .auth(createToken(user.getId()))
                .build();
    }

    protected void 카드도_함께_등록_1(WorkbookResponse workbookResponse, int cardCount, User user) {
        Long workbookId = workbookResponse.getId();
        IntStream.rangeClosed(1, cardCount)
                .forEach(number -> 유저_카드_등록되어_있음_1("질문", "정답", workbookId, user));
    }

    protected WorkbookResponse 유저_태그_포함_문제집_등록되어_있음_12(String name, boolean opened, User user) {
        List<TagRequest> tagRequests = Collections.singletonList(
                TagRequest.builder().id(1L).name("자바").build()
        );
        return 유저_태그포함_문제집_등록되어_있음_1(name, opened, tagRequests, user);
    }

    protected WorkbookCardResponse 문제집의_카드_모아보기(Long workbookId) {
        HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookId)
                .auth(createToken(USER_PK.getId()))
                .build();
        return response.convertBody(WorkbookCardResponse.class);
    }

    protected String createToken(Long id) {
        return jwtTokenProvider.createAccessToken(id);
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
