package botobo.core.ui.auth;

import java.util.List;

public class PathMethods {
    private final List<PathMethod> pathMethods;

    public PathMethods(List<PathMethod> pathMethods) {
        this.pathMethods = pathMethods;
    }

    public boolean match(String httpMethod) {
        if (pathMethods.contains(PathMethod.ANY)) {
            return true;
        }
        return pathMethods.stream()
                .anyMatch(pathMethod -> pathMethod.match(httpMethod));
    }
}
