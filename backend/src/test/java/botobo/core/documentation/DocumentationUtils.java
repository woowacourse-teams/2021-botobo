package botobo.core.documentation;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class DocumentationUtils {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                prettyPrint(),
                modifyUris().host("botobo.r-e.kr").removePort()
        );
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
