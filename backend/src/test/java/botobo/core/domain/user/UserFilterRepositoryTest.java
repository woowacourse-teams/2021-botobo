package botobo.core.domain.user;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.FilterRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({UserFilterRepository.class, QuerydslConfig.class})
class UserFilterRepositoryTest extends FilterRepositoryTest {

    @Autowired
    private UserFilterRepository userFilterRepository;

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 영어는 대소문자를 구분하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenEng() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("java");

        // then
        assertThat(users).hasSize(5);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 일치하는 문제집 없음.")
    @Test
    void findAllByContainsWorkbookNameWhenEmptyResult() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("바보");

        // then
        assertThat(users).isEmpty();
    }

    @DisplayName("한글 문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공")
    @Test
    void findAllByContainsKoreanWorkbookName() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("문제집");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 카드가 없는 경우는 조회하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenCardsEmpty() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("없는");

        // then
        assertThat(users).isEmpty();
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 비공개 문제집은 조회하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenOpened() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("비공개");

        // then
        assertThat(users).isEmpty();
    }


    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 문제집 명이 null일 때 빈 리스트를 반환한다.")
    @Test
    void findAllByWorkbookNameNull() {
        // given - when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName(null);

        // then
        assertThat(users).isEmpty();
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 키워드가 태그와 일치할 때에도 가져온다.")
    @Test
    void findAllByWorkbookNameEqualsTagName() {
        // given - when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("자바짱");

        // then
        assertThat(users).hasSize(1);
    }
}
