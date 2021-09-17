package botobo.core.infrastructure;

import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import botobo.core.infrastructure.s3.FileNameGenerator;
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
    @DisplayName("파일 이름을 생성한다. - 성공")
    @MethodSource("createTestFiles")
    void generateFileName(MultipartFile multipartFile, String userId) {
        assertThat(fileNameGenerator.generateFileName(multipartFile, userId))
                .contains("users/1/")
                .contains(getDate());
    }

    @ParameterizedTest
    @DisplayName("파일 이름을 생성한다. - 실패, 허용하지 않는 파일 확장자")
    @MethodSource("createTestFilesWithNowAllowedExtension")
    void generateFileNameWithNotAllowedExtension(MultipartFile multipartFile, String userId) {
        assertThatThrownBy(() -> fileNameGenerator.generateFileName(multipartFile, userId))
                .isInstanceOf(ImageExtensionNotAllowedException.class);
    }

    private static Stream<Arguments> createTestFiles() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("png"), "1"),
                Arguments.of(FileFactory.testFile("jpg"), "1"),
                Arguments.of(FileFactory.testFile("jpeg"), "1")
        );
    }

    private static Stream<Arguments> createTestFilesWithNowAllowedExtension() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("txt"), "1"),
                Arguments.of(FileFactory.testFile("gif"), "1"),
                Arguments.of(FileFactory.testFile("mov"), "1"),
                Arguments.of(FileFactory.testFile("tiff"), "1")
        );
    }

    private String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
