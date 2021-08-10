package botobo.core.exception.common;

import botobo.core.exception.ExternalException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.http.InternalServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorTypeTest {

    @Test
    @DisplayName("클래스 타입으로 ErrorTye 생성 - 성공")
    void createWithClassType() {
        // given
        ErrorType errorType = ErrorType.of(TokenNotValidException.class);

        // when, then
        assertThat(errorType).isEqualTo(ErrorType.A001);
    }

    @Test
    @DisplayName("클래스 타입으로 ErrorType 생성 - 실패, ExternalException은 불가")
    void createWithNonExistentClassType() {
        // when, then
        assertThatThrownBy(() -> ErrorType.of(ExternalException.class))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("코드로 ErrorTye 생성 - 성공")
    void createWithCode() {
        // given
        ErrorType errorType = ErrorType.of("W004");

        // when, then
        assertThat(errorType).isEqualTo(ErrorType.W004);
    }

    @Test
    @DisplayName("ErrorType에 존재하지 않는 코드로 ErrorTye 생성 - 성공, ErrorType.X001")
    void createWithNonExistentCode() {
        // given
        ErrorType errorType = ErrorType.of("W9876");

        // when, then
        assertThat(errorType).isEqualTo(ErrorType.X001);
    }
}