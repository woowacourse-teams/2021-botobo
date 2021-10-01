package botobo.core.domain.tag;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class TagFilterRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tag> findAllByContainsWorkbookName(String workbookName) {
        if (Objects.isNull(workbookName)) {
            return Collections.emptyList();
        }
        return jpaQueryFactory.selectFrom(tag)
                .distinct()
                .innerJoin(tag.workbookTags, workbookTag).fetchJoin()
                .innerJoin(workbookTag.workbook, workbook)
                .innerJoin(workbook.cards.cards, card)
                .where(containWorkbookName(workbookName),
                        openedTrue())
                .fetch();
    }

    private BooleanExpression containWorkbookName(String workbookName) {
        return workbook
                .name
                .lower()
                .contains(workbookName.toLowerCase());
    }

    private BooleanExpression openedTrue() {
        return workbook.opened.isTrue();
    }

}
