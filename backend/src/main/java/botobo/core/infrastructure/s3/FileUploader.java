package botobo.core.infrastructure.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    String upload(MultipartFile multipartFile, String userId) throws IOException;

    void deleteFromS3(String oldImageUrl);
}
