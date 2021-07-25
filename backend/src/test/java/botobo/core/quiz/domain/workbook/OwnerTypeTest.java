package botobo.core.quiz.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerTypeTest {

    @ValueSource(strings = {"mine", "MINE", "MinE"})
    @ParameterizedTest
    @DisplayName("mine OwnerType 객체 생성 - 성공")
    void createWithMine(String value) {
        // when, then
        assertThat(OwnerType.from(value)).isEqualTo(OwnerType.MINE);
    }

    @ValueSource(strings = {"all", "ALL", "AlL"})
    @ParameterizedTest
    @DisplayName("all OwnerType 객체 생성 - 성공")
    void createWithAll(String value) {
        // when, then
        assertThat(OwnerType.from(value)).isEqualTo(OwnerType.ALL);
    }

    @NullAndEmptySource
    @ValueSource(strings = {"  ", "anything value", "default is all"})
    @ParameterizedTest
    @DisplayName("OwnerType 객체 생성 시 인자가 mine, all이 아닐 시 기본 값은 all이다.")
    void createDefault(String value) {
        // when, then
        assertThat(OwnerType.from(value)).isEqualTo(OwnerType.ALL);
    }
}