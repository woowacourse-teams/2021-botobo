package botobo.core.infrastructure;

import botobo.core.domain.user.User;
import botobo.core.exception.user.s3.FileConvertFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
    private String cloudfrontUrlFormat;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.user-default-image}")
    private String userDefaultImageName;

    public String upload(MultipartFile multipartFile, User user) throws IOException {
        final String oldImageUrl = user.getProfileUrl();
        final String userName = user.getUserName();

        if (isEmpty(multipartFile)) {
            deletePreviousImageFromS3(oldImageUrl);
            return makeCloudFrontUrl(userDefaultImageName);
        }

        String generatedFileName = fileNameGenerator.generateFileName(multipartFile, userName);
        uploadImageToS3(
                makeUploadImageFile(multipartFile),
                generatedFileName
        );

        deletePreviousImageFromS3(oldImageUrl);
        return makeCloudFrontUrl(generatedFileName);
    }

    private File makeUploadImageFile(MultipartFile multipartFile) throws IOException {
        return convert(multipartFile)
                .orElseThrow(FileConvertFailedException::new);
    }

    private void deletePreviousImageFromS3(String oldImageUrl) {
        String oldImageName = oldImageUrl.replace(
                cloudfrontUrl(),
                ""
        );
        if (!Objects.equals(oldImageName, userDefaultImageName) && amazonS3Client.doesObjectExist(bucket, oldImageName)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, oldImageName));
        }
    }

    private String cloudfrontUrl() {
        return cloudfrontUrlFormat.replace("%s", "");
    }

    private boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    private String makeCloudFrontUrl(String uploadImageUrl) {
        return String.format(cloudfrontUrlFormat, uploadImageUrl);
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