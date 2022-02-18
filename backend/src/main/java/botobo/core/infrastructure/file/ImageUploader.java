package botobo.core.infrastructure.file;

import botobo.core.domain.user.User;
import botobo.core.exception.user.s3.S3UploadFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ImageUploader implements FileUploader {

    private final FileNameGenerator fileNameGenerator;

    @Value("${file.image.directory}")
    private String fileDirectory;

    @Value("${file.user-default-image}")
    private String userDefaultImageName;

    @Value("${file.image.url-format}")
    private String urlFormat;

    @Override
    public String upload(MultipartFile multipartFile, User user) {
        if (isEmpty(multipartFile)) {
            return urlFormat + userDefaultImageName;
        }
        UploadFile uploadFile = fileNameGenerator.generateUploadFile(multipartFile);
        return uploadImage(uploadFile, user.getId());
    }

    private String uploadImage(UploadFile uploadFile, Long userId) {
        MultipartFile multipartFile = uploadFile.getMultipartFile();
        Path directory = Paths.get(fileDirectory, String.valueOf(userId));
        try {
            Files.createDirectories(directory);
            Path targetPath = directory.resolve(uploadFile.getFileName()).normalize();
            multipartFile.transferTo(targetPath);
            return urlFormat + "images/" + userId + "/" + uploadFile.getFileName();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    @Override
    public String upload(File file, User user) {
        return null;
    }

    @Override
    public void deleteFromS3(String oldImageUrl) {
        try {
            if (!Objects.equals(oldImageUrl, userDefaultImageName) && oldImageUrl.startsWith(urlFormat)) {
                Path targetPath = Path.of(fileDirectory + oldImageUrl.replace(urlFormat, ""));
                Files.deleteIfExists(targetPath);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }
}
