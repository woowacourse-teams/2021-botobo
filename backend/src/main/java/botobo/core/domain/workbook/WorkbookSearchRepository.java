package botobo.core.domain.workbook;

import botobo.core.ui.search.SearchCriteria;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.heart.QHeart.heart;
import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.user.QUser.user;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;


@RequiredArgsConstructor
@Repository
public class WorkbookSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public DownloadWorkbooks findAllDownloadWorkbooksByUserId(Long userId) {
        List<DownloadWorkbook> downloadWorkbooks = jpaQueryFactory.from(workbook)
                .innerJoin(card)
                .on(workbook.id.eq(card.workbook.id))
                .distinct()
                .where(equalsUserId(userId))
                .transform(groupBy(workbook.id)
                        .list(Projections.constructor(
                                DownloadWorkbook.class,
                                workbook.name,
                                list(Projections.constructor(DownloadCard.class, card.question, card.answer))
                        ))
                );

        return new DownloadWorkbooks(downloadWorkbooks);
    }

    private BooleanExpression equalsUserId(Long userId) {
        return workbook.user.id.eq(userId);
    }

    public Page<Workbook> searchAll(WorkbookSearchParameter parameter,
                                    List<Long> tags,
                                    List<Long> users,
                                    Pageable pageable) {
        QueryResults<Workbook> results = queryBy(parameter, tags, users)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    public List<Workbook> searchAll(WorkbookSearchParameter parameter) {
        return queryBy(parameter)
                .limit(parameter.getSize())
                .fetch();
    }

    private JPAQuery<Workbook> queryBy(WorkbookSearchParameter parameter) {
        return queryBy(parameter, Collections.emptyList(), Collections.emptyList());
    }

    private JPAQuery<Workbook> queryBy(WorkbookSearchParameter parameter, List<Long> tags, List<Long> users) {
        return jpaQueryFactory.selectFrom(workbook)
                .innerJoin(workbook.user, user).fetchJoin()
                .innerJoin(workbook.cards.cards, card)
                .leftJoin(workbook.workbookTags, workbookTag)
                .leftJoin(workbookTag.tag, tag)
                .leftJoin(workbook.hearts.hearts, heart)
                .where(containKeyword(parameter.getSearchKeyword()),
                        containTags(tags),
                        containUsers(users),
                        openedTrue())
                .groupBy(workbook.id)
                .orderBy(findCriteria(parameter.getSearchCriteria()), workbook.id.asc());
    }

    private BooleanExpression containKeyword(SearchKeyword searchKeyword) {
        if (searchKeyword == null) {
            return null;
        }
        String keyword = searchKeyword.getValue();
        return containsKeywordInWorkbookName(keyword)
                .or(equalsKeywordInWorkbookTag(keyword));
    }

    private BooleanExpression containsKeywordInWorkbookName(String keyword) {
        return workbook.name.lower().contains(keyword);
    }

    private BooleanExpression equalsKeywordInWorkbookTag(String keyword) {
        return workbook.workbookTags.any().tag.tagName.value.eq(keyword);
    }

    private BooleanExpression containTags(List<Long> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return workbookTag
                .tag
                .id
                .in(tags);
    }

    private BooleanExpression containUsers(List<Long> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return user
                .id
                .in(users);
    }

    private BooleanExpression openedTrue() {
        return workbook.opened.isTrue();
    }

    private OrderSpecifier<?> findCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria == SearchCriteria.DATE) {
            return workbook.createdAt.desc();
        }
        if (searchCriteria == SearchCriteria.NAME) {
            return workbook.name.asc();
        }
        if (searchCriteria == SearchCriteria.COUNT) {
            return card.countDistinct().desc();
        }
        return heart.countDistinct().desc();
    }
}
