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
import java.util.Objects;
import java.util.Set;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class TagFilterRepository {

    private final JPAQueryFactory jpaQueryFactory;

//    public List<Tag> findAllByContainsWorkbookName(String workbookName) {
//        if (Objects.isNull(workbookName)) {
//            return Collections.emptyL
//        return jpaQueryFactory.selectFrom(tag)
//                .distinct()
//                .innerJoin(tag.workbookTags, workbookTag).fetchJoin()
//                .innerJoin(workbookTag.workbook, workbook)
//                .innerJoin(workbook.workbookTags, workbookTag)
//                .innerJoin(workbookTag.tag, tag)
//                .innerJoin(workbook.cards.cards, card)
//                .where(containKeyword(workbookName),
//                        openedTrue())
//                .fetch();
//    }

    public List<Tag> findAllByContainsWorkbookName(String workbookName) {
        if (Objects.isNull(workbookName)) {
            return Collections.emptyList();
        }

        List<Workbook> workbooks = jpaQueryFactory.selectFrom(workbook)
                .distinct()
                .leftJoin(workbook.workbookTags, workbookTag)
                .leftJoin(workbookTag.tag, tag)
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
