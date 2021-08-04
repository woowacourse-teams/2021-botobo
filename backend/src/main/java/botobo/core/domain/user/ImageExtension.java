package botobo.core.domain.user;

import java.util.Arrays;

public enum ImageExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    BMP("bmp");

    private final String extension;

    ImageExtension(String extension) {
        this.extension = extension;
    }

    public static boolean isAllowedExtension(String ext) {
        return Arrays.stream(values())
                .anyMatch(imageExtension -> ext.equalsIgnoreCase(imageExtension.extension));

    }
}
