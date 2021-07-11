package botobo.core;

import botobo.core.card.domain.AnswerRepository;
import botobo.core.card.domain.CardRepository;
import botobo.core.category.domain.CategoryRepository;
import org.springframework.boot.CommandLineRunner;

// 더미 데이터 삽입 시에 사용될 DataLoader 뼈대 코드
public class DataLoader implements CommandLineRunner {
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
    }
}
