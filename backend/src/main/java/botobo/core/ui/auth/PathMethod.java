package botobo.core.ui.auth;

import java.util.Arrays;

public enum PathMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, ANY;

    public static PathMethod of(String httpMethod) {
        return Arrays.stream(values())
                .filter(pathMethod -> pathMethod.match(httpMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 PathMethod 입니다."));
    }

    private boolean match(String httpMethod) {
        return this.name().equals(httpMethod);
    }
}
