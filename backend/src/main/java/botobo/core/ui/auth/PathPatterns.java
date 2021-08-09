package botobo.core.ui.auth;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PathPatterns {
    private final Map<String, PathMethods> includePatterns;
    private final Map<String, PathMethods> excludePatterns;
    private final PathMatcher pathMatcher;

    public PathPatterns() {
        this.includePatterns = new HashMap<>();
        this.excludePatterns = new HashMap<>();
        this.pathMatcher = new AntPathMatcher();
    }

    public void addPathPatterns(String pathPattern, PathMethod... pathMethods) {
        includePatterns.put(pathPattern, new PathMethods(Set.of(pathMethods)));
    }

    public void excludePathPatterns(String pathPattern, PathMethod... pathMethods) {
        excludePatterns.put(pathPattern, new PathMethods(Set.of(pathMethods)));
    }

    public boolean isExcludedPath(String uri, PathMethod pathMethod) {
        return matchPatterns(excludePatterns, uri, pathMethod) || !matchPatterns(includePatterns, uri, pathMethod);
    }

    private boolean matchPatterns(Map<String, PathMethods> patterns, String uri, PathMethod pathMethod) {
        return patterns.keySet().stream()
                .filter(pattern -> pathMatcher.match(pattern, uri))
                .anyMatch(pattern -> patterns.get(pattern).contains(pathMethod));
    }
}
