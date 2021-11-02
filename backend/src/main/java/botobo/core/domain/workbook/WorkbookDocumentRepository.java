package botobo.core.domain.workbook;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

// TODO
public interface WorkbookDocumentRepository extends ElasticsearchRepository<WorkbookDocument, Long> {
}
