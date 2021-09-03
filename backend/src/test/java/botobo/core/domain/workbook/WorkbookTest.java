package botobo.core.domain.workbook;

import botobo.core.domain.card.Card;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.exception.workbook.WorkbookNameLengthException;
import botobo.core.exception.workbook.WorkbookNameBlankException;
import botobo.core.exception.workbook.WorkbookTagLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

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
        assertThat(workbook.getWorkbookTags()).isEmpty();
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
        // when, then
        assertThatThrownBy(() -> Workbook.builder()
                .name(stringGenerator(31))
                .build())
                .isInstanceOf(WorkbookNameLengthException.class);
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
                .isInstanceOf(WorkbookNameBlankException.class);
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공, 태그와 함꼐 생성")
    void createWithTags() {
        //given
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("java"), Tag.of("코딩")
        ));
        String workbookName = "단계별 자바 문제집";

        // when
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .tags(tags)
                .build();

        // then
        assertThat(workbook.getWorkbookTags()).hasSize(3);
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 실패, 최대 태그수 초과")
    void createWithManyTag() {
        // given
        Tags manyTags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("java"), Tag.of("코딩"), Tag.of("언어"), Tag.of("학습"), Tag.of("프로그램")
        ));

        // when, then
        assertThatThrownBy(() -> Workbook.builder()
                .name("자바 문제집")
                .tags(manyTags)
                .build())
                .isInstanceOf(WorkbookTagLimitException.class);
    }

    @Test
    @DisplayName("태그 추가 - 성공")
    void addTags() {
        //given
        Tags tags = Tags.of(
                Collections.singletonList(Tag.of("자바"))
        );
        Workbook workbook = Workbook.builder()
                .name("단계별 자바 문제집")
                .tags(tags)
                .build();
        assertThat(workbook.getWorkbookTags()).hasSize(1);

        // when
        Tags others = Tags.of(
                Collections.singletonList(Tag.of("스프링"))
        );
        workbook.addTags(others);

        // then
        assertThat(workbook.getWorkbookTags()).hasSize(2);
    }

    @Test
    @DisplayName("존재하는 유저 조회 - 성공, 문제집의 User 필드가 null이면 author는 '존재하지 않는 유저' 이다.")
    void authorWithNullUser() {
        // given
        Workbook workbook = Workbook.builder()
                .name("단계별 자바 문제집")
                .user(null)
                .build();

        // when, then
        assertThat(workbook.author()).isEqualTo("존재하지 않는 유저");
    }

    @Test
    @DisplayName("createBy를 하면 문제집의 author가 주어진 user로 바뀐다 - 성공")
    void createByWithNewAuthor() {
        // given
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        User newUser = User.builder()
                .id(1L)
                .socialId("1")
                .userName("pk")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        Workbook workbook = Workbook.builder()
                .name("단계별 자바 문제집")
                .user(user)
                .build();

        // when
        workbook.createBy(newUser);

        // then
        assertThat(workbook.author()).isEqualTo(newUser.getUserName());
    }

    @ValueSource(strings = {"java", "Java", "JAVA", "JaVa"})
    @ParameterizedTest
    @DisplayName("단어 포함 검사 - 성공, 문제집의 이름에 해당 단어가 포함되어 있는지 검사한다.(영어는 소문자로 변환하여 검사)")
    void containsWord(String word) {
        // given
        Workbook workbook = Workbook.builder()
                .name("Javascript")
                .build();

        // when, then
        assertTrue(workbook.containsWord(word));
    }

    @Test
    @DisplayName("문제집 수정 - 성공")
    void updateWorkbook() {
        // given
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("java"), Tag.of("코딩")
        ));

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(tags)
                .build();

        Tags updatedTags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("중급")
        ));

        Workbook updateWorkbook = Workbook.builder()
                .name("오즈의 Java를 다 잡아")
                .opened(false)
                .tags(updatedTags)
                .build();

        // when
        workbook.update(updateWorkbook);

        // then
        assertThat(workbook.getName()).isEqualTo(updateWorkbook.getName());
        assertThat(workbook.isOpened()).isFalse();
        assertThat(workbook.getWorkbookTags()).extracting("tag")
                .contains(Tag.of("자바"), Tag.of("중급"));
    }

    @Test
    @DisplayName("유저가 자신의 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        Tags tags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("java"), Tag.of("코딩")
        ));

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .tags(tags)
                .build()
                .createBy(user);

        // when
        workbook.delete();

        // then
        assertThat(workbook.isDeleted()).isTrue();
        assertThat(user.getWorkbooks()).isEmpty();
        assertThat(workbook.getWorkbookTags()).extracting("deleted")
                .doesNotContain(false);
    }

    @Test
    @DisplayName("유저가 자신의 문제집과 추가된 카드도 삭제 - 성공")
    void deleteWorkbookWithCard() {
        // given
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .build()
                .createBy(user);

        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .deleted(false)
                .build();

        // when
        workbook.delete();

        // then
        assertThat(workbook.isDeleted()).isTrue();
        assertThat(user.getWorkbooks().size()).isEqualTo(0);
        assertThat(card.isDeleted()).isTrue();
    }
}