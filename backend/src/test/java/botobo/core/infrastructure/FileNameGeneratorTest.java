package botobo.core.infrastructure;

import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import botobo.core.infrastructure.file.FileNameGenerator;
import botobo.core.infrastructure.file.UploadFile;
import botobo.core.utils.FileFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileNameGeneratorTest {

    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    @ParameterizedTest
    @DisplayName("UploadFileDto를 생성한다. - 성공")
    @MethodSource("createTestFiles")
    void generateUploadFile(MultipartFile multipartFile, String expectedContentType) {
        UploadFile uploadFile = fileNameGenerator.generateUploadFile(multipartFile);
        assertThat(uploadFile.getFileName()).contains(getDate());
        assertThat(uploadFile.getContentType()).isEqualTo(expectedContentType);
    }

    @ParameterizedTest
    @DisplayName("파일 이름을 생성한다. - 실패, 허용하지 않는 파일 확장자")
    @MethodSource("createTestFilesWithNowAllowedExtension")
    void generateFileNameWithNotAllowedExtension(MultipartFile multipartFile) {
        assertThatThrownBy(() -> fileNameGenerator.generateUploadFile(multipartFile))
                .isInstanceOf(ImageExtensionNotAllowedException.class);
    }

    private static Stream<Arguments> createTestFiles() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("png"), "image/png"),
                Arguments.of(FileFactory.testFile("jpg"), "image/jpeg"),
                Arguments.of(FileFactory.testFile("jpeg"), "image/jpeg")
        );
    }

    private static Stream<Arguments> createTestFilesWithNowAllowedExtension() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("txt")),
                Arguments.of(FileFactory.testFile("gif")),
                Arguments.of(FileFactory.testFile("mov")),
                Arguments.of(FileFactory.testFile("tiff"))
        );
    }

    private String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
