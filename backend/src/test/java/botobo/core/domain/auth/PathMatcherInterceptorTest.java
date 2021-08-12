package botobo.core.domain.auth;

import botobo.core.ui.auth.PathMatcherInterceptor;
import botobo.core.ui.auth.PathMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@MockitoSettings
public class PathMatcherInterceptorTest {

    @Mock
    private HandlerInterceptor handlerInterceptor;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private PathMatcherInterceptor pathMatcherInterceptor;

    @Test
    @DisplayName("preHandle 검사 - 동일한 api와 PathMethod를 추가시키면 preHandle은 다음 인터셉터의 preHandle을 반환한다.")
    void preHandle() throws Exception {
        // given
        given(httpServletRequest.getRequestURI())
                .willReturn("/api/**");
        given(httpServletRequest.getMethod())
                .willReturn(HttpMethod.GET.name());
        given(handlerInterceptor.preHandle(any(), any(), any()))
                .willReturn(true);
        pathMatcherInterceptor.addPathPatterns("/api/**", PathMethod.GET);

        // when, then
        assertThat(pathMatcherInterceptor.preHandle(httpServletRequest, null, null)).isEqualTo(
                handlerInterceptor.preHandle(any(), any(), any())
        );
    }

    @Test
    @DisplayName("Options 검사 - 성공, 모든 api 요청에서 OPTIONS일 때를 제외시키면 preHandle에서 true를 반환한다.")
    void preHandleWithOptions() throws Exception {
        // given
        given(httpServletRequest.getRequestURI())
                .willReturn("/api/workbooks");
        given(httpServletRequest.getMethod())
                .willReturn(HttpMethod.OPTIONS.toString());
        pathMatcherInterceptor.excludePathPatterns("/api/**", PathMethod.OPTIONS);

        // when, then
        assertThat(pathMatcherInterceptor.preHandle(httpServletRequest, null, null)).isTrue();
    }
}
