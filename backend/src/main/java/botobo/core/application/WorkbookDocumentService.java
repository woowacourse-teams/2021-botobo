package botobo.core.application;

import botobo.core.domain.workbook.WorkbookDocument;
import botobo.core.domain.workbook.WorkbookDocumentQueryBuilder;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// TODO
@Service
public class WorkbookDocumentService {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public WorkbookDocumentService(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public List<Long> search(WorkbookSearchParameter workbookSearchParameter, Pageable pageable) {
        /**
         * 입력으로 들어온 값에 대해 boolQuery를 만들어 검색을 한 뒤, 결과값의 ID를 바탕으로 실제 엔티티를 가져온다.
         */
        Query query = WorkbookDocumentQueryBuilder.makeQuery(workbookSearchParameter, pageable);
        SearchHits<WorkbookDocument> searchHits
                = elasticsearchRestTemplate.search(query, WorkbookDocument.class, IndexCoordinates.of("botobo-workbook-document"));

        final SearchPage<WorkbookDocument> searchPages
                = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        return searchPages.stream()
                .map(searchPage -> searchPage.getContent().getId())
                .collect(Collectors.toList());
    }
}
