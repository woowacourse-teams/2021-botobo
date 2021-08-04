package botobo.core.ui.auth;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathMatcherInterceptor implements HandlerInterceptor {
    private final HandlerInterceptor handlerInterceptor;
    private final PathPatterns pathPatterns;

    public PathMatcherInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
        this.pathPatterns = new PathPatterns();
    }

    public PathMatcherInterceptor addPathPatterns(String pathPattern, PathMethod... pathMethods) {
        pathPatterns.addPathPatterns(pathPattern, pathMethods);
        return this;
    }

    public PathMatcherInterceptor excludePathPatterns(String pathPattern, PathMethod... pathMethods) {
        pathPatterns.excludePathPatterns(pathPattern, pathMethods);
        return this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        if (pathPatterns.isExcludedPath(uri, httpMethod)) {
            return true;
        }
        return handlerInterceptor.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        handlerInterceptor.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        handlerInterceptor.afterCompletion(request, response, handler, ex);
    }
}
