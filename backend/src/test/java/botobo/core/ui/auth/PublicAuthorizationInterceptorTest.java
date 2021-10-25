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
import static org.mockito.BDDMockito.willThrow;

@DisplayName("공용 인증 인터셉터 테스트")
@MockitoSettings
class PublicAuthorizationInterceptorTest {

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
    private PublicAuthorizationInterceptor publicAuthorizationInterceptor;

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
    @DisplayName("올바른 액세스 토큰이면 회원으로 간주하여 요청한 api를 실행한다 - 성공")
    void requestWithValidAccessToken() {
        // when
        boolean result = publicAuthorizationInterceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 액세스 토큰과 유효한 리프레시 토큰으로 요청 시 " +
            "토큰이 만료된 회원으로 간주하여 AccessTokenRenewalException 을 발생시킨다 - 성공")
    void requestWithInValidAccessToken() {
        // given
        givenRefreshToken();
        willThrow(TokenNotValidException.class).given(authService).validateAccessToken(accessToken);

        // when, then
        assertThatThrownBy(() -> publicAuthorizationInterceptor.preHandle(request, response, handler))
                .isInstanceOf(AccessTokenRenewalException.class);
    }

    @Test
    @DisplayName("유효하지 않은 액세스 토큰과 유효하지 않은 리프레시 토큰으로 요청 시 비회원으로 간주하여 요청한 api를 실행한다 - 성공")
    void requestWithInValidAccessTokenAndInvalidRefreshToken() {
        // given
        givenRefreshToken();
        willThrow(TokenNotValidException.class).given(authService).validateAccessToken(accessToken);
        willThrow(TokenNotValidException.class).given(authService).validateRefreshToken(refreshToken);

        // when
        boolean result = publicAuthorizationInterceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    private void givenRefreshToken() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("BTOKEN_REFRESH", refreshToken);
        given(request.getCookies()).willReturn(cookies);
    }
}
