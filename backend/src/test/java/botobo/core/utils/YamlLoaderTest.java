package botobo.core.utils;

import botobo.core.utils.YamlLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class YamlLoaderTest {

    private final String resourceFileName = "application-fake.yml";

    @Test
    @DisplayName("fullKey의 value를 가지고 온다 - 성공")
    void extractValue() {
        // given
        String fullKey = "github.client.id";

        // when
        Object value = YamlLoader.extractValue(resourceFileName, fullKey);

        // then
        assertThat((String) value).isEqualTo("githubId");
    }

    @Test
    @DisplayName("fullKey가 다른 key -value 정보를 가지고 있을 때 맵을 반환한다 - 성공")
    void extractValueFromKeyWithMoreKeyPath() {
        // given
        String fullKey = "github";

        // when
        Object value = YamlLoader.extractValue(resourceFileName, fullKey);

        // then
        assertThat(value).isExactlyInstanceOf(LinkedHashMap.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"github.client.secret", "github.client.id.with.some.parts.that.do.not.exist", "none.existing.key"})
    @DisplayName("fullKey의 value가 없을 때 null을 반환한다 - 성공")
    void extractValueFromYamlKeyWithNoValue(String fullKey) {
        // given // when
        Object value = YamlLoader.extractValue(resourceFileName, fullKey);

        // then
        assertThat(value).isNull();
    }
}
