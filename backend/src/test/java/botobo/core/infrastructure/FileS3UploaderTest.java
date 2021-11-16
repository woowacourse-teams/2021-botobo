package botobo.core.infrastructure;

import botobo.core.config.LocalStackS3Config;
import botobo.core.domain.user.User;
import botobo.core.domain.workbook.DownloadCard;
import botobo.core.domain.workbook.DownloadWorkbook;
import botobo.core.domain.workbook.DownloadWorkbooks;
import botobo.core.infrastructure.s3.FileS3Uploader;
import botobo.core.infrastructure.s3.ImageS3Uploader;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("s3")
@DisplayName("File S3 Uploader Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
public class FileS3UploaderTest {
    @Value("${aws.s3.file.bucket}")
    private String bucket;

    @Value("${aws.cloudfront.file.url-format}")
    private String cloudfrontUrlFormat;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private FileS3Uploader s3Uploader;

    private User user;
    private DownloadWorkbooks downloadWorkbooks;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(bucket);
        downloadWorkbooks = new DownloadWorkbooks(
                Arrays.asList(
                        new DownloadWorkbook(
                                "조앤의 문제집" ,
                                Arrays.asList(new DownloadCard("뭐 먹어?", "미역국1"))
                        ),
                        new DownloadWorkbook(
                                "피케이 문제집",
                                Arrays.asList(
                                        new DownloadCard("내일 뭐 먹어?", "미역국2"),
                                        new DownloadCard("모레 뭐 먹어?", "미역국3")
                                )
                        )
                )
        );

        user = User.builder()
                .userName("joanne")
                .build();
    }

    @Test
    @DisplayName("파일을 S3에 업로드한다. - 성공")
    void upload() {
        String uploadUrl = s3Uploader.upload(downloadWorkbooks.toTextFile(), user);
        String uploadFileName = uploadUrl.replace(cloudfrontUrl(), "");
        assertAll(
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(uploadUrl).contains(cloudfrontUrl()),
                () -> assertThat(uploadFileName).contains(user.getUserName()),
                () -> assertThat(amazonS3.doesObjectExist(bucket, uploadFileName)).isTrue()
        );
    }

    private String cloudfrontUrl() {
        return cloudfrontUrlFormat.replace("%s", "");
    }

}
