package botobo.core.quiz.domain.answer;

import botobo.core.quiz.exception.AnswerNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Embeddable
public class Answers {

    @OneToMany(mappedBy = "card")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public String getFirstAnswerContent() {
        if (answers.isEmpty()) {
            throw new AnswerNotFoundException("답변이 존재하지 않습니다.");
        }
        return answers.get(0).getContent();
    }
}
