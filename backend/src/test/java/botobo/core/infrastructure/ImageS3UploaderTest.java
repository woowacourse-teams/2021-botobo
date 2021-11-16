package botobo.core.infrastructure;

import botobo.core.config.LocalStackS3Config;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.infrastructure.s3.FileUploader;
import botobo.core.infrastructure.s3.ImageS3Uploader;
import botobo.core.utils.FileFactory;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Disabled
@ActiveProfiles("s3")
@DisplayName("Image S3 Uploader Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class ImageS3UploaderTest {

    public static final String FILE_PATH = "/src/test/resources/";
    public static final String UPLOAD_IMAGE_NAME = "imagesForS3Test/botobo-upload-profile.png";
    private static final String USER_DEFAULT_IMAGE_NAME = "imagesForS3Test/botobo-default-profile.png";
    private static final String EMPTY_IMAGE_NAME = "imagesForS3Test/botobo-empty-image.png";

    @Value("${aws.s3.image.bucket}")
    private String bucket;

    @Value("${aws.cloudfront.image.url-format}")
    private String cloudfrontUrlFormat;

    @Value("${aws.user-default-image}")
    private String userDefaultImageName;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ImageS3Uploader s3Uploader;

    private User user;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(bucket);
        amazonS3.putObject(bucket,
                "botobo-default-profile.png",
                new File(new File("").getAbsolutePath() + FILE_PATH + USER_DEFAULT_IMAGE_NAME)
        );
        user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("조앤")
                .profileUrl("https://avatars.githubusercontent.com/u/48412963?v=4")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공")
    void upload() throws IOException {
        MultipartFile multipartFile = getMultipartFile(UPLOAD_IMAGE_NAME);

        String uploadUrl = s3Uploader.upload(multipartFile, user);
        String uploadImageName = uploadUrl.replace(cloudfrontUrl(), "");

        assertAll(
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 제거한다. - 성공, 기본 이미지가 아닌 경우")
    void deletePreviousFile() throws IOException {
        // given
        String imageUrl = s3Uploader.upload(getMultipartFile(UPLOAD_IMAGE_NAME), user);
        String imageName = imageUrl.replace(cloudfrontUrl(), "");

        // when
        s3Uploader.deleteFromS3(imageUrl);

        // then
        assertAll(
                () -> assertThat(amazonS3.doesObjectExist(bucket, imageName)).isFalse()
        );
    }

    private String cloudfrontUrl() {
        return cloudfrontUrlFormat.replace("%s", "");
    }

    private MultipartFile getMultipartFile(String previousProfileImageName) throws IOException {
        File previousImageFile = new File(new File("").getAbsolutePath() + FILE_PATH + previousProfileImageName);
        return FileFactory.uploadFile(previousImageFile, previousProfileImageName);
    }

    @Test
    @DisplayName("이미지를 제거한다. - 실패, 기본 이미지인 경우")
    void deleteWhenPreviousFileIsDefault() throws IOException {
        // given - when
        s3Uploader.deleteFromS3(String.format(cloudfrontUrlFormat, userDefaultImageName));

        // then
        assertAll(
                () -> assertThat(amazonS3.doesObjectExist(bucket, userDefaultImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공, multipartFile이 null인 경우에는 디폴트 이미지로 대체")
    void uploadWithNull() {
        MultipartFile multipartFile = null;
        String uploadUrl = s3Uploader.upload(multipartFile, user);
        String cloudfrontUrl = cloudfrontUrl();
        String defaultImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(String.format(cloudfrontUrlFormat, userDefaultImageName)),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, defaultImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공, multipartFile이 empty인 경우에는 디폴트 이미지로 대체")
    void uploadWithEmpty() throws IOException {
        MultipartFile multipartFile = getMultipartFile(EMPTY_IMAGE_NAME);
        String uploadUrl = s3Uploader.upload(multipartFile, user);
        String cloudfrontUrl = cloudfrontUrl();
        String defaultImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(String.format(cloudfrontUrlFormat, userDefaultImageName)),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, defaultImageName)).isTrue()
        );
    }
}
