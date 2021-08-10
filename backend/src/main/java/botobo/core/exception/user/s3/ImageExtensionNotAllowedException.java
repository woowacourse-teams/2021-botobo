package botobo.core.exception.user.s3;

import botobo.core.exception.http.InternalServerErrorException;

public class ImageExtensionNotAllowedException extends InternalServerErrorException {
    public ImageExtensionNotAllowedException() {
        super("허용되지 않는 파일 확장자입니다.");
    }
}
