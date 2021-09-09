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

import java.util.List;

import static botobo.core.domain.workbook.QWorkbook.workbook;

@RequiredArgsConstructor
public class WorkbookRepositoryImpl implements WorkbookRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Workbook> searchAll(WorkbookSearchParameter parameter, List<Long> tags,
                                    List<Long> users, Pageable pageable) {
       QueryResults<Workbook> results =  jpaQueryFactory.selectFrom(workbook)
               .where(containKeyword(parameter.getSearchKeyword()), containTags(tags),
                        containUsers(users), containCard())
               .orderBy(findCriteria(parameter.getSearchCriteria()), workbook.id.asc())
               .offset(pageable.getOffset())
               .limit(pageable.getPageSize())
               .fetchResults();
       return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression containCard() {
        return workbook.cards
                .cards
                .isNotEmpty();
    }

    private BooleanExpression containKeyword(SearchKeyword searchKeyword) {
        if (searchKeyword == null || searchKeyword.equals("")) {
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
        return workbook
                .workbookTags
                .any()
                .tag
                .id
                .in(tags);
    }

    private BooleanExpression containUsers(List<Long> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return workbook
                .user
                .id
                .in(users);
    }

    private OrderSpecifier<?> findCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria == SearchCriteria.DATE) {
            return workbook.createdAt.desc();
        }
        if (searchCriteria == SearchCriteria.NAME) {
            return workbook.name.asc();
        }
        if (searchCriteria == SearchCriteria.COUNT) {
            return workbook.cards.cards.size().desc();
        }
        return workbook.hearts.hearts.size().desc();
    }
}
