package botobo.core.exception.user.s3;

import botobo.core.exception.http.InternalServerErrorException;

public class S3UploadFailedException extends InternalServerErrorException {
    public S3UploadFailedException() {
        super("S3로 파일을 업로드하지 못했습니다.");
    }
}
