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

// ref: https://techblog.woowahan.com/2638/
// 테스트가 통과하는데, docker image를 가져오느라 오래 걸려서 jenkins ec2 메모리 초과 우려로 우선은 disabled 처리 해 둠.
@Disabled
@DisplayName("S3 Uploader 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class S3UploaderTest {

    public static final String FILE_PATH = "/src/test/resources/";
    public static final String UPLOAD_IMAGE_NAME = "uploadBotobo.png";


    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.cloudfront.url-format}")
    private String cloudfrontUrlFormat;

    // 변경 예정
    private static final String defaultImage = "botobo.png";

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    S3Uploader s3Uploader;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(bucket);
        amazonS3.putObject(bucket, "botobo.png", new File(new File("").getAbsolutePath() + FILE_PATH + defaultImage));
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
}