package botobo.core.domain.auth;

import botobo.core.ui.auth.PathMethod;
import botobo.core.ui.auth.PathMethods;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PathMethodsTest {

    @Test
    @DisplayName("PathMethod가 PathMethods에 존재할 때 true, 아닐 때는 false를 반환한다.")
    void contains() {
        // given
        PathMethods pathMethods = new PathMethods(Set.of(PathMethod.GET, PathMethod.POST));

        // when, then
        assertThat(pathMethods.contains(PathMethod.GET)).isTrue();
        assertThat(pathMethods.contains(PathMethod.POST)).isTrue();
        assertThat(pathMethods.contains(PathMethod.PUT)).isFalse();
    }

    @Test
    @DisplayName("PathMethod가 ANY일 때는 어떤 method가 와도 true를 반환한다.")
    void containsWithAny() {
        // given
        PathMethods pathMethods = new PathMethods(Set.of(PathMethod.ANY));

        // when, then
        assertThat(pathMethods.contains(PathMethod.GET)).isTrue();
        assertThat(pathMethods.contains(PathMethod.POST)).isTrue();
        assertThat(pathMethods.contains(PathMethod.PUT)).isTrue();
        assertThat(pathMethods.contains(PathMethod.HEAD)).isTrue();
        assertThat(pathMethods.contains(PathMethod.PATCH)).isTrue();
        assertThat(pathMethods.contains(PathMethod.TRACE)).isTrue();
        assertThat(pathMethods.contains(PathMethod.DELETE)).isTrue();
        assertThat(pathMethods.contains(PathMethod.OPTIONS)).isTrue();
    }
}
