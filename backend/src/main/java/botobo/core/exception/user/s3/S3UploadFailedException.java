package botobo.core.exception.user.s3;

import botobo.core.exception.common.ErrorType;
import botobo.core.exception.http.InternalServerErrorException;

public class S3UploadFailedException extends InternalServerErrorException {
    public S3UploadFailedException() {
        super(ErrorType.U013.getMessage());
    }
}
