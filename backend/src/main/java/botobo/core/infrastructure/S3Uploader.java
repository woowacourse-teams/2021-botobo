package botobo.core.infrastructure;

import botobo.core.exception.user.s3.FileConvertFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    private final AmazonS3 amazonS3Client;
    private final FileNameGenerator fileNameGenerator;

    @Value("${aws.cloudfront.url-format}")
    private String cloudFrontUrlFormat;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.user-default-image}")
    private String userDefaultImage;

    public String upload(MultipartFile multipartFile, String userName) throws IOException {
        if (isEmpty(multipartFile)) {
            return makeCloudFrontUrl(userDefaultImage);
        }
        final String newlyCreatedFileName = fileNameGenerator.generateFileName(multipartFile, userName);
        File uploadImageFile = convert(multipartFile)
                .orElseThrow(FileConvertFailedException::new);
        uploadImageToS3(uploadImageFile, newlyCreatedFileName);
        return makeCloudFrontUrl(newlyCreatedFileName);
    }

    private boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    private String makeCloudFrontUrl(String uploadImageUrl) {
        return String.format(cloudFrontUrlFormat, uploadImageUrl);
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private void uploadImageToS3(File uploadImage, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadImage));
        } finally {
            deleteTemporaryFile(uploadImage);
        }
    }

    private void deleteTemporaryFile(File uploadImage) {
        if (!uploadImage.delete()) {
            log.info("업로드용 파일이 제대로 삭제되지 않았습니다.");
        }
    }
}