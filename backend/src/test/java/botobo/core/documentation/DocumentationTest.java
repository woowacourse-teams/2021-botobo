package botobo.core.documentation;

import botobo.core.application.AuthService;
import botobo.core.documentation.utils.DocumentRequestBuilder;
import botobo.core.documentation.utils.DocumentRequestBuilder.MockMvcFunction;
import botobo.core.domain.user.AppUser;
import botobo.core.infrastructure.auth.JwtRefreshTokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class DocumentationTest {

    private DocumentRequestBuilder documentRequestBuilder;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected JwtRefreshTokenInfo jwtRefreshTokenInfo;

    private final AppUser authenticatedUser = AppUser.user(1L);
    private final String authenticatedToken = "botobo.access.token";

    @BeforeEach
    void setUp() {
        documentRequestBuilder = new DocumentRequestBuilder();
        given(authService.findAppUserByToken(null)).willReturn(AppUser.anonymous());
        given(authService.findAppUserByToken(authenticatedToken)).willReturn(authenticatedUser);
        given(authService.isValidAccessToken(authenticatedToken)).willReturn(true);
    }

    protected AppUser authenticatedUser() {
        return this.authenticatedUser;
    }

    protected String authenticatedToken() {
        return this.authenticatedToken;
    }

    /**
     * How to use
     * document()
     * .mockMvc(mockMvc)
     * .get(path)        HttpMethod
     * .auth(token)      default false
     * .locationHeader(Location URL) default false
     * .build()          necessary!!!!!!!
     * .addStatusAndIdentifier(status().isOk(), "workbooks-get-success"); necessary!!!
     */
    protected MockMvcFunction document() {
        return documentRequestBuilder.build();
    }
}
