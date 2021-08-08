package botobo.core.infrastructure;

import botobo.core.config.LocalStackS3Config;
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
    public static final String UPLOAD_IMAGE_NAME = "uploadBotobo.png";
    private static final String USER_DEFAULT_IMAGE = "botobo-default-profile.png";

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

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(bucket);
        amazonS3.putObject(bucket,
                "botobo-default-profile.png",
                new File(new File("").getAbsolutePath() + FILE_PATH + USER_DEFAULT_IMAGE)
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공")
    void upload() throws IOException {
        File uploadFile = new File(new File("").getAbsolutePath() + FILE_PATH + UPLOAD_IMAGE_NAME);
        MultipartFile multipartFile = FileFactory.uploadFile(uploadFile, UPLOAD_IMAGE_NAME);

        String uploadUrl = s3Uploader.upload(multipartFile, "조앤");
        String cloudfrontUrl = cloudfrontUrlFormat.replace("%s", "");
        String uploadImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 S3에 업로드한다. - 성공, multipartFile이 비어있는 경우에는 디폴트 이미지로 대체")
    void uploadWithDefault() throws IOException {
        MultipartFile multipartFile = null;
        String uploadUrl = s3Uploader.upload(multipartFile, "조앤");
        String cloudfrontUrl = cloudfrontUrlFormat.replace("%s", "");
        String uploadImageName = uploadUrl.replace(cloudfrontUrl, "");

        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(String.format(cloudfrontUrlFormat, userDefaultImage)),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadImageName)).isTrue()
        );
    }
}