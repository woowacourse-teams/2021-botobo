package botobo.core;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@Profile("local")
public class DataLoader implements CommandLineRunner {

    @Value("${dummy.file-path}")
    private String filePath;

    @Value("${dummy.bootrun-file-path}")
    private String bootrunFilePath;

    @Value("${dummy.workbooks}")
    private List<String> workbooks;

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public DataLoader(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(String... args) {
        String targetPath = filePath;
        if (isBootrun(bootrunFilePath)) {
            targetPath = bootrunFilePath;
        }
        for (String workbook : workbooks) {
            readFile(targetPath + workbook);
        }
    }

    private boolean isBootrun(String filePath) {
        return new File(filePath).exists();
    }

    public void readFile(String filePath) {
        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            saveData(bufferedReader);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            log.info("Dummy Data 파일 경로를 찾지 못했습니다.");
        } catch (IOException e) {
            log.info("Dummy Data를 불러오는 중 자바 입출력 에러가 발생했습니다.");
        }
    }

    private void saveData(BufferedReader bufferedReader) throws IOException {
        String workbookName = bufferedReader.readLine();
        if (Objects.isNull(workbookName)) {
            return;
        }
        Workbook workbook = saveWorkbook(workbookName);
        String question = bufferedReader.readLine();
        String answer = bufferedReader.readLine();

        while (Objects.nonNull(question) && Objects.nonNull(answer)) {
            saveCard(workbook, question, answer);
            question = bufferedReader.readLine();
            answer = bufferedReader.readLine();
        }
    }

    private Workbook saveWorkbook(String workbookName) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .build();
        return workbookRepository.save(workbook);
    }

    private void saveCard(Workbook workbook, String question, String answer) {
        Card card = Card.builder()
                .workbook(workbook)
                .question(question)
                .answer(answer)
                .build();
        cardRepository.save(card);
    }
}
