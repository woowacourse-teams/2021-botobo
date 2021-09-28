package botobo.core.domain.workbook;

import botobo.core.ui.search.SearchCriteria;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static botobo.core.domain.card.QCard.card;
import static botobo.core.domain.heart.QHeart.heart;
import static botobo.core.domain.user.QUser.user;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class WorkbookSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Workbook> searchAll(WorkbookSearchParameter parameter, List<Long> tags,
                                    List<Long> users, Pageable pageable) {
        QueryResults<Workbook> results = jpaQueryFactory.selectFrom(workbook)
                .innerJoin(workbook.user, user).fetchJoin()
                .innerJoin(workbook.cards.cards, card)
                .leftJoin(workbook.workbookTags, workbookTag)
                .leftJoin(workbook.hearts.hearts, heart)
                .where(containKeyword(parameter.getSearchKeyword()),
                        containTags(tags),
                        containUsers(users),
                        openedTrue())
                .groupBy(workbook.id)
                .orderBy(findCriteria(parameter.getSearchCriteria()), workbook.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression containKeyword(SearchKeyword searchKeyword) {
        if (searchKeyword == null) {
            return null;
        }
        return workbook
                .name
                .lower()
                .contains(searchKeyword.getValue().toLowerCase());
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
            return card.count().desc();
        }
        return heart.count().desc();
    }
}
