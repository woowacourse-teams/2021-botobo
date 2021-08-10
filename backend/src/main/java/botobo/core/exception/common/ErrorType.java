package botobo.core.exception;

import botobo.core.exception.auth.NotAdminException;
import botobo.core.exception.auth.OauthApiFailedException;
import botobo.core.exception.auth.TokenExpirationException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.auth.UserProfileLoadFailedException;
import botobo.core.exception.card.CardAnswerNullException;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.card.CardQuestionNullException;
import botobo.core.exception.card.QuizEmptyException;
import botobo.core.exception.request.RequestValidationFailrureException;
import botobo.core.exception.tag.TagNameLengthException;
import botobo.core.exception.tag.TagNameNullException;
import botobo.core.exception.tag.TagNullException;
import botobo.core.exception.user.NotAuthorException;
import botobo.core.exception.user.SocialTypeNotFoundException;
import botobo.core.exception.user.UserNotFoundException;
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
    A001("A001", "토큰이 유효하지 않습니다.", TokenNotValidException.class),
    A002("A002", "만료된 토큰입니다.", TokenExpirationException.class),
    A003("A003", "작성자가 아니므로 권한이 없습니다.", NotAuthorException.class),
    A004("A004", "AccessToken을 받아오는데 실패했습니다.", OauthApiFailedException.class),
    A005("A005", "유저정보를 불러오는데 실패했습니다.", UserProfileLoadFailedException.class),
    A006("A006", "Admin 권한이 아니기에 접근할 수 없습니다.", NotAdminException.class),
    A007("A007", "존재하지 않는 소셜 로그인 방식입니다.", SocialTypeNotFoundException.class),

    U001("U001", "해당 유저를 찾을 수 없습니다.", UserNotFoundException.class),

    W001("W001", "문제집 이름은 30자 이하여야 합니다.", WorkbookNameLengthException.class),
    W002("W002", "문제집 이름은 필수 입력값입니다.", WorkbookNameNullException.class),
    W003("W003", "태그 아이디는 필수 입력값입니다.", RequestValidationFailrureException.class),
    W004("W004", "태그 아이디는 0이상의 숫자입니다.", RequestValidationFailrureException.class),
    W005("W005", "태그 이름은 필수 입력값입니다.", TagNameNullException.class),
    W006("W006", "태그는 20자 이하여야 합니다.", TagNameLengthException.class),
    W007("W007", "문제집이 가질 수 있는 태그수는 최대 3개 입니다.", WorkbookTagLimitException.class),
    W008("W008", "문제집을 수정하려면 태그가 필요합니다.", TagNullException.class),
    W009("W009", "문제집의 공개 여부는 필수 입력값입니다.", RequestValidationFailrureException.class),
    W010("W010", "카드 개수는 필수 입력값입니다..", RequestValidationFailrureException.class),
    W011("W011", "카드 개수는 0이상 입니다.", RequestValidationFailrureException.class),
    W012("W012", "좋아요 개수는 필수 입력값입니다.", RequestValidationFailrureException.class),
    W013("W013", "좋아요 개수는 0이상 입니다.", RequestValidationFailrureException.class),
    W014("W014", "해당 문제집을 찾을 수 없습니다.", WorkbookNotFoundException.class),
    W015("W015", "공개 문제집이 아닙니다.", NotOpenedWorkbookException.class),
    W016("W016", "카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.", RequestValidationFailrureException.class),

    C001("C001", "해당 카드를 찾을 수 없습니다.", CardNotFoundException.class),
    C002("C002", "질문은 필수 입력값입니다.", CardQuestionNullException.class),
    C003("C003", "질문은 최대 2000자까지 입력 가능합니다.", RequestValidationFailrureException.class),
    C004("C004", "답변은 필수 입력값입니다.", CardAnswerNullException.class),
    C005("C005", "답변은 최대 2000자까지 입력 가능합니다.", RequestValidationFailrureException.class),
    C006("C006", "문제집 아이디는 필수 입력값입니다.", RequestValidationFailrureException.class),
    C007("C007", "문제집 아이디는 0이상의 숫자입니다.", RequestValidationFailrureException.class),
    C008("C008", "마주친 횟수는 필수 입력값입니다.", RequestValidationFailrureException.class),
    C009("C009", "마주친 횟수는 0이상 입니다.", RequestValidationFailrureException.class),
    C010("C010", "카드를 수정하기 위해서는 북마크 정보가 필요합니다.", RequestValidationFailrureException.class),
    C011("C011", "카드를 수정 위해서는 또 보기 정보가 필요합니다.", RequestValidationFailrureException.class),
    C012("C012", "유효하지 않은 또 보기 카드 등록 요청입니다.", RequestValidationFailrureException.class),

    Q001("Q001", "퀴즈를 진행하려면 문제집 아이디가 필요합니다.", RequestValidationFailrureException.class),
    Q002("Q002", "퀴즈의 개수는 10 ~ 30 사이의 수만 가능합니다.", RequestValidationFailrureException.class),
    Q003("Q003", "퀴즈에 문제가 존재하지 않습니다.", QuizEmptyException.class),

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
