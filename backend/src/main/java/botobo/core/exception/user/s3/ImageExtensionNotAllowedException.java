package botobo.core.exception.user.s3;

import botobo.core.exception.common.BadRequestException;

public class ImageExtensionNotAllowedException extends BadRequestException {
    public ImageExtensionNotAllowedException() {
        super("허용되지 않는 파일 확장자입니다.");
    }

    public ImageExtensionNotAllowedException(String message) {
        super(message);
    }
}
