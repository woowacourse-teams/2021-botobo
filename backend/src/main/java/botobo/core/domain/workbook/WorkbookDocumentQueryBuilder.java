package botobo.core.domain.workbook;

import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

// TODO
public class WorkbookDocumentQueryBuilder {
    private WorkbookDocumentQueryBuilder() {
    }

    public static Query makeQuery(WorkbookSearchParameter workbookSearchParameter,
                                  Pageable pageable) {
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        makeBoolQuery(query, workbookSearchParameter.getSearchKeyword());

        return query
                .withPageable(pageable)
                .build();
    }

    private static void makeBoolQuery(NativeSearchQueryBuilder query, SearchKeyword searchKeyword) {
        query.withQuery(
                QueryBuilders.boolQuery()
                        .must(multiField("*" + searchKeyword.getValue() + "*"))
        );
    }

    private static QueryBuilder multiField(String value) {
        return QueryBuilders.queryStringQuery(value)
                .field("workbookName")
                .field("tagNames");
    }
}
