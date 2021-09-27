package botobo.core.domain.user.s3;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UploadFileDto {
    private final MultipartFile multipartFile;
    private final String fileName;
    private final String contentType;

    private UploadFileDto(MultipartFile multipartFile, String fileName, String contentType) {
        this.multipartFile = multipartFile;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public static UploadFileDto of(MultipartFile multipartFile, String fileName, String contentType) {
        return new UploadFileDto(multipartFile, fileName, contentType);
    }
}
