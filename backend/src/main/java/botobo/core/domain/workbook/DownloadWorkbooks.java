package botobo.core.domain.workbook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class DownloadWorkbooks {
    private final List<DownloadWorkbook> downloadWorkbooks;

    public DownloadWorkbooks(List<DownloadWorkbook> downloadWorkbooks) {
        this.downloadWorkbooks = downloadWorkbooks;
    }

    public File toTextFile() {
        File file = new File(getFilePath());
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            fileWriter.write(downloadWorkbookToString());
            fileWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Download 파일 I/O 작업에 실패했습니다.");
        }
        return file;
    }

    private String getFilePath() {
        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "_" + UUID.randomUUID() + ".txt";
    }

    private String downloadWorkbookToString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (DownloadWorkbook downloadWorkbook : downloadWorkbooks) {
            stringJoiner.add(downloadWorkbook.getName());
            stringJoiner.add(collectQuestionAndAnswer(downloadWorkbook.getCards()));
        }
        return stringJoiner.toString();
    }

    private String collectQuestionAndAnswer(List<DownloadCard> cards) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (DownloadCard card : cards) {
            stringJoiner.add("Q. " + card.getQuestion());
            stringJoiner.add("A. " + card.getAnswer());
        }
        stringJoiner.add("");
        return stringJoiner.toString();
    }

    public int size() {
        return downloadWorkbooks.size();
    }
}
