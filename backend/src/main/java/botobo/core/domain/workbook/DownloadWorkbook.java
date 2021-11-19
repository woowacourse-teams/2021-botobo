package botobo.core.domain.workbook;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DownloadWorkbook {
    private String name;
    private List<DownloadCard> cards = new ArrayList<>();
}
