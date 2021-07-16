package botobo.core;

import botobo.core.quiz.domain.answer.Answer;
import botobo.core.quiz.domain.answer.AnswerRepository;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    @Value("${dummy.file-path}")
    private String filePath;
    @Value("${dummy.categories}")
    private List<String> categories;
    private CategoryRepository categoryRepository;
    private CardRepository cardRepository;
    private AnswerRepository answerRepository;

    public DataLoader(CategoryRepository categoryRepository, CardRepository cardRepository, AnswerRepository answerRepository) {
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!categoryRepository.findAll().isEmpty()) {
            return;
        }
        for (String category : categories) {
            readFile(filePath + category);
        }
    }

    public void readFile(String filePath) {
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            setData(bufferedReader);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            log.info("Dummy Data 파일 경로를 찾지 못했습니다.");
        } catch (IOException e) {
            log.info("Dummy Data를 불러오는 중 자바 입출력 에러가 발생했습니다.");
        }
    }

    private void setData(BufferedReader bufferedReader) throws IOException {
        String line = "";
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            Category category = makeCategory(line);
            Card card = Card.builder()
                    .category(category)
                    .question("")
                    .isDeleted(false)
                    .build();
            setQuestionAndAnswer(bufferedReader, category, card);
        }
    }

    private void setQuestionAndAnswer(BufferedReader bufferedReader, Category category, Card card) throws IOException {
        String line = "";
        int count = 0;
        while ((line = bufferedReader.readLine()) != null){
            if (count % 2 == 0) {
                card = setQuestion(category, line);
            } else {
                setAnswer(card, line);
            }
            count++;
        }
    }

    public Category makeCategory(String categoryName) {
        Category category = Category.builder()
                .name(categoryName)
                .isDeleted(false).build();
        return categoryRepository.save(category);
    }

    public Card setQuestion(Category category, String question) {
        Card card = Card.builder()
                .question(question)
                .category(category)
                .isDeleted(false)
                .build();
        return cardRepository.save(card);
    }

    public void setAnswer(Card card, String answer) {
        answerRepository.save(Answer.builder()
                .content(answer)
                .card(card)
                .isDeleted(false)
                .build());
    }

}
