package ddip.me.ddipbe.presentation;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest extends IntegrationTest {

    @AfterEach
    void tearDown() {
        truncateDatabase();
    }

    @DisplayName("회원 가입을 성공한다.")
    @Test
    void signup_success() {
        // given
        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(json.read("$.data.memberId", Long.class)).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("회원 가입을 실패한다. (올바르지 않은 이메일 형식)")
    @ParameterizedTest
    @CsvSource(value = {
            "test",
            "test@",
            "@test.com",
            "test@test."})
    void signup_fail_wrongEmailFormat(String email) {
        // given
        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("회원 가입을 실패한다. (올바르지 않은 비밀번호 형식)")
    @ParameterizedTest
    @CsvSource(value = {
            "t1!",
            "test!@#$",
            "test1234",
            "1234!@#$",
            "testtest12341234!@#$!"})
    void signup_fail_wrongPasswordFormat(String password) {
        // given
        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("회원 가입을 실패한다. (이미 존재하는 이메일)")
    @Test
    void signup_fail_alreadyExistsEmail() {
        // given
        signup();

        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("로그인을 성공한다.")
    @Test
    void signin_success() {
        // given
        signup();

        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signin",
                HttpMethod.POST,
                httpEntity,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.data.memberId", Long.class)).isGreaterThanOrEqualTo(1L);
        assertThat(res.getHeaders().get("Set-Cookie")).isNotNull().anyMatch(cookie -> cookie.startsWith("JSESSIONID"));
    }

    @DisplayName("로그인을 실패한다. (존재하지 않는 이메일)")
    @Test
    void signin_fail_notExistsEmail() {
        // given
        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signin",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("로그인을 실패한다. (잘못된 비밀번호)")
    @Test
    void signin_fail_wrongPassword() {
        // given
        signup();

        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(EMAIL, "wrongPassword");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signin",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("로그아웃을 성공한다.")
    @Test
    void signout_success() {
        // given
        signup();
        String sessionCookie = signinForSessionCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/signout",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        asserThatSessionIsNotValid(sessionCookie);
    }

    private void asserThatSessionIsNotValid(String sessionCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = restTemplate.exchange("/members/me",
                HttpMethod.GET,
                httpEntity,
                String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @DisplayName("로그인 상태에서 내 정보를 조회한다.")
    @Test
    void getMe_success() {
        // given
        signup();
        String sessionCookie = signinForSessionCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/me",
                HttpMethod.GET,
                httpEntity,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.data.id", Long.class)).isGreaterThanOrEqualTo(1L);
        assertThat(json.read("$.data.email", String.class)).isEqualTo(EMAIL);
    }

    @DisplayName("로그인 상태가 아닌 경우 내 정보 조회를 실패한다.")
    @Test
    void getMe_fail_notSignedIn() {
        // given
        signup();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange("/members/me",
                HttpMethod.GET,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}