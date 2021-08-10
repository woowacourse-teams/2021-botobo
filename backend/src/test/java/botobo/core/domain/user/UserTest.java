package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @ValueSource(strings = {"oz", "oz1", "oz2"})
    @DisplayName("현재 회원명과 같은 회원명이 인자로 들어오면 뒤에 숫자 1을 추가한다. - 성공")
    void changeUserName(String name) {
        // given
        User user = User.builder()
                .userName(name)
                .build();

        // when
        user.changeUserName(name);

        // then
        assertThat(user.getUserName()).isEqualTo(name + 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"oz1", "oz702", "oz1203"})
    @DisplayName("현재 회원명과 앞부분이 같은 회원명이 인자로 들어오면 뒤에 숫자 1을 더한 값을 추가한다. - 성공")
    void changeUserNameWithSamePrefix(String name) {
        // given
        User user = User.builder()
                .userName(name)
                .build();
        String suffix = name.replace("oz", "");

        // when
        user.changeUserName("oz");

        // then
        assertThat(user.getUserName()).isEqualTo("oz" + (Integer.parseInt(suffix) + 1));
    }
}
