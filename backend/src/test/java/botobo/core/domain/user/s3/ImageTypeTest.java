package botobo.core.domain.user.s3;

import botobo.core.infrastructure.file.ImageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"JPG", "jpg", "JPEG", "jpeg", "PNG", "png", "BMP", "bmp"})
    @DisplayName("허용되는 확장자이면 True를 반환한다. - 성공")
    void isAllowedExtension(String ext) {
        assertThat(ImageType.isAllowedExtension(ext))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"TIFF", "GIF", "gif", "MOV", "mov", "tiff", "txt", "TXT"})
    @DisplayName("허용되지 않는 확장자이면 False를 반환한다. - 성공")
    void isNotAllowedExtension(String ext) {
        assertThat(ImageType.isAllowedExtension(ext))
                .isFalse();
    }

    @DisplayName("ImageType에 따른 ContentType을 반환한다. - 성공")
    @ValueSource(strings = {"png image/png", "jpeg image/jpeg", "jpg image/jpeg", "bmp image/bmp"})
    @ParameterizedTest
    void contentType(String imageType) {
        // given
        String[] imageTypes = imageType.split(" ");
        assertThat(ImageType.contentType(imageTypes[0]))
                .isEqualTo(imageTypes[1]);
    }
}
