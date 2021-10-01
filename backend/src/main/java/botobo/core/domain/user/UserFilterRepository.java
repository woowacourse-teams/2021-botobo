package botobo.core.domain.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.user.QUser.user;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Repository
public class UserFilterRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<User> findAllByContainsWorkbookName(String workbookName) {
        if (Objects.isNull(workbookName)) {
            return emptyList();
        }

        return jpaQueryFactory.selectFrom(user)
                .distinct()
                .innerJoin(user.workbooks, workbook)
                .innerJoin(workbook.cards.cards, card)
                .where(containsWorkbookName(workbookName),
                        isOpened())
                .fetch();
    }

    private BooleanExpression containsWorkbookName(String workbookName) {
        return workbook
                .name
                .lower()
                .contains(workbookName.toLowerCase(Locale.ROOT));
    }

    private BooleanExpression isOpened() {
        return workbook
                .opened
                .isTrue();
    }
}
