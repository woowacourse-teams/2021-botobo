package botobo.core.documentation;

import botobo.core.application.WorkbookRankService;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.RankController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인기 시리즈 문서화 테스트")
@WebMvcTest(RankController.class)
class RankDocumentationTest extends DocumentationTest {

    @MockBean
    private WorkbookRankService workbookRankService;

    @Test
    @DisplayName("하트 수가 가장 높은 인기 문제집 제공 - 성공")
    void findWorkbookRanks() throws Exception {
        // given
        given(workbookRankService.findWorkbookRanks()).willReturn(createWorkbookReponses());

        // when // then
        document()
                .mockMvc(mockMvc)
                .get("/ranks/workbooks")
                .build()
                .status(status().isOk())
                .identifier("ranks-workbooks-get-success");
    }

    private List<WorkbookResponse> createWorkbookReponses() {
        return List.of(
                WorkbookResponse.builder()
                        .id(3L)
                        .name("카일의 react 문제집")
                        .cardCount(15)
                        .heartCount(20)
                        .author("GwangYeol-Im")
                        .tags(List.of(
                                TagResponse.builder().id(1L).name("javascript").build(),
                                TagResponse.builder().id(2L).name("frontend").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("피케이의 java 문제집")
                        .cardCount(15)
                        .heartCount(10)
                        .author("pkeugine")
                        .tags(List.of(
                                TagResponse.builder().id(3L).name("java").build(),
                                TagResponse.builder().id(4L).name("backend").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(1L)
                        .name("조앤의 kubernates 문제집")
                        .cardCount(15)
                        .heartCount(8)
                        .author("seovalue")
                        .tags(List.of(
                                TagResponse.builder().id(5L).name("docker").build(),
                                TagResponse.builder().id(6L).name("infrastructure").build()
                        ))
                        .build()
        );
    }
}
