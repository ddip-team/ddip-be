package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.global.util.TestCustomClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class IntegrationTest {

    protected static final String UUID_REGEX = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected TestCustomClock testCustomClock = new TestCustomClock();
    protected static ZonedDateTime now = ZonedDateTime.now();
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));

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
    protected long signup() {
        return signup(EMAIL, PASSWORD);
    }

    protected long signup(String email, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, passwordEncoder.encode(password));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    // 로그인 후 세션 쿠키 반환
    protected String signinForSessionCookie() {
        return signinForSessionCookie(EMAIL, PASSWORD);
    }

    protected String signinForSessionCookie(String email, String password) {
        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        ResponseEntity<String> res = restTemplate.exchange("/members/signin",
                HttpMethod.POST,
                createHttpEntity(body, null),
                String.class);

        // get the cookie value that has JSESSIONID as key
        String sessionCookieKey = "JSESSIONID";
        return Objects.requireNonNull(res.getHeaders().get(HttpHeaders.SET_COOKIE)).stream()
                .filter(cookie -> cookie.contains(sessionCookieKey))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(sessionCookieKey + " not found"));
    }

    protected HttpEntity<?> createHttpEntity(String body, String sessionCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (sessionCookie != null) {
            headers.set(HttpHeaders.COOKIE, sessionCookie);
        }
        return new HttpEntity<>(body, headers);
    }

    // 생성해 DB에 저장할 에정된 이벤트, 진행중인 이벤트, 종료된 이벤트 개수를 인자로 받아서 저장 후 UUID 목록을 반환한다. (UUID는 인자의 순서와 개수대로 반환)
    protected List<UUID> addEvents(long memberId, int preOpenCount, int openCount, int closeCount) {
        List<UUID> uuids = new ArrayList<>();
        int seconds = 0;
        for (int i = 0; i < preOpenCount; i++) {
            UUID uuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now.plusSeconds(seconds++));
            uuids.add(uuid);
        }
        for (int i = 0; i < openCount; i++) {
            UUID uuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now.plusSeconds(seconds++));
            uuids.add(uuid);
        }
        for (int i = 0; i < closeCount; i++) {
            UUID uuid = addEvent(memberId, now.minusDays(2), now.minusDays(1), now.plusSeconds(seconds++));
            uuids.add(uuid);
        }
        return uuids;
    }

    protected UUID addEvent(long memberId, ZonedDateTime startDateTime, ZonedDateTime endDateTime, ZonedDateTime createdAt) {
        UUID uuid = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO event (uuid, member_id, title, limit_count, remain_count, success_content, success_image_url, thumbnail_image_url, start_date_time, end_date_time, success_form, created_at, updated_at) VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                uuid.toString(),
                memberId,
                "title",
                10,
                10,
                "content",
                "https://cdn.ddip.me/success-image.png",
                "https://cdn.ddip.me/thumbnail-image.png",
                startDateTime.toLocalDateTime(),
                endDateTime.toLocalDateTime(),
                "{ \"test\": \"test\"}",
                createdAt.toLocalDateTime(),
                createdAt.toLocalDateTime());
        return uuid;
    }

    protected List<String> addSuccessRecords(UUID eventUuid, int count, boolean isFormInput) {
        Long eventId = jdbcTemplate.queryForObject("SELECT id FROM event WHERE uuid = UUID_TO_BIN(?)", Long.class, eventUuid.toString());

        List<String> tokens = new ArrayList<>();
        int seconds = 0;
        for (int i = 0; i < count; i++) {
            String token = UUID.randomUUID().toString();
            LocalDateTime timestamp = now.toLocalDateTime().plusSeconds(seconds++);
            jdbcTemplate.update("INSERT INTO success_record (event_id, token, form_input_value, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
                    eventId,
                    token,
                    isFormInput ? "{ \"test\": \"test\"}" : null,
                    timestamp,
                    timestamp);
            tokens.add(token);
        }
        return tokens;
    }
}
