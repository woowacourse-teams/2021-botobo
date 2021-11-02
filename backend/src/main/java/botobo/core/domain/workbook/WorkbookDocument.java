package botobo.core.domain.workbook;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

// TODO
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "botobo-workbook-document")
public class WorkbookDocument {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    private String workbookName;

    @Field(type = FieldType.Auto)
    private List<String> tagNames;

    public static WorkbookDocument of(Long id, String workbookName, Tags tags) {
        List<String> tagNames = tags.toList()
                .stream()
                .map(Tag::getTagNameValue)
                .collect(Collectors.toList());
        return new WorkbookDocument(id, workbookName, tagNames);
    }
}
