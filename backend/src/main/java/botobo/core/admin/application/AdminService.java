package botobo.core.admin.application;

import botobo.core.admin.dto.*;
import botobo.core.card.domain.Answer;
import botobo.core.card.domain.AnswerRepository;
import botobo.core.card.domain.Card;
import botobo.core.card.domain.CardRepository;
import botobo.core.card.exception.CardNotFoundException;
import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final CategoryRepository categoryRepository;
    private final CardRepository cardRepository;
    private final AnswerRepository answerRepository;

    public AdminService(CategoryRepository categoryRepository, CardRepository cardRepository, AnswerRepository answerRepository) {
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public AdminCategoryResponse createCategory(AdminCategoryRequest adminCategoryRequest) {
        Category category = adminCategoryRequest.toCategory();
        Category savedCategory = categoryRepository.save(category);
        return AdminCategoryResponse.of(savedCategory);
    }

    @Transactional
    public AdminCardResponse createCard(AdminCardRequest adminCardRequest) {
        Long categoryId = adminCardRequest.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        Card card = Card.builder()
                .question(adminCardRequest.getQuestion())
                .category(category)
                .build();
        Card savedCard = cardRepository.save(card);
        return AdminCardResponse.of(savedCard);
    }

    @Transactional
    public AdminAnswerResponse createAnswer(AdminAnswerRequest adminAnswerRequest) {
        Long cardId = adminAnswerRequest.getCardId();
        Card card = cardRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new);
        Answer answer = Answer.builder()
                .card(card)
                .content(adminAnswerRequest.getContent())
                .build();
        Answer savedAnswer = answerRepository.save(answer);
        return AdminAnswerResponse.of(savedAnswer);
    }
}
