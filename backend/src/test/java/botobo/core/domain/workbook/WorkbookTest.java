package botobo.core.domain.workbook;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.exception.NotAuthorException;
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
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공, 태그와 함꼐 생성")
    void createWithTags() {
        //given
        Tags tags = Tags.from(Arrays.asList(
                Tag.from("자바"), Tag.from("java"), Tag.from("코딩")
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
        Tags manyTags = Tags.from(Arrays.asList(
                Tag.from("자바"), Tag.from("java"), Tag.from("코딩"), Tag.from("언어")
        ));

        // when, then
        assertThatThrownBy(() -> Workbook.builder()
                .name("자바 문제집")
                .tags(manyTags)
                .build())
                .isInstanceOf(WorkbookTagLimitException.class)
                .hasMessageContaining("문제집이 가질 수 있는 태그수는 최대")
                .hasMessageContaining("개 입니다.");
    }

    @Test
    @DisplayName("태그 추가 - 성공")
    void addTags() {
        //given
        Tags tags = Tags.from(
                Collections.singletonList(Tag.from("자바"))
        );
        Workbook workbook = Workbook.builder()
                .name("단계별 자바 문제집")
                .tags(tags)
                .build();
        assertThat(workbook.getWorkbookTags()).hasSize(1);

        // when
        Tags others = Tags.from(
                Collections.singletonList(Tag.from("스프링"))
        );
        workbook.addTags(others);

        // then
        assertThat(workbook.getWorkbookTags()).hasSize(2);
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

    @Test
    @DisplayName("유저가 자신의 문제집 삭제 - 성공")
    void deleteWorkbook() {
        // given
        User user = User.builder()
                .id(1L)
                .githubId(1L)
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

        // when
        workbook.deleteIfUserIsAuthor(user.getId());

        // then
        assertThat(workbook.isDeleted()).isTrue();
        assertThat(user.getWorkbooks().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("유저가 자신의 문제집 삭제 - 실패, 다른 유저가 삭제를 시도할 때")
    void deleteWorkbookWithOtherUser() {
        // given
        User user1 = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .githubId(2L)
                .userName("pk")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .build()
                .createBy(user1);

        // when, then
        assertThatThrownBy(() -> workbook.deleteIfUserIsAuthor(user2.getId()))
                .isInstanceOf(NotAuthorException.class);
    }
}