package botobo.core.utils;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.User;
import botobo.core.domain.workbook.Workbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkbookFactory {

    public static Workbook workbook(String workbookName,
                                    User user,
                                    int cardSize,
                                    boolean opened,
                                    Tags tags) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(user)
                .opened(opened)
                .tags(tags)
                .deleted(false)
                .build();
        workbook.addCards(makeCards(cardSize));
        return workbook;
    }

    public static Workbook workbook(String workbookName,
                                    User user,
                                    int cardSize,
                                    boolean opened,
                                    Tag... tags) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(user)
                .opened(opened)
                .tags(Tags.of(Arrays.asList(tags)))
                .deleted(false)
                .build();
        workbook.addCards(makeCards(cardSize));
        return workbook;
    }

    public static Workbook workbook(String workbookName,
                                    User user,
                                    int cardSize,
                                    boolean opened,
                                    User heartUser,
                                    Tags tags) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(user)
                .opened(opened)
                .tags(tags)
                .deleted(false)
                .build();
        workbook.addCards(makeCards(cardSize));
        workbook.toggleHeart(Heart.builder()
                .userId(heartUser.getId())
                .workbook(workbook)
                .build());
        return workbook;
    }

    private static Cards makeCards(int cardSize) {
        if (cardSize == 0) {
            return new Cards();
        }
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardSize; i++) {
            Card card = Card.builder()
                    .question("질문" + i)
                    .answer("답변" + i)
                    .deleted(false)
                    .build();
            cards.add(card);
        }
        return new Cards(cards);
    }

}
