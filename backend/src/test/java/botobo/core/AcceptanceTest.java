package botobo.core;

import botobo.core.utils.RequestBuilder;
import botobo.core.utils.RequestBuilder.HttpFunction;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AcceptanceTest {
    @LocalServerPort
    private int port;

    private RequestBuilder requestBuilder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        String defaultToken = jwtTokenProvider.createToken(100L);
        requestBuilder = new RequestBuilder(defaultToken);
    }

    /**
     * example:
     * request()
     * .get("/path")   http method type
     * .auth()         default: false
     * .build();
     *
     * - logging is default
     */
    protected HttpFunction request() {
        return requestBuilder.build();
    }
}
