package botobo.core.documentation;

import botobo.core.application.AuthService;
import botobo.core.documentation.utils.DocumentRequestBuilder;
import botobo.core.documentation.utils.DocumentRequestBuilder.MockMvcFunction;
import botobo.core.domain.user.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class DocumentationTest {
    private DocumentRequestBuilder documentRequestBuilder;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService authService;

    private final String authenticatedToken = "botobo.access.token";

    @BeforeEach
    void setUp() {
        documentRequestBuilder = new DocumentRequestBuilder();
        AppUser authenticatedUser = AppUser.user(2L);
        given(authService.findAppUserByToken(authenticatedToken)).willReturn(authenticatedUser);
    }

    protected String obtainAuthenticatedToken() {
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
