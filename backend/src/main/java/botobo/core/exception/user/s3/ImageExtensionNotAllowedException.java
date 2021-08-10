package botobo.core.exception.user.s3;

import botobo.core.exception.http.BotoboInternalServerErrorException;

public class ImageExtensionNotAllowedException extends BotoboInternalServerErrorException {
    public ImageExtensionNotAllowedException() {
        super("허용되지 않는 파일 확장자입니다.");
    }
}
