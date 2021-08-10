package botobo.core.domain.heart;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.heart.HeartsCreationFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeartsTest {

    private Workbook workbook;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .id(1L)
                .name("문제집")
                .build();
    }

    @Test
    @DisplayName("Hearts 객체 생성 - 성공")
    void create() {
        // when
        Hearts hearts = Hearts.of(Arrays.asList(
                Heart.builder().workbook(workbook).userId(1L).build(),
                Heart.builder().workbook(workbook).userId(2L).build(),
                Heart.builder().workbook(workbook).userId(3L).build()
        ));

        // then
        assertThat(hearts.getHearts()).extracting(Heart::getUserId)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Hearts 객체 생성 - 실패, 같은 문제집을 가진 하트만 포함해야 한다.")
    void createWithDifferentWorkbook() {
        // given
        Workbook differentWorkbook = Workbook.builder()
                .id(2L)
                .name("문제집")
                .build();

        // when, then
        assertThatThrownBy(
                () -> Hearts.of(Arrays.asList(
                        Heart.builder().workbook(workbook).userId(1L).build(),
                        Heart.builder().workbook(workbook).userId(2L).build(),
                        Heart.builder().workbook(differentWorkbook).userId(3L).build()
                ))
        ).isInstanceOf(HeartsCreationFailureException.class);
    }

    @Test
    @DisplayName("Hearts 객체 생성 - 실패, 다른 유저 아이디를 가진 하트만 포함해야 한다.")
    void createWithSameUserId() {
        // when, then
        assertThatThrownBy(
                () -> Hearts.of(Arrays.asList(
                        Heart.builder().workbook(workbook).userId(1L).build(),
                        Heart.builder().workbook(workbook).userId(2L).build(),
                        Heart.builder().workbook(workbook).userId(1L).build()
                ))
        ).isInstanceOf(HeartsCreationFailureException.class);
    }

    @Test
    @DisplayName("Hearts에 유저 아이디가 있는지 확인한다..")
    void contains() {
        // given
        Hearts hearts = Hearts.of(Arrays.asList(
                Heart.builder().workbook(workbook).id(1L).userId(1L).build(),
                Heart.builder().workbook(workbook).id(2L).userId(2L).build()
        ));

        // when, then
        assertThat(hearts.contains(1L)).isTrue();
        assertThat(hearts.contains(3L)).isFalse();
    }

    @Test
    @DisplayName("하트를 누른다.")
    void toggleOnHeart() {
        // given
        Hearts hearts = Hearts.of(Arrays.asList(
                Heart.builder().workbook(workbook).id(1L).userId(1L).build()
        ));

        // when, then
        hearts.toggleHeart(
                Heart.builder().workbook(workbook).id(2L).userId(2L).build()
        );
        assertThat(hearts.getHearts()).extracting(Heart::getUserId)
                .containsExactly(1L, 2L);

        hearts.toggleHeart(
                Heart.builder().workbook(workbook).id(3L).userId(3L).build()
        );
        assertThat(hearts.getHearts()).extracting(Heart::getId)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("하트를 취소한다.")
    void toggleOffHeart() {
        // given
        Hearts hearts = Hearts.of(Arrays.asList(
                Heart.builder().workbook(workbook).id(1L).userId(1L).build(),
                Heart.builder().workbook(workbook).id(2L).userId(2L).build(),
                Heart.builder().workbook(workbook).id(3L).userId(3L).build()
        ));

        // when, then
        hearts.toggleHeart(
                Heart.builder().workbook(workbook).id(2L).userId(2L).build()
        );
        assertThat(hearts.getHearts()).extracting(Heart::getUserId)
                .containsExactly(1L, 3L);

        hearts.toggleHeart(
                Heart.builder().workbook(workbook).id(3L).userId(3L).build()
        );
        assertThat(hearts.getHearts()).extracting(Heart::getUserId)
                .containsExactly(1L);
    }
}