package botobo.core.domain.user.s3;

import java.util.Arrays;

public enum ImageType {
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
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
}
