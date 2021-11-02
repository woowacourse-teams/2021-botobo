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

    // 현재는 간단하게 와일드카드를 이용해 해당 키워드가 workbookName이나 tagName 필드에 포함된 경우만 조회되도록 함.
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
