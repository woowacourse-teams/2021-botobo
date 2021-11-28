package botobo.core.domain.tag;


import botobo.core.domain.tag.dto.TagDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class TagQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<TagDto> findAllTagContaining(String keyword) {
        if (Objects.isNull(keyword)) {
            return Collections.emptyList();
        }

        return jpaQueryFactory.from(tag)
                .select(Projections.constructor(TagDto.class,
                        tag.id,
                        tag.tagName.value
                )).distinct()
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

        return jpaQueryFactory.select(workbookTag.tag)
                .from(workbook)
                .distinct()
                .innerJoin(workbook.workbookTags, workbookTag)
                .innerJoin(workbookTag.tag, tag)
                .where(workbook.in(JPAExpressions.selectFrom(workbook)
                        .leftJoin(workbook.workbookTags, workbookTag)
                        .leftJoin(workbookTag.tag, tag)
                        .innerJoin(workbook.cards.cards, card)
                        .where(containKeyword(workbookName), openedTrue())))
                .fetch();
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
