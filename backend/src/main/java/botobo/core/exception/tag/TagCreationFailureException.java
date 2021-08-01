package botobo.core.exception.tag;

import botobo.core.exception.BadRequestException;

public class TagCreationFailureException extends BadRequestException {
    public TagCreationFailureException(String reason) {
        super(String.format("Tag객체 생성에 실패했습니다. (%s)", reason));
    }
}
