package botobo.core.exception.tag;

import botobo.core.exception.http.BotoboInternalServerErrorException;

public class TagsCreationFailureException extends BotoboInternalServerErrorException {
    public TagsCreationFailureException(String reason) {
        super(String.format("Tags객체 생성에 실패했습니다. (%s)", reason));
    }
}
