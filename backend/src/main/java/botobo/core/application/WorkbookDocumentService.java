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
