package botobo.core.domain.auth;

import botobo.core.ui.auth.PathMethod;
import botobo.core.ui.auth.PathPatterns;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternsTest {

    @Test
    @DisplayName("경로 검사 - 성공, 제외되거나 추가되지 않은 경로는 true, 추가된 경로는 false를 반환한다.")
    void isExcludedPath() {
        // given
        PathPatterns pathPatterns = new PathPatterns();
        pathPatterns.excludePathPatterns("/login", PathMethod.POST);
        pathPatterns.addPathPatterns("/workbooks", PathMethod.GET);

        // when, then
        assertThat(pathPatterns.isExcludedPath("/login", PathMethod.POST)).isTrue();
        assertThat(pathPatterns.isExcludedPath("/workbooks", PathMethod.PATCH)).isTrue();
        assertThat(pathPatterns.isExcludedPath("/workbooks", PathMethod.GET)).isFalse();
    }

    @Test
    @DisplayName("PathMethod.ANY 검사 - 성공, 추가된 경로의 PathMethod가 ANY면 항상 false를 반환한다.")
    void isExcludedPathWithPathMethodAny() {
        // given
        PathPatterns pathPatterns = new PathPatterns();
        pathPatterns.addPathPatterns("/**", PathMethod.ANY);

        // when, then
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.POST)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.GET)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.PUT)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.HEAD)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.PATCH)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.TRACE)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.DELETE)).isFalse();
        assertThat(pathPatterns.isExcludedPath("/**", PathMethod.OPTIONS)).isFalse();
    }
}
