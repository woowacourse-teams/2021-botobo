package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
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

@Profile({"dev1", "dev2", "prod1", "prod2"})
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

        String generatedFileName = fileNameGenerator.generateFileName(multipartFile, String.valueOf(user.getId()));
//        uploadImageToS3(
//                makeUploadImageFile(multipartFile),
//                generatedFileName
//        );

        uploadImageToS3(
                multipartFile,
                generatedFileName
        );

        return makeCloudFrontUrl(generatedFileName);
    }

//    private void uploadImageToS3(File uploadImage, String fileName) {
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setCacheControl("max-age=31536000");
//            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, new FileInputStream(uploadImage), metadata));
//        } catch (FileNotFoundException e) {
//            log.info("{}의 파일을 찾을 수 없습니다.",uploadImage.getName());
//        } finally {
//            deleteTemporaryFile(uploadImage);
//        }
//    }

    private void uploadImageToS3(MultipartFile uploadImageFile, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(String.valueOf(MediaType.ANY_IMAGE_TYPE));
        metadata.setContentLength(uploadImageFile.getSize());
        try (final InputStream uploadImageFileInputStream = uploadImageFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadImageFileInputStream, metadata));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private File makeUploadImageFile(MultipartFile multipartFile) throws IOException {
//        return convert(multipartFile)
//                .orElseThrow(FileConvertFailedException::new);
//    }

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

//    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
//        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        if (convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(multipartFile.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//        return Optional.empty();
//    }
//
//    private void deleteTemporaryFile(File uploadImage) {
//        if (!uploadImage.delete()) {
//            log.info("업로드용 파일이 제대로 삭제되지 않았습니다.");
//        }
//    }
}
