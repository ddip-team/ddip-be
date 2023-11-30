package ddip.me.ddipbe.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

public abstract class IntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected void truncateDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbcTemplate.execute("TRUNCATE TABLE success_record");
        jdbcTemplate.execute("TRUNCATE TABLE event");
        jdbcTemplate.execute("TRUNCATE TABLE member");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    protected static final String EMAIL = "test@test.com";
    protected static final String PASSWORD = "test1234!@#$";

    // 회원 가입
    protected void signup() {
        jdbcTemplate.update("INSERT INTO member (email, password) VALUES (?, ?)", EMAIL, passwordEncoder.encode(PASSWORD));
    }

    // 로그인 후 세션 쿠키 반환
    protected String signinForSessionCookie() {
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

        ResponseEntity<String> res = restTemplate.exchange("/members/signin",
                HttpMethod.POST,
                httpEntity,
                String.class);

        // get the cookie value that has JSESSIONID as key
        String sessionCookieKey = "JSESSIONID";
        return Objects.requireNonNull(res.getHeaders().get(HttpHeaders.SET_COOKIE)).stream()
                .filter(cookie -> cookie.contains(sessionCookieKey))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(sessionCookieKey + " not found"));
    }
}
