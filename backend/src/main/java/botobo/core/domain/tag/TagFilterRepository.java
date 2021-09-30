package botobo.core.domain.tag;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static botobo.core.domain.tag.QTag.tag;
import static botobo.core.domain.workbook.QWorkbook.workbook;
import static botobo.core.domain.workbooktag.QWorkbookTag.workbookTag;

@RequiredArgsConstructor
@Repository
public class TagFilterRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tag> findAllByContainsWorkbookName(@Param("workbookName") String workbookName) {
        QueryResults<Tag> results = jpaQueryFactory.selectFrom(tag)
                .innerJoin(tag.workbookTags, workbookTag).fetchJoin()
                .innerJoin(workbookTag.workbook, workbook)
                .where(containWorkbookName(workbookName))
                .fetchResults();
        return results.getResults();
    }

    private BooleanExpression containWorkbookName(String workbookName) {
        if (workbookName == null) {
            return null;
        }
        return workbook
                .name
                .lower()
                .contains(workbookName.toLowerCase());
    }


}
