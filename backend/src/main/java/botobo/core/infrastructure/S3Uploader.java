package botobo.core.infrastructure;

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

/**
 * ::흐름::
 * 1. MultipartFile을 전달 받는다.
 * 2. S3에 전달할 수 있도록 MultiPartFile을 File로 변환한다.
 * (S3에는 MultipartFile 타입은 전송할 수 없기 때문)
 * 3. 전환된 File을 S3에서 public 읽기 권한으로 PUT
 * (외부에서 정적 파일을 읽을 수 있도록 하기 위함)
 * 4. 로컬에 생성된 File 삭제
 * (Multipartfile -> File로 변환되면서 로컬에 파일 생성된 것을 삭제)
 * 5. 업로드된 파일의 S3 URL 주소를 반환
 */

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

    public String upload(MultipartFile multipartFile, String userName) throws IOException {
        // TODO 적절한 에러로 변환하기
        final String newlyCreatedFileName = fileNameGenerator.generateFileName(multipartFile, userName);
        File uploadImage = convert(multipartFile)
                .orElseThrow(IllegalArgumentException::new);
        return makeCloudFrontUrl(uploadImageUrl(uploadImage, newlyCreatedFileName));
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

    private String uploadImageUrl(File uploadImage, String newFileName) {
        String uploadImageUrl = putS3(uploadImage, newFileName);
        deleteTemporaryFile(uploadImage);
        return uploadImageUrl;
    }

    private void deleteTemporaryFile(File uploadImage) {
        if (!uploadImage.delete()) {
            log.info("업로드용 파일이 제대로 삭제되지 않았습니다.");
        }
    }

    private String putS3(File uploadImage, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadImage));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
