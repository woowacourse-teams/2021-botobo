package botobo.core.domain.tag;


import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbooktag.WorkbookTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class TagSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tag> findAllTagContaining(String keyword) {
        if (Objects.isNull(keyword)) {
            return Collections.emptyList();
        }

        return jpaQueryFactory.selectFrom(tag)
                .distinct()
                .innerJoin(tag.workbookTags, workbookTag)
                .innerJoin(workbookTag.workbook, workbook)
                .innerJoin(workbook.cards.cards, card)
                .where(containsTagName(keyword),
                        openedTrue())
                .fetch();
    }

    private BooleanExpression containsTagName(String keyword) {
        return tag.tagName.value.lower()
                .contains(keyword.toLowerCase(Locale.ROOT));
    }

    public List<Tag> findAllByContainsWorkbookName(String workbookName) {
        if (Objects.isNull(workbookName)) {
            return Collections.emptyList();
        }

        List<Workbook> workbooks = jpaQueryFactory.selectFrom(workbook)
                .distinct()
                .leftJoin(workbook.workbookTags, workbookTag).fetchJoin()
                .leftJoin(workbookTag.tag, tag).fetchJoin()
                .innerJoin(workbook.cards.cards, card)
                .where(containKeyword(workbookName),
                        openedTrue())
                .fetch();

        Set<Tag> tags = new HashSet<>();
        for (Workbook workbook : workbooks) {
            List<WorkbookTag> workbookTags = workbook.getWorkbookTags();
            for (WorkbookTag workbookTag : workbookTags) {
                tags.add(workbookTag.getTag());
            }
        }
        return new ArrayList<>(tags);
    }

    private BooleanExpression containKeyword(String workbookName) {
        return containsKeywordInWorkbook(workbookName)
                .or(equalsKeywordInWorkbookTag(workbookName));
    }

    private BooleanExpression containsKeywordInWorkbook(String workbookName) {
        return workbook
                .name
                .lower()
                .contains(workbookName.toLowerCase());
    }

    private BooleanExpression equalsKeywordInWorkbookTag(String workbookName) {
        return workbookTag
                .tag
                .tagName
                .value
                .eq(workbookName);
    }

    private BooleanExpression openedTrue() {
        return workbook.opened.isTrue();
    }

}
