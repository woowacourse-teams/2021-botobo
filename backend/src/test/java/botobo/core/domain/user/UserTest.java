package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class UserTest {

    @Test
    @DisplayName("Builder를 이용한 User 객체 생성 - 성공")
    void createWithBuilder() {
        assertThatCode(() -> User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .build()
        ).doesNotThrowAnyException();
    }
}
