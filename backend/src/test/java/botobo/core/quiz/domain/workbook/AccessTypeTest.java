package botobo.core.quiz.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTypeTest {

    @ValueSource(strings = {"public", "PUBLIC", "PuBLic"})
    @ParameterizedTest
    @DisplayName("public AccessType 객체 생성 - 성공")
    void createPublicAccess(String value) {
        // when, then
        assertThat(AccessType.from(value)).isEqualTo(AccessType.PUBLIC);
    }

    @ValueSource(strings = {"private", "PRIVATE", "pRivaTe"})
    @ParameterizedTest
    @DisplayName("private AccessType 객체 생성 - 성공")
    void createPrivateAccess(String value) {
        // when, then
        assertThat(AccessType.from(value)).isEqualTo(AccessType.PRIVATE);
    }

    @ValueSource(strings = {"all", "ALL", "aLl"})
    @ParameterizedTest
    @DisplayName("all AccessType 객체 생성 - 성공")
    void createAllAccess(String value) {
        // when, then
        assertThat(AccessType.from(value)).isEqualTo(AccessType.ALL);
    }

    @NullAndEmptySource
    @ValueSource(strings = {"  ", "anything value", "default is public"})
    @ParameterizedTest
    @DisplayName("AccessType 객체 생성 시 인자가 public, private, all이 아닐 시 기본 값은 public이다.")
    void createDefaultAccess(String value) {
        // when, then
        assertThat(AccessType.from(value)).isEqualTo(AccessType.PUBLIC);
    }
}