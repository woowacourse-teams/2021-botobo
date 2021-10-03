package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.exception.auth.AccessTokenRenewalException;
import botobo.core.exception.auth.TokenNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

@DisplayName("인증 인터셉터 테스트")
@MockitoSettings
class AuthorizationInterceptorTest {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private AuthorizationInterceptor authorizationInterceptor;

    private final String accessToken = "botobo.access.token";
    private final String refreshToken = "botobo.refresh.token";

    @BeforeEach
    void setUp() {
        List<String> header = List.of(
                String.format("%s %s", BEARER_TYPE, accessToken)
        );
        Enumeration<String> enumeration = Collections.enumeration(header);
        given(request.getHeaders(AUTHORIZATION)).willReturn(enumeration);
    }

    @Test
    @DisplayName("올바른 액세스 토큰이면 요청한 api를 실행한다 - 성공")
    void requestWithValidAccessToken() {
        // given
        given(authService.isValidAccessToken(accessToken)).willReturn(true);

        // when
        boolean result = authorizationInterceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 액세스 토큰과 유효하지 않은 리프레시 토큰으로 api를 실행한다 - 성공")
    void requestWithInValidAccessTokenAndInvalidRefreshToken() {
        // given
        givenRefreshToken();
        given(authService.isValidAccessToken(accessToken)).willReturn(false);
        willThrow(TokenNotValidException.class).given(authService).validateRefreshToken(refreshToken);

        // when, then
        assertThatThrownBy(() -> authorizationInterceptor.preHandle(request, response, handler))
                .isInstanceOf(TokenNotValidException.class);

        then(authService).should(times(1)).isValidAccessToken(accessToken);
        then(authService).should(times(1)).validateRefreshToken(refreshToken);
    }


    @Test
    @DisplayName("유효하지 않은 액세스 토큰과 유효한 리프레시 토큰으로 api를 실행한다 - 성공")
    void requestWithInValidAccessTokenAndValidRefreshToken() {
        // given
        givenRefreshToken();
        given(authService.isValidAccessToken(accessToken)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authorizationInterceptor.preHandle(request, response, handler))
                .isInstanceOf(AccessTokenRenewalException.class);

        then(authService).should(times(1)).isValidAccessToken(accessToken);
        then(authService).should(times(1)).validateRefreshToken(refreshToken);
    }

    private void givenRefreshToken() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("BTOKEN_REFRESH", refreshToken);
        given(request.getCookies()).willReturn(cookies);
    }
}
