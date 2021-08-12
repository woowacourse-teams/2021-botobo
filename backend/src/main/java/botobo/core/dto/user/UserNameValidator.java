package botobo.core.dto.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UserNameValidator implements ConstraintValidator<ValidUserName, String> {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        if (isNull(userName)) {
            addConstraintViolation(context, "U004");
            return false;
        }
        if (!hasProperLength(userName)) {
            addConstraintViolation(context, String.format("U005", MIN_LENGTH, MAX_LENGTH));
            return false;
        }
        if (hasWhiteSpace(userName)) {
            addConstraintViolation(context, "U006");
            return false;
        }
        return true;
    }

    private boolean hasWhiteSpace(String userName) {
        return !userName.matches("^\\S*$");
    }

    private boolean hasProperLength(String userName) {
        int length = userName.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }

    private boolean isNull(String userName) {
        return Objects.isNull(userName);
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();
    }

    @Override
    public void initialize(ValidUserName validUserName) {
        ConstraintValidator.super.initialize(validUserName);
    }
}
