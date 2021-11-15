package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileUploader {
    String upload(MultipartFile multipartFile, User user);

    String upload(File file, User user);

    void deleteFromS3(String oldImageUrl);
}
