package botobo.core.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkbookTest {

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공")
    void createWithBuilder() {
        //given
        String workbookName = "단계별 자바 문제집";

        // when
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .build();

        // then
        assertThat(workbook.getName()).isEqualTo(workbookName);
        assertFalse(workbook.isDeleted());
        assertFalse(workbook.isOpened());
        assertNotNull(workbook.getCards());
        assertThat(workbook.getCards().getCards()).hasSize(0);
        assertNull(workbook.getUser());
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공, 유효한 길이의 이름")
    void createWithValidLengthName() {
        // when, then
        assertThatCode(() -> Workbook.builder()
                .name(stringGenerator(30))
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 실패, 긴 길이의 이름")
    void createWithLongName() {
        // whenm, then
        assertThatThrownBy(() -> Workbook.builder()
                .name(stringGenerator(31))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {" ", "    ", "\t", "\r", "\r\n", "\n"})
    @DisplayName("Builder를 이용한 Workbook 생성 - 실패, name은 null, empty or blank String이 될 수 없다.")
    void createWithInvalidName(String name) {
        // when, then
        assertThatThrownBy(() -> Workbook.builder()
                .name(name)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("문제집의 User 필드가 null이면 author는 '존재하지 않는 유저' 이다.")
    void authorWithNullUser() {
        // given
        Workbook workbook = Workbook.builder()
                .name("단계별 자바 문제집")
                .user(null)
                .build();

        // when, then
        assertThat(workbook.author()).isEqualTo("존재하지 않는 유저");
    }

    @ValueSource(strings = {"java", "Java", "JAVA", "JaVa"})
    @ParameterizedTest
    @DisplayName("문제집의 이름에 해당 단어가 포함되어 있는지 검사한다.(영어는 소문자로 변환하여 검사)")
    void containsWord(String word) {
        // given
        Workbook workbook = Workbook.builder()
                .name("Javascript")
                .build();

        // when, then
        assertTrue(workbook.containsWord(word));
    }
}