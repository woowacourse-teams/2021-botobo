package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import botobo.core.exception.user.s3.S3UploadFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Profile({"dev", "prod", "s3"})
@Slf4j
@RequiredArgsConstructor
@Component
public class FileS3Uploader implements FileUploader {

    private final AmazonS3 amazonS3Client;

    @Value("${aws.cloudfront.file.url-format}")
    private String cloudfrontFileUrlFormat;

    @Value("${aws.s3.file.bucket}")
    private String fileBucket;

    @Override
    public String upload(MultipartFile multipartFile, User user) {
        throw new IllegalStateException("잘못된 요청입니다.");
    }

    @Override
    public String upload(File file, User user) {
        String fileName = user.getUserName() + "/" + file.getName();
        try {
            amazonS3Client.putObject(new PutObjectRequest(fileBucket, fileName, file));
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
        return makeCloudFrontUrl(fileName);
    }

    @Override
    public void deleteFromS3(String oldImageUrl) {
        throw new IllegalStateException();
    }

    private String makeCloudFrontUrl(String uploadImageUrl) {
        return String.format(cloudfrontFileUrlFormat, uploadImageUrl);
    }
}
