package botobo.core.domain.workbook;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.stream.Collectors;

// TODO
@Getter
@NoArgsConstructor
@AllArgsConstructor
// 인덱스는 존재하지 않으면 새로 만들어줌. 하지만 미리 PUT index를 통해 인덱스를 매핑해놓은 뒤 사용하는 것 권장
@Document(indexName = "botobo-workbook-document")
public class WorkbookDocument {
    // Id 값이 필수로 필요함.
    @Field(type = FieldType.Long)
    private Long id;

    // Field 타입은 생략 가능하나 지정해주는 것이 예기치 못한 예외 방지에 좋음.
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
