package botobo.core.exception.common;

import botobo.core.exception.BotoboException;
import botobo.core.exception.ExternalException;
import botobo.core.exception.auth.AccessTokenRenewalException;
import botobo.core.exception.auth.OauthApiFailedException;
import botobo.core.exception.auth.TokenExpirationException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.auth.UserProfileLoadFailedException;
import botobo.core.exception.card.CardAnswerNullException;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.card.CardQuestionNullException;
import botobo.core.exception.card.QuizEmptyException;
import botobo.core.exception.http.InternalServerErrorException;
import botobo.core.exception.search.ForbiddenSearchKeywordException;
import botobo.core.exception.search.InvalidPageSizeException;
import botobo.core.exception.search.InvalidPageStartException;
import botobo.core.exception.search.InvalidSearchCriteriaException;
import botobo.core.exception.search.InvalidSearchOrderException;
import botobo.core.exception.search.InvalidSearchTypeException;
import botobo.core.exception.search.LongSearchKeywordException;
import botobo.core.exception.search.SearchKeywordNullException;
import botobo.core.exception.search.ShortSearchKeywordException;
import botobo.core.exception.tag.TagNameLengthException;
import botobo.core.exception.tag.TagNameNullException;
import botobo.core.exception.tag.TagNullException;
import botobo.core.exception.user.NotAuthorException;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import botobo.core.exception.user.SocialTypeNotFoundException;
import botobo.core.exception.user.UserNameDuplicatedException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import botobo.core.exception.workbook.NotOpenedWorkbookException;
import botobo.core.exception.workbook.WorkbookNameBlankException;
import botobo.core.exception.workbook.WorkbookNameLengthException;
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
    A001("A001", "????????? ???????????? ????????????.", TokenNotValidException.class),
    A002("A002", "????????? ???????????????.", TokenExpirationException.class),
    A003("A003", "???????????? ???????????? ????????? ????????????.", NotAuthorException.class),
    A004("A004", "AccessToken??? ??????????????? ??????????????????.", OauthApiFailedException.class),
    A005("A005", "??????????????? ??????????????? ??????????????????.", UserProfileLoadFailedException.class),
    A007("A007", "???????????? ?????? ?????? ????????? ???????????????.", SocialTypeNotFoundException.class),
    A008("A008", "????????? ?????? ???????????? ???????????????.", AccessTokenRenewalException.class),

    U001("U001", "?????? ????????? ?????? ??? ????????????.", UserNotFoundException.class),
    U002("U002", "????????? ????????? ????????? ???????????????.", ProfileUpdateNotAllowedException.class),
    U003("U003", "?????? ???????????? ?????? ???????????????.", UserNameDuplicatedException.class),
    U004("U004", "???????????? ?????? ??????????????????.", ExternalException.class),
    U005("U005", "????????? ?????? 1??? ??????, ?????? 20????????? ?????? ???????????????.", ExternalException.class),
    U006("U006", "???????????? ????????? ????????? ??? ????????????.", ExternalException.class),
    U007("U007", "?????? ????????? ???????????? ???????????? ????????? ????????? ???????????????.", ExternalException.class),
    U008("U008", "?????? ????????? ???????????? ???????????? ???????????? ?????? 0??? ????????? ???????????????.", ExternalException.class),
    U009("U009", "???????????? ?????? 255????????? ???????????????.", ExternalException.class),
    U010("U010", "10MB ????????? ????????? ???????????? ??? ????????????.", ExternalException.class),
    U011("U011", "????????? ??? ?????? ?????? ?????? ????????? 100MB ?????????.", ExternalException.class),
    U012("U012", "???????????? ?????? ?????? ??????????????????.", ImageExtensionNotAllowedException.class),

    W001("W001", "????????? ????????? 30??? ???????????? ?????????.", WorkbookNameLengthException.class),
    W002("W002", "????????? ????????? ?????? ??????????????????.", WorkbookNameBlankException.class),
    W003("W003", "?????? ???????????? ?????? ??????????????????.", ExternalException.class),
    W004("W004", "?????? ???????????? 0????????? ???????????????.", ExternalException.class),
    W005("W005", "?????? ????????? ?????? ??????????????????.", TagNameNullException.class),
    W006("W006", "????????? 20??? ???????????? ?????????.", TagNameLengthException.class),
    W007("W007", "???????????? ?????? ??? ?????? ???????????? ?????? 3??? ?????????.", WorkbookTagLimitException.class),
    W008("W008", "???????????? ??????????????? ????????? ???????????????.", TagNullException.class),
    W009("W009", "???????????? ?????? ????????? ?????? ??????????????????.", ExternalException.class),
    W010("W010", "?????? ????????? ?????? ??????????????????..", ExternalException.class),
    W011("W011", "?????? ????????? 0?????? ?????????.", ExternalException.class),
    W012("W012", "????????? ????????? ?????? ??????????????????.", ExternalException.class),
    W013("W013", "????????? ????????? 0?????? ?????????.", ExternalException.class),
    W014("W014", "?????? ???????????? ?????? ??? ????????????.", WorkbookNotFoundException.class),
    W015("W015", "?????? ???????????? ????????????.", NotOpenedWorkbookException.class),
    W016("W016", "????????? ??? ??????????????? ???????????? ?????? ???????????? ???????????????.", ExternalException.class),

    C001("C001", "?????? ????????? ?????? ??? ????????????.", CardNotFoundException.class),
    C002("C002", "????????? ?????? ??????????????????.", CardQuestionNullException.class),
    C003("C003", "????????? ?????? 2000????????? ?????? ???????????????.", ExternalException.class),
    C004("C004", "????????? ?????? ??????????????????.", CardAnswerNullException.class),
    C005("C005", "????????? ?????? 2000????????? ?????? ???????????????.", ExternalException.class),
    C006("C006", "????????? ???????????? ?????? ??????????????????.", ExternalException.class),
    C007("C007", "????????? ???????????? 0????????? ???????????????.", ExternalException.class),
    C008("C008", "????????? ????????? ?????? ??????????????????.", ExternalException.class),
    C009("C009", "????????? ????????? 0?????? ?????????.", ExternalException.class),
    C010("C010", "????????? ???????????? ???????????? ????????? ????????? ???????????????.", ExternalException.class),
    C011("C011", "????????? ?????? ???????????? ??? ?????? ????????? ???????????????.", ExternalException.class),
    C012("C012", "???????????? ?????? ??? ?????? ?????? ?????? ???????????????.", ExternalException.class),

    Q001("Q001", "????????? ??????????????? ????????? ???????????? ???????????????.", ExternalException.class),
    Q002("Q002", "????????? ????????? 10 ~ 30 ????????? ?????? ???????????????.", ExternalException.class),
    Q003("Q003", "????????? ????????? ???????????? ????????????.", QuizEmptyException.class),

    S001("S001", "???????????? ?????? ?????? ????????? ??? ??? ????????????.", InvalidPageStartException.class),
    S002("S002", "???????????? ?????? ????????? ???????????????. ????????? ?????? : 1 ~ 100", InvalidPageSizeException.class),
    S003("S003", "???????????? ?????? ?????? ???????????????. ????????? ?????? ?????? : date, name, count, heart", InvalidSearchCriteriaException.class),
    S004("S004", "???????????? ?????? ?????? ???????????????. ????????? ?????? ?????? : ASC, DESC", InvalidSearchOrderException.class),
    S005("S005", "???????????? ?????? ?????? ???????????????. ????????? ?????? ?????? : name, tag, user", InvalidSearchTypeException.class),
    S006("S006", "???????????? null??? ??? ????????????.", SearchKeywordNullException.class),
    S007("S007", "???????????? 30??? ???????????? ?????????.", LongSearchKeywordException.class),
    S008("S008", "???????????? 1??? ??????????????? ?????????.", ShortSearchKeywordException.class),
    S009("S009", "???????????? ??????????????????.", ForbiddenSearchKeywordException.class),

    E001("E001", "????????????", InternalServerErrorException.class),
    E002("E002", "??????????????? ???????????? ?????????.", ExternalException.class),

    X001("X001", "???????????? ?????? ??????", UndefinedException.class),
    ;

    private final String code;
    private final String message;
    private final Class<? extends BotoboException> classType;

    private static final Map<Class<? extends BotoboException>, ErrorType> codeMap = new HashMap<>();

    static {
        Arrays.stream(values())
                .filter(ErrorType::isNotExternalException)
                .forEach(errorType -> codeMap.put(errorType.classType, errorType));
    }

    public static ErrorType of(Class<? extends BotoboException> classType) {
        if (classType.equals(ExternalException.class)) {
            throw new UnsupportedOperationException("???????????? ErrorType??? ????????? ??? ?????? ???????????????.");
        }
        return codeMap.getOrDefault(classType, ErrorType.X001);
    }

    public static ErrorType of(String code) {
        return Arrays.stream(values())
                .parallel()
                .filter(errorType -> errorType.hasSameCode(code))
                .findAny()
                .orElse(ErrorType.X001);
    }

    public boolean isNotExternalException() {
        return !classType.equals(ExternalException.class);
    }

    public boolean hasSameCode(String code) {
        return Objects.equals(this.code, code);
    }
}
