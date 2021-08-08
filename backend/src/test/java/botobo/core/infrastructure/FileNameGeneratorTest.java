package botobo.core.infrastructure;

import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
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
    void generateFileName(MultipartFile multipartFile, String userName) {
        assertThat(fileNameGenerator.generateFileName(multipartFile, userName))
                .contains("users/조앤/")
                .contains(getDate());
    }

    @ParameterizedTest
    @DisplayName("파일 이름을 생성한다. - 성공, userName에 공백이 포함되어있으면 _로 대체한다.")
    @MethodSource("createTestFilesWithWhiteSpaceUserName")
    void generateFileNameWithUserNameIncludeWhiteSpace(MultipartFile multipartFile, String userName) {
        assertThat(fileNameGenerator.generateFileName(multipartFile, userName))
                .contains("users/박사_조앤/")
                .contains(getDate());
    }

    @ParameterizedTest
    @DisplayName("파일 이름을 생성한다. - 실패, 허용하지 않는 파일 확장자")
    @MethodSource("createTestFilesWithNowAllowedExtension")
    void generateFileNameWithNotAllowedExtension(MultipartFile multipartFile, String userName) {
        assertThatThrownBy(() -> fileNameGenerator.generateFileName(multipartFile, userName))
                .isInstanceOf(ImageExtensionNotAllowedException.class);
    }

    private static Stream<Arguments> createTestFiles() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("png"), "조앤"),
                Arguments.of(FileFactory.testFile("jpg"), "조앤"),
                Arguments.of(FileFactory.testFile("jpeg"), "조앤")
        );
    }

    private static Stream<Arguments> createTestFilesWithWhiteSpaceUserName() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("png"), "박사 조앤")
        );
    }

    private static Stream<Arguments> createTestFilesWithNowAllowedExtension() {
        return Stream.of(
                Arguments.of(FileFactory.testFile("txt"), "조앤"),
                Arguments.of(FileFactory.testFile("gif"), "조앤"),
                Arguments.of(FileFactory.testFile("mov"), "조앤"),
                Arguments.of(FileFactory.testFile("tiff"), "조앤")
        );
    }

    private String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}