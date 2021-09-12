package botobo.core.ui.search;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SearchArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SearchParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> parameterMap = extractMap(request);

        return WorkbookSearchParameter.builder()
                .searchKeyword(parameterMap.getOrDefault("keyword", null))
                .searchCriteria(parameterMap.getOrDefault("criteria", null))
                .start(parameterMap.getOrDefault("start", null))
                .size(parameterMap.getOrDefault("size", null))
                .build();
    }

    private Map<String, String> extractMap(HttpServletRequest request) {
        Map<String, String> parameterMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, request.getParameter(name));
        }
        return parameterMap;
    }
}
