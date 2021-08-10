package botobo.core;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@Profile("local")
@Transactional
public class DataLoader implements CommandLineRunner {

    @Value("${dummy.file-path}")
    private String filePath;

    @Value("${dummy.bootrun-file-path}")
    private String bootrunFilePath;

    @Value("${dummy.workbooks}")
    private List<String> workbooks;

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private User adminUser;
    private User normalUser;
    private int adminWorkbookCount;

    public DataLoader(WorkbookRepository workbookRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        this.adminUser = saveAdminUser();
        this.normalUser = saveNormalUser();
        this.adminWorkbookCount = 11;
        String targetPath = filePath;
        if (isBootrun(bootrunFilePath)) {
            targetPath = bootrunFilePath;
        }
        for (String workbook : workbooks) {
            for (int i = 0; i < 10; i++) {
                readFile(targetPath + workbook, i);
            }
        }
    }

    private boolean isBootrun(String filePath) {
        return new File(filePath).exists();
    }

    public void readFile(String filePath, int i) {
        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            saveData(bufferedReader, i);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            log.info("Dummy Data 파일 경로를 찾지 못했습니다.");
        } catch (IOException e) {
            log.info("Dummy Data를 불러오는 중 자바 입출력 에러가 발생했습니다.");
        }
    }

    private void saveData(BufferedReader bufferedReader, int i) throws IOException {
        String workbookName = bufferedReader.readLine();
        if (Objects.isNull(workbookName)) {
            return;
        }
        Workbook workbook = saveWorkbook(workbookName + i);
        String question = bufferedReader.readLine();
        String answer = bufferedReader.readLine();

        while (Objects.nonNull(question) && Objects.nonNull(answer)) {
            saveCard(workbook, question, answer);
            question = bufferedReader.readLine();
            answer = bufferedReader.readLine();
        }
    }

    private User saveAdminUser() {
        User user = User.builder()
                .userName("admin")
                .socialId("88036280")
                .profileUrl("https://avatars.githubusercontent.com/u/88036280?v=4")
                .role(Role.ADMIN)
                .build();
        return userRepository.save(user);
    }

    private User saveNormalUser() {
        User user = User.builder()
                .userName("user")
                .socialId("88143445")
                .profileUrl("botobo.profile.url")
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    private Workbook saveWorkbook(String workbookName) {
        User author = normalUser;
        if (adminWorkbookCount > 0) {
            author = adminUser;
            adminWorkbookCount--;
        }
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(author)
                .opened(true)
                .tags(Tags.of(Collections.singletonList(Tag.of(workbookName))))
                .build();
        Heart heart1 = Heart.builder()
                .workbook(workbook)
                .userId(adminUser.getId())
                .build();
        Heart heart2 = Heart.builder()
                .workbook(workbook)
                .userId(normalUser.getId())
                .build();
        if (author.equals(adminUser)) {
            workbook.toggleHeart(heart1);
        }
        if (author.equals(normalUser)) {

            workbook.toggleHeart(heart1);
            workbook.toggleHeart(heart2);
        }
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
