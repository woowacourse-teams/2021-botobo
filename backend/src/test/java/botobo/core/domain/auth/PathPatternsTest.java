package botobo.core.domain.auth;

import botobo.core.ui.auth.PathMethod;
import botobo.core.ui.auth.PathPatterns;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternsTest {

    @Test
    @DisplayName("제외되거나 추가되지 않은 경로는 true, 추가된 경로는 false를 반환한다.")
    void isExcludedPath() {
        // given
        PathPatterns pathPatterns = new PathPatterns();
        pathPatterns.excludePathPatterns("/api/login", PathMethod.POST);
        pathPatterns.addPathPatterns("/api/workbooks", PathMethod.GET);

        // when, then
        assertThat(pathPatterns.isExcludedPath("/api/login", PathMethod.POST)).isTrue();
        assertThat(pathPatterns.isExcludedPath("/api/workbooks", PathMethod.PATCH)).isTrue();
        assertThat(pathPatterns.isExcludedPath("/api/workbooks", PathMethod.GET)).isFalse();
    }

    @Test
    @DisplayName("추가된 경로의 PathMethod가 ANY면 항상 false를 반환한다.")
    void isExcludedPathWithPathMethodAny() {
        // given
        PathPatterns pathPatterns = new PathPatterns();
        pathPatterns.addPathPatterns("/api/**", PathMethod.ANY);

        // when, then
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.POST)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.GET)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.PUT)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.HEAD)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.PATCH)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.TRACE)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.DELETE)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/api/**", PathMethod.OPTIONS)).isFalse();
    }
}
