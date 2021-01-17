package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인을 시도한다")
    @Test
    void tryLogin(){
        // given
        회원_등록되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }


    private void 회원_등록되어_있음(String email, String password, int age) {
        ExtractableResponse<Response> response = MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(new TokenRequest(email, password))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

}
