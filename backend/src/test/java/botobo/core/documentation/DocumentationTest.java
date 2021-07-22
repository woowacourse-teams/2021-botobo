package botobo.core.documentation;

import botobo.core.utils.DocumentRequestBuilder;
import botobo.core.utils.DocumentRequestBuilder.MockMvcFunction;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class DocumentationTest {
    private DocumentRequestBuilder documentRequestBuilder;

    @BeforeEach
    void setUp() {
        documentRequestBuilder = new DocumentRequestBuilder();
    }

    /**
     * How to use
     * document()
     * .mockMvc(mockMvc)
     * .get(path)        HttpMethod
     * .auth(token)      default false
     * .build()          necessary!!!!!!!
     * .addStatusAndIdentifier(status().isOk(), "workbooks-get-success"); necessary!!!
     */
    protected MockMvcFunction document() {
        return documentRequestBuilder.build();
    }
}
