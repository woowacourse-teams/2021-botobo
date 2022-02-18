package botobo.core.infrastructure.file;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UploadFile {
    private final MultipartFile multipartFile;
    private final String fileName;
    private final String contentType;

    private UploadFile(MultipartFile multipartFile, String fileName, String contentType) {
        this.multipartFile = multipartFile;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public static UploadFile of(MultipartFile multipartFile, String fileName, String contentType) {
        return new UploadFile(multipartFile, fileName, contentType);
    }
}
