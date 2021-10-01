package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String upload(MultipartFile multipartFile, User user);

    void deleteFromS3(String oldImageUrl);
}
