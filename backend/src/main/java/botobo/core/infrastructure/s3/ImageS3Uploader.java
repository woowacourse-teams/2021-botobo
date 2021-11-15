package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import botobo.core.exception.user.s3.S3UploadFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Profile({"dev", "prod", "s3"})
@Slf4j
@RequiredArgsConstructor
@Component
public class ImageS3Uploader implements FileUploader {
    private final AmazonS3 amazonS3Client;
    private final FileNameGenerator fileNameGenerator;

    @Value("${aws.cloudfront.image.url-format}")
    private String cloudfrontImageUrlFormat;

    @Value("${aws.s3.image.bucket}")
    private String imageBucket;

    @Value("${aws.user-default-image}")
    private String userDefaultImageName;

    @Override
    public String upload(MultipartFile multipartFile, User user) {
        if (isEmpty(multipartFile)) {
            return makeCloudFrontUrl(userDefaultImageName);
        }

        UploadFile uploadFile = fileNameGenerator.generateUploadFile(
                multipartFile, String.valueOf(user.getId())
        );

        uploadImageToS3(uploadFile);

        return makeCloudFrontUrl(uploadFile.getFileName());
    }

    @Override
    public String upload(File file, User user) {
        throw new IllegalStateException("잘못된 요청입니다.");
    }

    private void uploadImageToS3(UploadFile uploadFile) {
        MultipartFile uploadMultipartFile = uploadFile.getMultipartFile();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFile.getContentType());
        metadata.setContentLength(uploadMultipartFile.getSize());
        metadata.setCacheControl("max-age=31536000");

        try (final InputStream uploadImageFileInputStream = uploadMultipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(
                    imageBucket,
                    uploadFile.getFileName(),
                    uploadImageFileInputStream,
                    metadata
            ));
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }

    @Override
    public void deleteFromS3(String oldImageUrl) {
        String oldImageName = oldImageUrl.replace(
                cloudfrontUrl(),
                ""
        );
        if (!Objects.equals(oldImageName, userDefaultImageName) && amazonS3Client.doesObjectExist(imageBucket, oldImageName)) {
            log.info("S3Uploader, S3에서 이미지(이미지명: {})를 삭제했습니다.", oldImageName);
            amazonS3Client.deleteObject(new DeleteObjectRequest(imageBucket, oldImageName));
        }
    }

    private String cloudfrontUrl() {
        return cloudfrontImageUrlFormat.replace("%s", "");
    }

    private boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    private String makeCloudFrontUrl(String uploadImageUrl) {
        return String.format(cloudfrontImageUrlFormat, uploadImageUrl);
    }
}
