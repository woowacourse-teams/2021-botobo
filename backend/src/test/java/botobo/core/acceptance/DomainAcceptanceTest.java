package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.tag.TagRequest;
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
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_CARD_REQUEST;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_WORKBOOK_REQUEST;
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

    protected static final List<User> USERS = new ArrayList<>();

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        userRepository.save(USER_PK);
        userRepository.save(USER_JOANNE);
        userRepository.save(USER_BEAR);
        userRepository.save(USER_OZ);
        userRepository.save(USER_KYLE);
        userRepository.save(USER_DITTO);
        USERS.addAll(List.of(USER_PK, USER_BEAR, USER_OZ));
    }

    protected String CREATE_TOKEN(Long id) {
        return jwtTokenProvider.createAccessToken(id);
    }

    protected HttpResponse CREATE_WORKBOOK(WorkbookRequest workbookRequest, User user) {
        return request()
                .post("/workbooks", workbookRequest)
                .auth(CREATE_TOKEN(user.getId()))
                .build();
    }

    protected List<Long> CREATE_WORKBOOKS(List<WorkbookRequest> workbookRequests, User user) {
        List<Long> ids = new ArrayList<>();
        for (WorkbookRequest workbookRequest : workbookRequests) {
            ids.add(CREATE_WORKBOOK(workbookRequest, user)
                    .convertBody(WorkbookResponse.class)
                    .getId());
        }
        return ids;
    }

    protected WorkbookResponse CREATE_WORKBOOK_WITH_PARAMS(String name, boolean opened, List<TagRequest> tags, User user) {
        WorkbookRequest workbookRequest = MAKE_SINGLE_WORKBOOK_REQUEST(name, opened, tags);
        return CREATE_WORKBOOK(workbookRequest, user).convertBody(WorkbookResponse.class);
    }

    protected void CREATE_WORKBOOKS_OF_USERS(List<WorkbookRequest> workbookRequests, List<User> users) {
        int userSize = users.size();
        int i = 0;
        for (WorkbookRequest workbookRequest : workbookRequests) {
            CREATE_WORKBOOK(workbookRequest, users.get(i++ % userSize));
        }
    }

    protected HttpResponse CREATE_CARD(CardRequest cardRequest, User user) {
        return request()
                .post("/cards", cardRequest)
                .auth(CREATE_TOKEN(user.getId()))
                .build();
    }

    protected void CREATE_CARDS(List<CardRequest> cardRequests, User user) {
        for (CardRequest cardRequest : cardRequests) {
            CREATE_CARD(cardRequest, user);
        }
    }

    protected CardResponse CREATE_CARD_WITH_PARAMS(String question, String answer, Long workbookId, User user) {
        CardRequest cardRequest = MAKE_SINGLE_CARD_REQUEST(question, answer, workbookId);
        return CREATE_CARD(cardRequest, user).convertBody(CardResponse.class);
    }

    protected void CREATE_WORKBOOK_INCLUDE_CARD(String name, boolean opened, List<TagRequest> tagRequests, User user, String question, String answer) {
        Long workbookResponseId = CREATE_WORKBOOK(MAKE_SINGLE_WORKBOOK_REQUEST(name, opened, tagRequests), user)
                .convertBody(WorkbookResponse.class).
                getId();
        CREATE_CARD_WITH_PARAMS(question, answer, workbookResponseId, user);
    }

    protected void CREATE_WORKBOOK_WITH_CARD_AND_HEARTS(WorkbookResponse workbookResponse,
                                                        int cardCount,
                                                        String question,
                                                        String answer,
                                                        User user,
                                                        List<User> heartUsers
    ) {
        for (int i = 1; i <= cardCount; i++) {
            CREATE_CARD(MAKE_SINGLE_CARD_REQUEST(question, answer, workbookResponse.getId()), user);
        }
        for (User heartUser : heartUsers) {
            TOGGLE_HEARTS(workbookResponse.getId(), heartUser);
        }
    }

    protected HttpResponse TOGGLE_HEARTS(Long workbookId, User user) {
        return request()
                .putWithoutBody("/workbooks/{workbookId}/hearts", workbookId)
                .auth(CREATE_TOKEN(user.getId()))
                .build();
    }

    protected HttpResponse SEARCH_WORKBOOK(Map<String, Object> parameters) {
        return request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();
    }

    protected String RETURN_ACCESS_TOKEN_VIA_LOGIN(UserInfoResponse userInfo, SocialType socialType) {
        ExtractableResponse<Response> response = REQUEST_LOGIN(userInfo, socialType);
        return response.as(TokenResponse.class).getAccessToken();
    }

    protected ExtractableResponse<Response> REQUEST_LOGIN(UserInfoResponse userInfo, SocialType socialType) {
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
