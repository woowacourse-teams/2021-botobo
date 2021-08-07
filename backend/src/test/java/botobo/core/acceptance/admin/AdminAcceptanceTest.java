package botobo.core.acceptance.admin;

import botobo.core.acceptance.AcceptanceTest;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminCardResponse;
import botobo.core.dto.admin.AdminWorkbookRequest;
import botobo.core.dto.admin.AdminWorkbookResponse;
import botobo.core.exception.ErrorResponse;
import botobo.core.infrastructure.JwtTokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static botobo.core.utils.TestUtils.extractId;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin 인수 테스트")
public class AdminAcceptanceTest extends AcceptanceTest {

    private static final AdminWorkbookRequest ADMIN_WORKBOOK_REQUEST =
            new AdminWorkbookRequest("관리자의 문제집");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User admin;

    @BeforeEach
    void setUser() {
        admin = User.builder()
                .socialId("1")
                .userName("admin")
                .profileUrl("github.io")
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
    }


    @Test
    @DisplayName("관리자 문제집 생성 - 성공")
    void createWorkbook() {
        // given

        final ExtractableResponse<Response> response = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, admin);

        // when
        final AdminWorkbookResponse adminWorkbookResponse = response.as(AdminWorkbookResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminWorkbookResponse.getId()).isNotNull();
        assertThat(adminWorkbookResponse.getName()).isEqualTo("관리자의 문제집");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name이 null일 때")
    void createWorkbookWithNullName() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithNullName = new AdminWorkbookRequest(null);
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithNullName, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }


    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name은 최소 1글자")
    void createWorkbookWithInvalidLengthWithZero() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest("");
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name에 공백만 들어가는 경우")
    void createWorkbookWithOnlyWhiteSpace() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest("     ");
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, name은 최대 30글자")
    void createWorkbookWithInvalidLengthWith31() {
        //given
        AdminWorkbookRequest adminWorkbookRequestWithInvalidName = new AdminWorkbookRequest(
                stringGenerator(31));
        ExtractableResponse<Response> response = 문제집_생성_요청(adminWorkbookRequestWithInvalidName, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("문제집명은 최소 1글자, 최대 30글자만 가능합니다.");
    }

    @Test
    @DisplayName("관리자 문제집 생성 - 실패, admin이 아닐 경우")
    void createWorkbookWithNotAdmin() {
        //given
        User newUser = User.builder()
                .socialId("2")
                .userName("user")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        ExtractableResponse<Response> response = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, newUser);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(errorResponse.getMessage()).isEqualTo("Admin 권한이 아니기에 접근할 수 없습니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 성공")
    void createCard() {
        ExtractableResponse<Response> workbookResponse = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, admin);
        final Long workbookId = extractId(workbookResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest, admin);

        //when
        final AdminCardResponse adminCardResponse = response.as(AdminCardResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(adminCardResponse.getId()).isNotNull();
        assertThat(adminCardResponse.getQuestion()).isEqualTo("Question");
        assertThat(adminCardResponse.getAnswer()).isEqualTo("Answer");
        assertThat(adminCardResponse.getWorkbookId()).isEqualTo(workbookId);
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, question이 공백일 때")
    void createCardWithBlankQuestion() {
        ExtractableResponse<Response> workbookResponse = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, admin);
        final Long workbookId = extractId(workbookResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest("     ", "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, question이 null일 때")
    void createCardWithNullQuestion() {
        ExtractableResponse<Response> workbookResponse = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, admin);
        final Long workbookId = extractId(workbookResponse);

        AdminCardRequest adminCardRequest = new AdminCardRequest(null, "Answer", workbookId);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, workbookId가 null일 때")
    void createCardWithNullWorkbookId() {
        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", "Answer", null);
        final ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest, admin);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카드가 포함될 문제집 아이디는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, admin이 아닐 경우")
    void createCardWithNotAdmin() {
        //given
        ExtractableResponse<Response> workbookResponse = 문제집_생성_요청(ADMIN_WORKBOOK_REQUEST, admin);
        final Long workbookId = extractId(workbookResponse);

        User newUser = User.builder()
                .socialId("2")
                .userName("user")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        AdminCardRequest adminCardRequest = new AdminCardRequest("Question", "Answer", workbookId);

        ExtractableResponse<Response> response = 카드_생성_요청(adminCardRequest, newUser);

        //when
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(errorResponse.getMessage()).isEqualTo("Admin 권한이 아니기에 접근할 수 없습니다.");
    }

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest, User user) {
        return request()
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build()
                .extract();
    }

    public ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest, User user) {
        return request()
                .post("/api/admin/cards", adminCardRequest)
                .auth(jwtTokenProvider.createToken(user.getId()))
                .build()
                .extract();
    }
}


