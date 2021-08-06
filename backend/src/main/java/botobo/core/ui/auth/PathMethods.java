package botobo.core.ui.auth;

import java.util.Set;

public class PathMethods {
    private final Set<PathMethod> pathMethods;

    public PathMethods(Set<PathMethod> pathMethods) {
        this.pathMethods = pathMethods;
    }

    public boolean contains(PathMethod pathMethod) {
        if (pathMethods.contains(PathMethod.ANY)) {
            return true;
        }
        return pathMethods.contains(pathMethod);
    }
}
