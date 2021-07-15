package botobo.core.user.infrastructure;

import botobo.core.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class AuthorizationExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    public static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String headerValue = headers.nextElement();
            if (isBearerHeader(headerValue)) {
                return getAuthHeaderValue(request, headerValue);
            }
        }
        throw new UnauthorizedException("토큰 추출에 실패했습니다.");
    }


    private static String getAuthHeaderValue(HttpServletRequest request, String headerValue) {
        String authHeaderValue = headerValue.substring(BEARER_TYPE.length()).trim();
        request.setAttribute(ACCESS_TOKEN_TYPE, headerValue.substring(0, BEARER_TYPE.length()).trim());
        return substringBeforeComma(authHeaderValue);
    }

    private static String substringBeforeComma(String authHeaderValue) {
        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
            return authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    private static boolean isBearerHeader(String headerValue) {
        return headerValue.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }
}
