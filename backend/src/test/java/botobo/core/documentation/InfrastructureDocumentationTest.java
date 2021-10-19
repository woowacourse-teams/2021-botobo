package botobo.core.documentation;

import botobo.core.ui.InfraController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인프라 관련 API 문서화 테스트")
@WebMvcTest(InfraController.class)
class InfrastructureDocumentationTest extends DocumentationTest {

    @Test
    @DisplayName("활성된 포트 번호 반환 - 성공")
    void portTest() throws Exception {
        document()
                .mockMvc(mockMvc)
                .get("/infra/port")
                .build()
                .status(status().isOk())
                .identifier("infra-profile-get-success");
    }
}
