package botobo.core.infrastructure;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.infrastructure.file.FileUploader;
import botobo.core.utils.FileFactory;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class ImageUploaderTest {
    public static final String FILE_PATH = "src/test/resources/";
    public static final String UPLOAD_IMAGE_NAME = "imagesForS3Test/botobo-upload-profile.png";
    private static final String EMPTY_IMAGE_NAME = "imagesForS3Test/botobo-empty-image.png";
    private static final String TEST_IMAGE_PATH = "src/test/resources/test_images";

    @Value("${file.user-default-image}")
    private String userDefaultImage;

    @Value("${file.image.url-format}")
    private String urlFormat;

    @Autowired
    private FileUploader imageUploader;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("조앤")
                .profileUrl("https://avatars.githubusercontent.com/u/48412963?v=4")
                .role(Role.USER)
                .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        File file = new File(TEST_IMAGE_PATH);
        if (file.exists()) {
            FileUtils.deleteDirectory(new File(TEST_IMAGE_PATH));
        }
    }

    @Test
    @DisplayName("이미지를 지정된 경로에 업로드한다. - 성공")
    void upload() throws IOException {
        // given
        MultipartFile multipartFile = getMultipartFile(UPLOAD_IMAGE_NAME);

        // when
        String uploadUrl = imageUploader.upload(multipartFile, user);

        // then
        assertThat(uploadUrl).isNotNull();
    }

    @Test
    @DisplayName("이미지를 제거한다. - 성공, 기본 이미지가 아닌 경우")
    void deletePreviousFile() throws IOException {
        // given
        String imageUrl = imageUploader.upload(getMultipartFile(UPLOAD_IMAGE_NAME), user);

        // when
        imageUploader.deleteFromS3(imageUrl);

        // then
        File file = new File(imageUrl);
        assertThat(file.exists()).isFalse();
    }


    @Test
    @DisplayName("이미지를 제거한다. - 실패, 기본 이미지인 경우")
    void deleteWhenPreviousFileIsDefault() {
        // given, when
        imageUploader.deleteFromS3(userDefaultImage);

        // then
        File file = new File(FILE_PATH + userDefaultImage.replace(urlFormat, ""));
        assertThat(file.exists()).isTrue();
    }

    @Test
    @DisplayName("이미지를 지정된 경로에 업로드한다. - 성공, multipartFile이 null인 경우에는 디폴트 이미지로 대체")
    void uploadWithNull() {
        // given, when
        MultipartFile multipartFile = null;
        String uploadUrl = imageUploader.upload(multipartFile, user);

        // then
        File file = new File(FILE_PATH + userDefaultImage.replace(urlFormat, ""));
        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(urlFormat + userDefaultImage),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(file.exists()).isTrue()
        );
    }

    @Test
    @DisplayName("이미지를 지정된 경로에 업로드한다. - 성공, multipartFile이 empty인 경우에는 디폴트 이미지로 대체")
    void uploadWithEmpty() throws IOException {
        // given, when
        MultipartFile multipartFile = getMultipartFile(EMPTY_IMAGE_NAME);
        String uploadUrl = imageUploader.upload(multipartFile, user);

        // then
        File file = new File(FILE_PATH + userDefaultImage.replace(urlFormat, ""));
        assertAll(
                () -> assertThat(uploadUrl).isEqualTo(urlFormat + userDefaultImage),
                () -> assertThat(uploadUrl).isNotNull(),
                () -> assertThat(file.exists()).isTrue()
        );
    }

    private MultipartFile getMultipartFile(String previousProfileImageName) throws IOException {
        File previousImageFile = new File(new File("").getAbsolutePath() + "/" + FILE_PATH + previousProfileImageName);
        return FileFactory.uploadFile(previousImageFile, previousProfileImageName);
    }
}
