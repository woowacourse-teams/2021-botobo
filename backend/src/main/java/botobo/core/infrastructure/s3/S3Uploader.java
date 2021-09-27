package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import botobo.core.domain.user.s3.UploadFileDto;
import botobo.core.exception.user.s3.S3UploadFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.net.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Profile({"dev", "prod"})
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader implements FileUploader {
    private final AmazonS3 amazonS3Client;
    private final FileNameGenerator fileNameGenerator;

    @Value("${aws.cloudfront.url-format}")
    private String cloudfrontUrlFormat;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.user-default-image}")
    private String userDefaultImageName;

    @Override
    public String upload(MultipartFile multipartFile, User user) {
        if (isEmpty(multipartFile)) {
            return makeCloudFrontUrl(userDefaultImageName);
        }

        UploadFileDto uploadFileDto = fileNameGenerator.generateUploadFile(
                multipartFile, String.valueOf(user.getId())
        );

        uploadImageToS3(uploadFileDto);

        return makeCloudFrontUrl(uploadFileDto.getFileName());
    }

    private void uploadImageToS3(UploadFileDto uploadFileDto) {
        MultipartFile uploadMultipartFile = uploadFileDto.getMultipartFile();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFileDto.getContentType());
        metadata.setContentLength(uploadMultipartFile.getSize());
        metadata.setCacheControl("max-age=31536000");

        try (final InputStream uploadImageFileInputStream = uploadMultipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket,
                    uploadFileDto.getFileName(),
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
        if (!Objects.equals(oldImageName, userDefaultImageName) && amazonS3Client.doesObjectExist(bucket, oldImageName)) {
            log.info("S3Uploader, S3에서 이미지(이미지명: {})를 삭제했습니다.", oldImageName);
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
}
