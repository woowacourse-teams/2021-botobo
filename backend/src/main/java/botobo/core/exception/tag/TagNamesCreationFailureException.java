package botobo.core.exception.tag;

import botobo.core.exception.common.BadRequestException;

public class TagNamesCreationFailureException extends BadRequestException {
    public TagNamesCreationFailureException(String reason) {
        super(String.format("TagNames객체 생성에 실패했습니다. (%s)", reason));
    }
}
