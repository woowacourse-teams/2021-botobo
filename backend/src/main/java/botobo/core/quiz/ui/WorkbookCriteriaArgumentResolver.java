package botobo.core.quiz.ui;

import botobo.core.quiz.domain.workbook.WorkbookCriteria;
import botobo.core.quiz.domain.workbook.WorkbookSearchParams;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WorkbookCriteriaArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String KEYWORD = "keyword";
    private static final String OPENED = "opened";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WorkbookSearchParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> parameterMap = extractMap(request);

        return WorkbookCriteria.builder()
                .keyword(parameterMap.getOrDefault(KEYWORD, null))
                .opened(parameterMap.getOrDefault(OPENED, null))
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
