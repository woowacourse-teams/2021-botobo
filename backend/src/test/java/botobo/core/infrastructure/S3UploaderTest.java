package botobo.core.infrastructure;

import botobo.core.config.LocalStackS3Config;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.utils.FileFactory;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Disabled
@DisplayName("S3 Uploader 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class S3UploaderTest {

    public static final String FILE_PATH = "/src/test/resources/";
    public static final String UPLOAD_IMAGE_NAME = "imagesForS3Test/botobo-upload-profile.png";
    private static final String USER_DEFAULT_IMAGE_NAME = "imagesForS3Test/botobo-default-profile.png";
    private static final String PREVIOUS_PROFILE_IMAGE_NAME = "imagesForS3Test/botobo-previous-profile.png";
    private static final String EMPTY_IMAGE_NAME = "imagesForS3Test/botobo-empty-image.png";

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.cloudfront.url-format}")
    private String cloudfrontUrlFormat;

    @Value("${aws.user-default-image}")
    private String userDefaultImage;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private S3Uploader s3Uploader;

    private User joanne;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(bucket);
        amazonS3.putObject(bucket,
                "botobo-default-profile.png",
                new File(new File("").getAbsolutePath() + FILE_PATH + USER_DEFAULT_IMAGE_NAME)
        );
        joanne = User.builder()
                .userName("조앤")
                .profileUrl(String.format(cloudfrontUrlFormat, userDefaultImage))
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공")
    void upload() throws IOException {
        MultipartFile multipartFile = getMultipartFile(UPLOAD_IMAGE_NAME);

        String uploadUrl = s3Uploader.upload(multipartFile, joanne);
        String uploadImageName = uploadUrl.replace(cloudfrontUrl(), "");

        assertAll(
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드 후 이전 이미지는 제거한다. - 성공, 이전 이미지가 기본 이미지가 아닌 경우")
    void deletePreviousFile() throws IOException {
        // given
        MultipartFile previousMultipartFile = getMultipartFile(PREVIOUS_PROFILE_IMAGE_NAME);
        String previousImageUrl = s3Uploader.upload(previousMultipartFile, joanne);
        String previousImageName = previousImageUrl.replace(cloudfrontUrl(), "");
        joanne.updateProfileUrl(previousImageUrl);

        // when
        String uploadUrl = s3Uploader.upload(getMultipartFile(UPLOAD_IMAGE_NAME), joanne);
        String uploadImageName = uploadUrl.replace(cloudfrontUrl(), "");

        // then
        assertAll(
                () -> assertThat(previousImageUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, previousImageName)).isFalse(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
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
    @DisplayName("이미지를 S3에 업로드 후 이전 이미지가 기본 이미지일 경우 제거하지 않는다. - 성공")
    void deleteWhenPreviousFileIsDefault() throws IOException {
        // given
        String previousImageUrl = "https://d1mlkr1uzdb8as.cloudfront.net/botobo-default-profile.png";
        String previousImageName = previousImageUrl.replace(cloudfrontUrl(), "");
        joanne.updateProfileUrl(previousImageUrl);

        // when
        String uploadUrl = s3Uploader.upload(getMultipartFile(UPLOAD_IMAGE_NAME), joanne);
        String uploadImageName = uploadUrl.replace(cloudfrontUrl(), "");

        // then
        assertAll(
                () -> assertThat(previousImageUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, previousImageName)).isTrue(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공, multipartFile이 null인 경우에는 디폴트 이미지로 대체")
    void uploadWithNull() throws IOException {
        MultipartFile multipartFile = null;
        String uploadUrl = s3Uploader.upload(multipartFile, joanne);
        String cloudfrontUrl = cloudfrontUrl();
        String defaultImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(String.format(cloudfrontUrlFormat, userDefaultImage)),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, defaultImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공, multipartFile이 empty인 경우에는 디폴트 이미지로 대체")
    void uploadWithEmpty() throws IOException {
        MultipartFile multipartFile = getMultipartFile(EMPTY_IMAGE_NAME);
        String uploadUrl = s3Uploader.upload(multipartFile, joanne);
        String cloudfrontUrl = cloudfrontUrl();
        String defaultImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(String.format(cloudfrontUrlFormat, userDefaultImage)),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, defaultImageName)).isTrue()
        );
    }
}