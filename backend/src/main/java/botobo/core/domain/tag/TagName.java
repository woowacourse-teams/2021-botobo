package botobo.core.domain.tag;


import botobo.core.exception.tag.TagNameLengthException;
import botobo.core.exception.tag.TagNameNullException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@EqualsAndHashCode(of = "value")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class TagName {

    private static final int MAX_LENGTH = 20;

    @Column(name = "NAME", nullable = false, unique = true, length = MAX_LENGTH)
    private String value;

    private TagName(String value) {
        validateNotNull(value);
        validateNotBlank(value);
        validateLength(value);
        this.value = value.trim().toLowerCase();
    }

    public static TagName of(String value) {
        return new TagName(value);
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new TagNameNullException();
        }
    }

    private void validateNotBlank(String value) {
        if (value.isBlank()) {
            throw new TagNameNullException();
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new TagNameLengthException();
        }
    }
}
