package botobo.core.exception;

import botobo.core.exception.tag.TagNameLengthException;
import botobo.core.exception.tag.TagNameNullException;
import botobo.core.exception.tag.TagNullException;
import botobo.core.exception.workbook.NotOpenedWorkbookException;
import botobo.core.exception.workbook.WorkbookNameLengthException;
import botobo.core.exception.workbook.WorkbookNameNullException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import botobo.core.exception.workbook.WorkbookTagLimitException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ErrorType {
    W001("W001", "문제집 이름은 30자 이하여야 합니다.", WorkbookNameLengthException.class),
    W002("W002", "문제집 이름은 필수 입력값입니다.", WorkbookNameNullException.class),
    W003("W003", "태그 아이디는 필수 입력값입니다.", RequestValidationFailrureException.class),
    W004("W004", "태그 아이디는 0이상의 숫자입니다.", RequestValidationFailrureException.class),
    W005("W005", "태그 이름은 필수 입력값입니다.", TagNameNullException.class),
    W006("W006", "태그는 20자 이하여야 합니다.", TagNameLengthException.class),
    W007("W007", "문제집이 가질 수 있는 태그수는 최대 3개 입니다.", WorkbookTagLimitException.class),
    W008("W008", "문제집을 수정하려면 태그가 필요합니다.", TagNullException.class),
    W009("W009", "문제집의 공개 여부는 필수 입력값입니다.", RequestValidationFailrureException.class),
    W010("W010", "카드 개수는 0이상 입니다.", RequestValidationFailrureException.class),
    W011("W011", "하트 수는 0이상 입니다.", RequestValidationFailrureException.class),
    W012("W012", "문제집을 수정하려면 태그가 필요합니다.", RequestValidationFailrureException.class),
    W013("W013", "해당 문제집을 찾을 수 없습니다.", WorkbookNotFoundException.class),
    W014("W014", "공개 문제집이 아닙니다.", NotOpenedWorkbookException.class),
    W015("W015", "카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.", RequestValidationFailrureException.class),
    X001("X001", "정의되지 않은 에러", UndefinedException.class);

    private final String code;
    private final String message;
    private final Class<? extends BotoboException> classType;

    private static final Map<Class<? extends BotoboException>, ErrorType> codeMap = new HashMap<>();

    static {
        Arrays.stream(values())
                .forEach(errorType -> codeMap.put(errorType.classType, errorType));
    }

    public static ErrorType of(Class<? extends BotoboException> classType) {
        return codeMap.getOrDefault(classType, ErrorType.X001);
    }

    public static ErrorType of(String code) {
        return Arrays.stream(values())
                .parallel()
                .filter(errorType -> errorType.hasSameCode(code))
                .findAny()
                .orElse(ErrorType.X001);
    }

    public boolean hasSameCode(String code) {
        return Objects.equals(this.code, code);
    }
}
