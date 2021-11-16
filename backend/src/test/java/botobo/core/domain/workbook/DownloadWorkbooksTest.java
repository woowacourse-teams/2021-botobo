package botobo.core.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadWorkbooksTest {

    @DisplayName("")
    @Test
    void toTextFile() {
        // given
        DownloadWorkbooks downloadWorkbooks = new DownloadWorkbooks(
                Arrays.asList(
                        new DownloadWorkbook(
                                "조앤의 문제집" ,
                                Arrays.asList(new DownloadCard("뭐 먹어?", "미역국1"))
                        ),
                        new DownloadWorkbook(
                                "피케이 문제집",
                                Arrays.asList(
                                        new DownloadCard("내일 뭐 먹어?", "미역국2"),
                                        new DownloadCard("모레 뭐 먹어?", "미역국3")
                                )
                        )
                )
        );

        // when
        File file = downloadWorkbooks.toTextFile();

        // then
        assertThat(file).isNotNull();
        assertThat(file.exists()).isTrue();
    }
}
