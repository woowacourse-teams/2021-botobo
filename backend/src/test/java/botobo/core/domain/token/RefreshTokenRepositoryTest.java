package botobo.core.domain.token;

import botobo.core.config.EmbeddedRedisConfig;
import botobo.core.utils.YamlLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("test")
@Import(EmbeddedRedisConfig.class)
class RefreshTokenRepositoryTest {

    private static final String RESOURCE_FILE_NAME = "application-test.yml";
    private static final String FULL_KEY = "security.jwt.refresh-token.expire-length";
    private static final Long TIME_TO_LIVE = Long.valueOf(
            (Integer) Objects.requireNonNull(YamlLoader.extractValue(RESOURCE_FILE_NAME, FULL_KEY))
    );

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    RefreshToken savedRefreshToken;

    @BeforeEach
    void setUp() {
        savedRefreshToken = refreshTokenRepository.save(new RefreshToken("1", "tokenValue", TIME_TO_LIVE));
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @DisplayName("refresh token을 저장한다 - 성공")
    @Test
    void save() {
        // given
        String tokenId = "2";
        String tokenValue = "middleBearPK";
        RefreshToken refreshToken = new RefreshToken(tokenId, tokenValue, TIME_TO_LIVE);

        // when
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        // then
        assertThat(savedRefreshToken).usingRecursiveComparison()
                .isEqualTo(refreshToken);
    }

    @DisplayName("아이디를 이용하여 refresh token을 조회한다 - 성공")
    @Test
    void findById() {
        // get
        String id = savedRefreshToken.getId();

        // when, then
        assertThat(refreshTokenRepository.findById(id))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedRefreshToken);
    }

    @DisplayName("이미 Redis 에 존재하는 ID 의 RefreshToken 을 저장한다 - 성공, value 가 갱신되어 update 와 유사하게 동작한다.")
    @Test
    void saveWithExistentId() {
        // given
        String id = savedRefreshToken.getId();
        String updateTokenValue = "updatedTokenValue";
        RefreshToken updateToken = new RefreshToken(id, updateTokenValue, TIME_TO_LIVE);

        // when
        refreshTokenRepository.save(updateToken);

        // then
        assertThat(refreshTokenRepository.findById(id))
                .get()
                .extracting(RefreshToken::getTokenValue)
                .isEqualTo(updateTokenValue);
    }

    @DisplayName("refresh token을 삭제한다 - 성공")
    @Test
    void deleteById() {
        // given
        String id = savedRefreshToken.getId();
        assertThat(refreshTokenRepository.existsById(id)).isTrue();

        // when
        refreshTokenRepository.deleteById(id);

        // then
        assertThat(refreshTokenRepository.existsById(id)).isFalse();
    }
}
