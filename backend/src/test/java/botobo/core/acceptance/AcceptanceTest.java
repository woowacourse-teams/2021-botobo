package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.acceptance.utils.RequestBuilder.HttpFunction;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private RequestBuilder requestBuilder;

    @BeforeEach
    protected void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
            databaseCleaner.afterPropertiesSet();
        }
        requestBuilder = new RequestBuilder();
    }

    @AfterEach
    protected void tearDown() {
        databaseCleaner.execute();
    }

    /**
     * example:
     * <로그인이 필요한 경우>
     * request()
     * .get(path, params)   http method type
     * .queryParam(name, value)     optional
     * .auth(CREATE_TOKEN(USER.getId())         default: false
     * .log()          default: false
     * .build();
     * <로그인이 필요하지 않은 경우>
     * request()
     * .post(path, body)   http method type
     * .build();
     * <등록되지 않은 유저가 필요한 경우>
     * request()
     * .post(path, body)   http method type
     * .auth(CREATE_TOKEN(-100L))
     * .build();
     */
    protected HttpFunction request() {
        return requestBuilder.build();
    }
}
