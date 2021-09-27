package botobo.core.domain.user.s3;

import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import org.springframework.http.MediaType;

import java.util.Arrays;

public enum ImageType {
    JPG("jpg", MediaType.IMAGE_JPEG_VALUE),
    JPEG("jpeg", MediaType.IMAGE_JPEG_VALUE),
    PNG("png", MediaType.IMAGE_PNG_VALUE),
    BMP("bmp", "image/bmp");

    private final String extension;
    private final String contentType;

    ImageType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static boolean isAllowedExtension(String ext) {
        return Arrays.stream(values())
                .anyMatch(imageExtension -> ext.equalsIgnoreCase(imageExtension.extension));

    }

    public static String contentType(String extension) {
        ImageType imageType = Arrays.stream(values())
                .filter(imageExtension -> extension.equalsIgnoreCase(imageExtension.extension))
                .findAny()
                .orElseThrow(ImageExtensionNotAllowedException::new);
        return imageType.contentType;
    }
}
