package botobo.core.domain.workbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class DownloadCard {
    private String question;
    private String answer;
}
